package cl.municipalidad.bff.dto;

public record RegisterRequestDTO(
    String nombre,
    String email,
    String password,
    String rol
) {}
