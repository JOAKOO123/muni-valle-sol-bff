package cl.municipalidad.bff.dto;

public record LoginResponseDTO(
    Long id,
    String nombre,
    String email,
    String rol
) {}
