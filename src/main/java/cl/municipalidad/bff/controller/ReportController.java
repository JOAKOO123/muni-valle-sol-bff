package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.ReportDTO;
import cl.municipalidad.bff.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> listAll() {
        return ResponseEntity.ok(reportService.listAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ReportDTO>> listActive() {
        return ResponseEntity.ok(reportService.listActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReportDTO> create(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.create(body));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reportService.updateStatus(id, body.get("estado")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateTitle(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reportService.updateTitle(id, body.get("titulo")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
