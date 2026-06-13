package cl.municipalidad.bff.dto;

import jakarta.validation.constraints.NotBlank;

public record AlertCreateRequestDto(
    @NotBlank
    String title,
    @NotBlank
    String description,
    SeverityDto severity
) {

}
