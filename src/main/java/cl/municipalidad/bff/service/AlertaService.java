package cl.municipalidad.bff.service;

import cl.municipalidad.bff.dto.AlertaDTO;
import cl.municipalidad.bff.dto.ReporteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final ReporteService reporteService;

    public List<AlertaDTO> listarAlertas() {
        return reporteService.listarTodos()
                .stream()
                .filter(r -> "ACTIVO".equals(r.estado()))
                .map(this::toAlertaDTO)
                .toList();
    }

    public AlertaDTO crear(String titulo, String descripcion, String severidad) {
        return new AlertaDTO(
                UUID.randomUUID().toString(),
                titulo,
                descripcion,
                severidad,
                LocalDateTime.now()
        );
    }

    private AlertaDTO toAlertaDTO(ReporteDTO reporte) {
        String severidad = switch (reporte.tipo()) {
            case "INCENDIO"   -> "ALTA";
            case "HUMO"       -> "MEDIA";
            case "SOSPECHOSO" -> "BAJA";
            default           -> "MEDIA";
        };
        return new AlertaDTO(
                reporte.id().toString(),
                reporte.titulo(),
                reporte.descripcion(),
                severidad,
                reporte.fechaCreacion()
        );
    }
}
