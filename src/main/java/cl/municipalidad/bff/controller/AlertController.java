package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador de alertas del BFF.
 * Expone los endpoints REST para la gestion de alertas derivadas de reportes.
 *
 * Patrones aplicados:

 * 

 *   - Facade Pattern: delega toda la logica al AlertService

 *   - Single Responsibility: solo gestiona endpoints de alertas

 * 
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /**
     * Lista todas las alertas activas derivadas de reportes.
     *
     * @return lista de AlertDTO con las alertas activas
     */
    @GetMapping
    public ResponseEntity<List<AlertDTO>> listAlerts() {
        return ResponseEntity.ok(alertService.listAlerts());
    }

    /**
     * Crea una nueva alerta manual en el sistema.
     *
     * @param body mapa con los campos "titulo", "descripcion" y "severidad"
     * @return AlertDTO con la alerta creada
     */
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
