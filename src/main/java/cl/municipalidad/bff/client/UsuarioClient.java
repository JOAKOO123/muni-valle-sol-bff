package cl.municipalidad.bff.client;

import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
import cl.municipalidad.bff.dto.TokenResponseDTO;
import cl.municipalidad.bff.dto.UsuarioDTO;
import cl.municipalidad.bff.exception.MsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UsuarioClient {

    @Qualifier("msUsuariosClient")
    private final WebClient msUsuariosClient;

    public TokenResponseDTO login(LoginRequestDTO request) {
        return msUsuariosClient.post()
                .uri("/api/usuarios/login")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(TokenResponseDTO.class)
                .block();
    }

    public UsuarioDTO registrar(RegisterRequestDTO request) {
        return msUsuariosClient.post()
                .uri("/api/usuarios/register")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Error al registrar usuario", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.CONFLICT::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("El email ya esta registrado", HttpStatus.CONFLICT)))
                .bodyToMono(UsuarioDTO.class)
                .block();
    }

    public UsuarioDTO obtenerPorEmail(String email) {
        return msUsuariosClient.get()
                .uri("/api/usuarios/email/{email}", email)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Usuario no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(UsuarioDTO.class)
                .block();
    }
}
