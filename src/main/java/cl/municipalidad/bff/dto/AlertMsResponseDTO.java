package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

/**
 * DTO que mapea la respuesta del MS-Alertas.
 * Los campos siguen la nomenclatura en inglés del microservicio (AlertResponseDTO del ms).
 *
 * @param id          Identificador unico generado por MongoDB.
 * @param title       Titulo descriptivo de la alerta.
 * @param description Descripcion detallada del evento.
 * @param severity    Nivel de severidad: HIGH, MEDIUM o LOW.
 * @param status      Estado actual: ACTIVE o RESOLVED.
 * @param date        Fecha y hora de creacion.
 * @param reportId    Id del reporte asociado. Puede ser null.
 * @param userId      Id del usuario relacionado. Puede ser null.
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public record AlertMsResponseDTO(
    String id,
    String title,
    String description,
    String severity,
    String status,
    LocalDateTime date,
    Long reportId,
    Long userId
) {}