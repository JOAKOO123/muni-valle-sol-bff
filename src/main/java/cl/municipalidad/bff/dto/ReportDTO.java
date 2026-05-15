package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

public record ReportDTO(
    Long id,
    String titulo,
    String descripcion,
    String tipo,
    String estado,
    String emailUsuario,
    LocationDTO ubicacion,
    LocalDateTime fechaCreacion
) {}
