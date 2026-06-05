package cl.municipalidad.bff.service;

import cl.municipalidad.bff.client.UserClient;
import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
import cl.municipalidad.bff.dto.TokenResponseDTO;
import cl.municipalidad.bff.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticacion del BFF.
 * Orquesta las llamadas al MS-Usuarios para login, registro y obtencion de datos del usuario.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: expone una interfaz simplificada al controller</li>
 *   <li>Single Responsibility: solo gestiona logica de autenticacion</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;

    /**
     * Autentica un usuario contra el MS-Usuarios.
     *
     * @param request DTO con email y password del usuario
     * @return TokenResponseDTO con el token JWT generado
     */
    public TokenResponseDTO login(LoginRequestDTO request) {
        return userClient.login(request);
    }

    /**
     * Registra un nuevo usuario en el MS-Usuarios.
     *
     * @param request DTO con los datos del nuevo usuario
     * @return UserDTO con los datos del usuario registrado
     */
    public UserDTO register(RegisterRequestDTO request) {
        return userClient.register(request);
    }

    /**
     * Obtiene los datos de un usuario por su email.
     *
     * @param email email del usuario a buscar
     * @return UserDTO con los datos del usuario
     */
    public UserDTO getUser(String email) {
        return userClient.findByEmail(email);
    }
}