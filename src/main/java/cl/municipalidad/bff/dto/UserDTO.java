package cl.municipalidad.bff.dto;

public record UserDTO(
    Long id,
    String nombre,
    String email,
    String rol,
    Boolean activo
) {}
