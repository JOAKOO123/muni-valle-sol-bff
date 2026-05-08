package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

public record ReporteDTO(
    Long id,
    String titulo,
    String descripcion,
    String tipo,
    String estado,
    String emailUsuario,
    UbicacionDTO ubicacion,
    LocalDateTime fechaCreacion
) {}
