package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

public record AlertDTO(
    String id,
    String titulo,
    String descripcion,
    String severidad,
    LocalDateTime fecha
) {}
