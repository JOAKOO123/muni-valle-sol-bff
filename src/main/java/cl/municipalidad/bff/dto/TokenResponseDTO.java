package cl.municipalidad.bff.dto;

/**
 * DTO que encapsula el token JWT y el rol del usuario autenticado.
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public record TokenResponseDTO(
    /** Token JWT generado para la sesion. */
    String token,
    /** Rol del usuario asociado al token. */
    String rol
) {}
