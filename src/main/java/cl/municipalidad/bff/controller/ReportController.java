package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.CreateReportRequest;
import cl.municipalidad.bff.dto.ReportDTO;
import cl.municipalidad.bff.dto.UpdateStatusRequest;
import cl.municipalidad.bff.dto.UpdateTitleRequest;
import cl.municipalidad.bff.service.ReportRequestHandler;
import cl.municipalidad.bff.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de reportes del BFF.
 * Expone endpoints REST para la gestión de reportes de incendios.
 * Delega transformación de records al handler y lógica de negocio al service.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: interfaz simplificada para cliente HTTP</li>
 *   <li>Delegation Pattern: delega transformación y lógica</li>
 *   <li>Single Responsibility: solo maneja endpoints HTTP</li>
 * </ul>
 *
 * <p>Flujo de responsabilidades:</p>
 * <pre>{@code
 * ReportController (HTTP)
 *   ↓
 * ReportRequestHandler (transforma records)
 *   ↓
 * ReportService (lógica de negocio + circuit breaker)
 *   ↓
 * ReportClient (llamadas a MS-Reportes)
 * }</pre>
 *
 * <p><b>Documentación OpenAPI:</b> Ver /swagger-ui.html para ejemplos interactivos
 * e integración con el openapi.yaml</p>
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
    private final ReportRequestHandler reportRequestHandler;

    /**
     * Lista todos los reportes del sistema sin filtros.
     *
     * @return lista de ReportDTO con todos los reportes
     *
     * <p><b>OpenAPI:</b> GET /reportes - operationId: listAllReports</p>
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> listAll() {
        return ResponseEntity.ok(reportService.listAll());
    }

    /**
     * Lista solo los reportes con estado ACTIVO.
     *
     * @return lista de ReportDTO con reportes activos
     *
     * <p><b>OpenAPI:</b> GET /reportes/activos - operationId: listActiveReports</p>
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ReportDTO>> listActive() {
        return ResponseEntity.ok(reportService.listActive());
    }

    /**
     * Busca un reporte por su identificador único.
     *
     * @param id identificador del reporte
     * @return ReportDTO con los datos del reporte
     *
     * <p><b>OpenAPI:</b> GET /reportes/{id} - operationId: getReportById</p>
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    /**
     * Crea un nuevo reporte de incendio.
     * Delega transformación del record al handler que convierte a Map.
     *
     * @param request record con datos del reporte
     * @return ReportDTO con el reporte creado
     *
     * <p>Ejemplo de request:</p>
     * <pre>{@code
     * POST /api/reportes
     * Content-Type: application/json
     *
     * {
     *   "titulo": "Incendio en Cerro",
     *   "descripcion": "Fuego activo en ladera norte",
     *   "tipo": "INCENDIO",
     *   "emailUsuario": "juan@example.com",
     *   "latitud": -33.8688,
     *   "longitud": -71.5203
     * }
     * }</pre>
     *
     * <p><b>OpenAPI:</b> POST /reportes - operationId: createReport</p>
     */
    @PostMapping
    public ResponseEntity<ReportDTO> create(@RequestBody CreateReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportRequestHandler.handleCreate(request));
    }

    /**
     * Actualiza el estado de un reporte existente.
     * Estados válidos: ACTIVO, RESUELTO, CERRADO.
     *
     * @param id identificador del reporte
     * @param request record con el nuevo estado
     * @return ReportDTO con el reporte actualizado
     *
     * <p>Ejemplo de request:</p>
     * <pre>{@code
     * PUT /api/reportes/123/estado
     * Content-Type: application/json
     *
     * {
     *   "estado": "RESUELTO"
     * }
     * }</pre>
     *
     * <p><b>OpenAPI:</b> PUT /reportes/{id}/estado - operationId: updateReportStatus</p>
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(reportRequestHandler.handleUpdateStatus(id, request));
    }

    /**
     * Actualiza el título de un reporte existente.
     *
     * @param id identificador del reporte
     * @param request record con el nuevo título
     * @return ReportDTO con el reporte actualizado
     *
     * <p>Ejemplo de request:</p>
     * <pre>{@code
     * PUT /api/reportes/123
     * Content-Type: application/json
     *
     * {
     *   "titulo": "Incendio Zona Crítica - ACTUALIZADO"
     * }
     * }</pre>
     *
     * <p><b>OpenAPI:</b> PUT /reportes/{id} - operationId: updateReportTitle</p>
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateTitle(
            @PathVariable Long id,
            @RequestBody UpdateTitleRequest request) {
        return ResponseEntity.ok(reportRequestHandler.handleUpdateTitle(id, request));
    }

    /**
     * Elimina un reporte por su identificador.
     *
     * @param id identificador del reporte a eliminar
     * @return respuesta sin contenido (204 No Content)
     *
     * <p><b>OpenAPI:</b> DELETE /reportes/{id} - operationId: deleteReport</p>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}