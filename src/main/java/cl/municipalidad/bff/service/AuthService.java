package cl.municipalidad.bff.service;

import cl.municipalidad.bff.client.UsuarioClient;
import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.LoginResponseDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
import cl.municipalidad.bff.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioClient usuarioClient;

    public LoginResponseDTO login(LoginRequestDTO request) {
        return usuarioClient.login(request);
    }

    public UsuarioDTO registrar(RegisterRequestDTO request) {
        return usuarioClient.registrar(request);
    }
}
