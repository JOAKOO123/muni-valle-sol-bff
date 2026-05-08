package cl.municipalidad.bff.dto;

public record LoginRequestDTO(
    String email,
    String password
) {}
