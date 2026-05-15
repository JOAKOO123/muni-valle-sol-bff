package cl.municipalidad.bff.dto;

import java.time.LocalDateTime;

public record ReportMsDTO(
    Long id,
    String titulo,
    String descripcion,
    Double latitud,
    Double longitud,
    String tipo,
    String estado,
    String emailUsuario,
    LocalDateTime fechaCreacion
) {}
