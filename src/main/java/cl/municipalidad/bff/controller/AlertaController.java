package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.AlertaDTO;
import cl.municipalidad.bff.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    public ResponseEntity<List<AlertaDTO>> listarAlertas() {
        return ResponseEntity.ok(alertaService.listarAlertas());
    }

    @PostMapping
    public ResponseEntity<AlertaDTO> crear(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alertaService.crear(
                        body.get("titulo"),
                        body.get("descripcion"),
                        body.get("severidad")
                ));
    }
}
