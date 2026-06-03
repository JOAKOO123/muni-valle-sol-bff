package cl.municipalidad.bff.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

/**
 * Servicio para procesamiento y validacion de tokens JWT.
 * Encapsula la logica de extraccion de claims del token.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Single Responsibility: solo gestiona operaciones JWT</li>
 *   <li>Singleton: bean de Spring con instancia unica</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Extrae el email del subject del token JWT.
     *
     * @param token token JWT a procesar
     * @return email del usuario o null si el token es invalido
     */
    public String extractEmail(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Valida si un token JWT es valido y no ha expirado.
     *
     * @param token token JWT a validar
     * @return true si el token es valido, false en caso contrario
     */
    public boolean isTokenValid(String token) {
        return extractEmail(token) != null;
    }
}
