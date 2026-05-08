package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.LoginRequestDTO;
import cl.municipalidad.bff.dto.LoginResponseDTO;
import cl.municipalidad.bff.dto.RegisterRequestDTO;
import cl.municipalidad.bff.dto.UsuarioDTO;
import cl.municipalidad.bff.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }
}
