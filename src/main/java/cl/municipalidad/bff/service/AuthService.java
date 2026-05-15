package cl.municipalidad.bff.service;

import cl.municipalidad.bff.client.UserClient;
import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
import cl.municipalidad.bff.dto.TokenResponseDTO;
import cl.municipalidad.bff.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;

    public TokenResponseDTO login(LoginRequestDTO request) {
        return userClient.login(request);
    }

    public UserDTO register(RegisterRequestDTO request) {
        return userClient.register(request);
    }

    public UserDTO getUser(String email) {
        return userClient.findByEmail(email);
    }
}

