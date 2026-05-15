package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

public record AlertaDTO(
    String id,
    String titulo,
    String descripcion,
    String severidad,
    LocalDateTime fecha
) {}
