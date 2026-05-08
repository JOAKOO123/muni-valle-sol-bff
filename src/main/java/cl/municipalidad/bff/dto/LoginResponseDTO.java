package cl.municipalidad.bff.dto;

public record LoginResponseDTO(
    String token,
    String rol
) {}
