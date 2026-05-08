package cl.municipalidad.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @GetMapping
    public ResponseEntity<List<Object>> listarAlertas() {
        return ResponseEntity.ok(List.of());
    }
}
