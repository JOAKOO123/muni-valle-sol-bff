package cl.municipalidad.bff.dto;

public record UsuarioDTO(
    Long id,
    String nombre,
    String email,
    String rol,
    Boolean activo
) {}
