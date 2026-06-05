package cl.municipalidad.bff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejador global de excepciones del BFF.
 * Intercepta excepciones y retorna respuestas HTTP con formato estandarizado.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Chain of Responsibility: intercepta excepciones en cascada</li>
 *   <li>Single Responsibility: centraliza el manejo de errores</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de microservicios con status HTTP especifico.
     * Debe declararse ANTES del handler de RuntimeException porque MsException
     * extiende RuntimeException; Spring resuelve por tipo mas especifico primero,
     * pero declarar el mas especifico primero evita ambiguedad.
     *
     * @param ex MsException con mensaje y status HTTP del microservicio
     * @return ResponseEntity con el status y detalle del error del microservicio
     */
    @ExceptionHandler(MsException.class)
    public ResponseEntity<Map<String, Object>> handleMsException(MsException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
            Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", ex.getStatus().value()
            )
        );
    }

    /**
     * Maneja excepciones genericas de runtime no controladas.
     *
     * @param ex excepcion capturada
     * @return ResponseEntity con status 500 y detalle del error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 500
            )
        );
    }
}