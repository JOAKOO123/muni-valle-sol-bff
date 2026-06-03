package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

/**
 * DTO que representa una alerta activa derivada de un reporte de incendio.
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public record AlertDTO(
    /** Identificador unico de la alerta. */
    String id,
    /** Titulo descriptivo de la alerta. */
    String titulo,
    /** Descripcion detallada de la situacion. */
    String descripcion,
    /** Nivel de severidad (ej: BAJA, MEDIA, ALTA, CRITICA). */
    String severidad,
    /** Fecha y hora de creacion de la alerta. */
    LocalDateTime fecha
) {}
