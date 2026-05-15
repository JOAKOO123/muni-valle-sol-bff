package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertDTO>> listAlerts() {
        return ResponseEntity.ok(alertService.listAlerts());
    }

    @PostMapping
    public ResponseEntity<AlertDTO> create(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alertService.create(
                        body.get("titulo"),
                        body.get("descripcion"),
                        body.get("severidad")
                ));
    }
}
