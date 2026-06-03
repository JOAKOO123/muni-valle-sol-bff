package cl.municipalidad.bff.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepcion personalizada para errores provenientes de los microservicios.
 * Encapsula el mensaje de error y el status HTTP correspondiente.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Exception Shielding: oculta detalles internos del microservicio</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 */
@Getter
public class MsException extends RuntimeException {

    private final HttpStatus status;

    public MsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
