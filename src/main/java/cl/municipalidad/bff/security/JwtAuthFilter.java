package cl.municipalidad.bff.security;

import cl.municipalidad.bff.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Filtro de autenticación y autorización para rutas protegidas del BFF.
 *
 * <p>Implementa una doble capa de seguridad sobre las rutas de gestión interna
 * municipal ({@code /api/brigadas/**} y {@code /api/alertas/**}):</p>
 * <ul>
 *   <li><b>Autenticación:</b> exige una cookie {@code access_token} con un JWT
 *       válido, verificado contra la clave pública RSA de MS-Usuarios mediante
 *       {@link JwtService}.</li>
 *   <li><b>Autorización:</b> exige que el rol contenido en el token sea
 *       {@code ADMIN} o {@code FUNCIONARIO}. Un {@code CIUDADANO} autenticado
 *       no puede operar sobre brigadas ni alertas.</li>
 * </ul>
 *
 * <p>Se manejan dos niveles de protección distintos:</p>
 * <ul>
 *   <li><b>Solo autenticación</b> ({@code /api/reportes/**}): exige un token
 *       válido de cualquier rol (ADMIN, FUNCIONARIO o CIUDADANO). Cualquier
 *       persona registrada puede reportar una emergencia, pero quien no tiene
 *       cuenta no puede. Esto le da al rol CIUDADANO un propósito real: tener
 *       cuenta habilita el acceso a reportar.</li>
 *   <li><b>Autenticación + rol</b> ({@code /api/brigadas/**}, {@code /api/alertas/**}):
 *       exige además que el rol sea {@code ADMIN} o {@code FUNCIONARIO}, ya que
 *       es gestión interna municipal.</li>
 * </ul>
 *
 * <p>Solo {@code /api/usuarios/register} y {@code /api/usuarios/login} quedan
 * completamente públicos, por necesidad lógica: nadie puede autenticarse para
 * poder autenticarse.</p>
 *
 * <p><b>Códigos de respuesta:</b>
 * <ul>
 *   <li>{@code 401 Unauthorized} — no hay token, está vacío, o es inválido/expirado.</li>
 *   <li>{@code 403 Forbidden} — el token es válido pero el rol no alcanza para
 *       la ruta solicitada (aplica solo a brigadas/alertas).</li>
 * </ul></p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 * @see JwtService
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /** Nombre de la cookie HttpOnly donde viaja el JWT, igual que en AuthController. */
    private static final String COOKIE_NAME = "access_token";

    /** Prefijos de ruta que solo requieren un token válido (cualquier rol). */
    private static final List<String> RUTAS_SOLO_AUTENTICACION = List.of(
            "/api/reportes"
    );

    /** Prefijos de ruta que requieren token válido Y rol autorizado. */
    private static final List<String> RUTAS_CON_ROL = List.of(
            "/api/brigadas",
            "/api/alertas"
    );

    /** Roles con permiso para operar sobre brigadas y alertas. */
    private static final Set<String> ROLES_AUTORIZADOS = Set.of("ADMIN", "FUNCIONARIO");

    /**
     * Intercepta cada request HTTP antes de llegar al controller.
     *
     * <p>Si la ruta no requiere protección, deja pasar la request sin validar
     * nada. Si requiere solo autenticación (reportes), exige token válido de
     * cualquier rol. Si requiere autenticación y rol (brigadas, alertas),
     * exige además que el rol sea ADMIN o FUNCIONARIO.</p>
     *
     * @param request  Solicitud HTTP entrante.
     * @param response Respuesta HTTP saliente, usada para escribir el error si corresponde.
     * @param filterChain Cadena de filtros a continuar si la validación es exitosa.
     * @throws ServletException si ocurre un error propio del servlet.
     * @throws IOException si ocurre un error al escribir la respuesta de error.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean requiereSoloAuth = RUTAS_SOLO_AUTENTICACION.stream().anyMatch(path::startsWith);
        boolean requiereRol = RUTAS_CON_ROL.stream().anyMatch(path::startsWith);

        if (!requiereSoloAuth && !requiereRol) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extraerTokenDeCookie(request);

        if (token == null || token.isBlank()) {
            escribirError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "No se encontró un token de autenticación. Inicie sesión para continuar.");
            return;
        }

        String email = jwtService.extractEmail(token);
        if (email == null) {
            escribirError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "El token de autenticación es inválido o ha expirado.");
            return;
        }

        if (requiereSoloAuth) {
            // Reportes: cualquier rol autenticado puede continuar.
            filterChain.doFilter(request, response);
            return;
        }

        String rol = jwtService.extractRol(token);
        if (rol == null || !ROLES_AUTORIZADOS.contains(rol)) {
            log.warn("Acceso denegado por rol insuficiente: email={}, rol={}, ruta={}", email, rol, path);
            escribirError(response, HttpServletResponse.SC_FORBIDDEN,
                    "No tiene permisos para acceder a este recurso. Se requiere rol ADMIN o FUNCIONARIO.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el valor del token desde la cookie {@code access_token}.
     *
     * @param request Solicitud HTTP de la cual leer las cookies.
     * @return El valor del token si la cookie existe, {@code null} en caso contrario.
     */
    private String extraerTokenDeCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Escribe una respuesta de error JSON con formato consistente al resto del BFF.
     *
     * @param response Respuesta HTTP donde escribir el error.
     * @param status   Código de estado HTTP (401 o 403).
     * @param mensaje  Mensaje descriptivo del motivo del rechazo.
     * @throws IOException si ocurre un error al escribir el cuerpo de la respuesta.
     */
    private void escribirError(HttpServletResponse response, int status, String mensaje) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String json = String.format(
                "{\"error\":\"%s\",\"timestamp\":\"%s\",\"status\":%d}",
                mensaje, LocalDateTime.now(), status
        );
        response.getWriter().write(json);
    }
}