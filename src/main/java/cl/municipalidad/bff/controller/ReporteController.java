package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.ReporteDTO;
import cl.municipalidad.bff.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ReporteDTO>> listarActivos() {
        return ResponseEntity.ok(reporteService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crear(body));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReporteDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, body.get("estado")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO> actualizarTitulo(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reporteService.actualizarTitulo(id, body.get("titulo")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
