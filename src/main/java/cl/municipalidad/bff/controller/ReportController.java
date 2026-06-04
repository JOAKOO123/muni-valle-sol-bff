package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.ReportDTO;
import cl.municipalidad.bff.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador de reportes del BFF.
 * Expone los endpoints REST para la gestion de reportes de incendios.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: delega toda la logica al ReportService</li>
 *   <li>Single Responsibility: solo gestiona endpoints de reportes</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Lista todos los reportes del sistema.
     *
     * @return lista de ReportDTO con todos los reportes
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> listAll() {
        return ResponseEntity.ok(reportService.listAll());
    }

    /**
     * Lista solo los reportes con estado ACTIVO.
     *
     * @return lista de ReportDTO con reportes activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ReportDTO>> listActive() {
        return ResponseEntity.ok(reportService.listActive());
    }

    /**
     * Busca un reporte por su identificador.
     *
     * @param id identificador del reporte
     * @return ReportDTO con los datos del reporte
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    /**
     * Crea un nuevo reporte de incendio.
     *
     * @param body mapa con los datos del reporte a crear
     * @return ReportDTO con el reporte creado
     */
    @PostMapping
    public ResponseEntity<ReportDTO> create(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.create(body));
    }

    /**
     * Actualiza el estado de un reporte existente.
     *
     * @param id   identificador del reporte
     * @param body mapa con el campo "estado"
     * @return ReportDTO con el reporte actualizado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reportService.updateStatus(id, body.get("estado")));
    }

    /**
     * Actualiza el titulo de un reporte existente.
     *
     * @param id   identificador del reporte
     * @param body mapa con el campo "titulo"
     * @return ReportDTO con el reporte actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateTitle(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reportService.updateTitle(id, body.get("titulo")));
    }

    /**
     * Elimina un reporte por su identificador.
     *
     * @param id identificador del reporte a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}