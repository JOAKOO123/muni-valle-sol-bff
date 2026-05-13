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
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }       

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.findAll());
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
}
