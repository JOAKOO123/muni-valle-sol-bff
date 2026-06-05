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
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: delega toda la logica al AlertService</li>
 *   <li>Single Responsibility: solo gestiona endpoints de alertas</li>
 * </ul>
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
     * El body debe contener los campos "titulo", "descripcion" y "severidad".
     * Severidades validas: ALTA, MEDIA, BAJA.
     *
     * @param body mapa con los campos "titulo", "descripcion" y "severidad"
     * @return AlertDTO con la alerta creada, o 400 si faltan campos obligatorios
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        String titulo      = body.get("titulo");
        String descripcion = body.get("descripcion");
        String severidad   = body.get("severidad");

        if (titulo == null || titulo.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'titulo' es obligatorio"));
        }
        if (descripcion == null || descripcion.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'descripcion' es obligatorio"));
        }
        if (severidad == null || (!severidad.equals("ALTA") && !severidad.equals("MEDIA") && !severidad.equals("BAJA"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'severidad' debe ser ALTA, MEDIA o BAJA"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alertService.create(titulo, descripcion, severidad));
    }
}