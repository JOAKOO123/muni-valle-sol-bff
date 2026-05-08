package cl.municipalidad.bff.client;

import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.LoginResponseDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
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

    public LoginResponseDTO login(LoginRequestDTO request) {
        return msUsuariosClient.post()
                .uri("/api/usuarios/login")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(LoginResponseDTO.class)
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
                .bodyToMono(UsuarioDTO.class)
                .block();
    }
}
