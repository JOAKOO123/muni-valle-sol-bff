package cl.municipalidad.bff.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class MsException extends RuntimeException {

    private final HttpStatus status;

    public MsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
