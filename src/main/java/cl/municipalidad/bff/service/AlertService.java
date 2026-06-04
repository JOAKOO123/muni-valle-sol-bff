package cl.municipalidad.bff.service;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de alertas del BFF.
 * Genera alertas a partir de reportes activos y permite crear alertas manuales.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: expone interfaz simplificada al controller</li>
 *   <li>Single Responsibility: solo gestiona logica de alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AlertService {

    private final ReportService reportService;

    /**
     * Lista todas las alertas activas derivadas de reportes con estado ACTIVO.
     * Convierte cada reporte activo en una alerta con severidad calculada segun su tipo.
     *
     * @return lista de AlertDTO con las alertas activas
     */
    public List<AlertDTO> listAlerts() {
        return reportService.listAll()
                .stream()
                .filter(report -> "ACTIVO".equals(report.estado()))
                .map(this::toAlertDTO)
                .toList();
    }

    /**
     * Crea una nueva alerta manual con los datos proporcionados.
     *
     * @param title       titulo de la alerta
     * @param description descripcion de la alerta
     * @param severity    severidad de la alerta (ALTA, MEDIA, BAJA)
     * @return AlertDTO con la alerta creada
     */
    public AlertDTO create(String title, String description, String severity) {
        return new AlertDTO(
                UUID.randomUUID().toString(),
                title,
                description,
                severity,
                LocalDateTime.now()
        );
    }

    /**
     * Convierte un ReportDTO en un AlertDTO asignando severidad segun el tipo de reporte.
     * INCENDIO -> ALTA, HUMO -> MEDIA, SOSPECHOSO -> BAJA, otros -> MEDIA.
     *
     * @param report ReportDTO a convertir
     * @return AlertDTO con severidad calculada
     */
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
