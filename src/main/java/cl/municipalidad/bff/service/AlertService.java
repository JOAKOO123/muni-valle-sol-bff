package cl.municipalidad.bff.service;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final ReportService reportService;

    public List<AlertDTO> listAlerts() {
        return reportService.listAll()
                .stream()
                .filter(report -> "ACTIVO".equals(report.estado()))
                .map(this::toAlertDTO)
                .toList();
    }

    public AlertDTO create(String title, String description, String severity) {
        return new AlertDTO(
                UUID.randomUUID().toString(),
                title,
                description,
                severity,
                LocalDateTime.now()
        );
    }

    private AlertDTO toAlertDTO(ReportDTO report) {
        String severity = switch (report.tipo()) {
            case "INCENDIO"   -> "ALTA";
            case "HUMO"       -> "MEDIA";
            case "SOSPECHOSO" -> "BAJA";
            default           -> "MEDIA";
        };
        return new AlertDTO(
                report.id().toString(),
                report.titulo(),
                report.descripcion(),
                severity,
                report.fechaCreacion()
        );
    }
}
