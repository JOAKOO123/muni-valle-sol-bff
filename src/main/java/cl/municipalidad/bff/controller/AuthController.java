package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.LoginResponseDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
import cl.municipalidad.bff.dto.UserDTO;
import cl.municipalidad.bff.dto.TokenResponseDTO;
import cl.municipalidad.bff.service.AuthService;
import cl.municipalidad.bff.service.CookieService;
import cl.municipalidad.bff.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticacion del BFF.
 * Gestiona login, registro, logout y verificacion de sesion.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: delega logica al AuthService</li>
 *   <li>Single Responsibility: solo gestiona endpoints de autenticacion</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;
    private final JwtService jwtService;

    /**
     * Autentica un usuario y establece la cookie HttpOnly con el JWT.
     *
     * @param request  DTO con email y password
     * @param response HttpServletResponse para agregar la cookie
     * @return LoginResponseDTO con datos del usuario autenticado
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request,
            HttpServletResponse response) {

        TokenResponseDTO tokenResponse = authService.login(request);
        UserDTO user                   = authService.getUser(request.email());

        cookieService.setAuthCookie(response, tokenResponse.token());

        return ResponseEntity.ok(new LoginResponseDTO(
                user.id(),
                user.nombre(),
                user.email(),
                user.rol()
        ));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request DTO con los datos del nuevo usuario
     * @return UserDTO con los datos del usuario creado
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * Cierra la sesion del usuario limpiando la cookie de autenticacion.
     *
     * @param response HttpServletResponse para limpiar la cookie
     * @return respuesta sin contenido
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieService.clearAuthCookie(response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica la sesion activa del usuario mediante la cookie JWT.
     *
     * @param token token JWT extraido de la cookie HttpOnly
     * @return UserDTO con los datos del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(
            @CookieValue(name = "access_token", required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtService.extractEmail(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authService.getUser(email));
    }
}