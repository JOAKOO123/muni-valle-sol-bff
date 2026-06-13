package cl.municipalidad.bff.service;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.dto.CreateAlertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Handler que procesa y valida las solicitudes de creación de alertas.
 * Encapsula la lógica de validación y transformación de records antes de
 * delegar al servicio de negocio.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Handler Pattern: procesa y valida requests antes de persistir</li>
 *   <li>Single Responsibility: solo valida y transforma CreateAlertRequest</li>
 *   <li>Separation of Concerns: validación separada de lógica de negocio</li>
 * </ul>
 *
 * <p>Flujo de ejecución:</p>
 * <pre>{@code
 * Controller recibe CreateAlertRequest
 *     ↓
 * AlertRequestHandler.handleCreate()
 *     ↓
 * validateRequest() - valida campos obligatorios y valores
 *     ↓
 * AlertService.create() - crea la alerta
 *     ↓
 * Response devuelve AlertDTO
 * }</pre>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AlertRequestHandler {

    private final AlertService alertService;

    /**
     * Procesa la creación de una alerta validando el request.
     * Si la validación falla lanza IllegalArgumentException que es capturada
     * por GlobalExceptionHandler y retorna 400.
     *
     * @param request record con los datos de la alerta
     * @return AlertDTO con la alerta creada
     * @throws IllegalArgumentException si algún campo es inválido
     */
    public AlertDTO handleCreate(CreateAlertRequest request) {
        validateRequest(request);
        return alertService.create(request.titulo(), request.descripcion(), request.severidad());
    }

    /**
     * Valida todos los campos obligatorios del request de creación de alerta.
     * Validaciones:
     * <ul>
     *   <li>titulo: no puede ser null ni blank</li>
     *   <li>descripcion: no puede ser null ni blank</li>
     *   <li>severidad: debe ser exactamente ALTA, MEDIA o BAJA</li>
     * </ul>
     *
     * @param request record a validar
     * @throws IllegalArgumentException si alguna validación falla
     */
    private void validateRequest(CreateAlertRequest request) {
        if (request.titulo() == null || request.titulo().isBlank()) {
            throw new IllegalArgumentException("El campo 'titulo' es obligatorio");
        }
        if (request.descripcion() == null || request.descripcion().isBlank()) {
            throw new IllegalArgumentException("El campo 'descripcion' es obligatorio");
        }
        if (request.severidad() == null || 
            (!request.severidad().equals("ALTA") && 
             !request.severidad().equals("MEDIA") && 
             !request.severidad().equals("BAJA"))) {
            throw new IllegalArgumentException("El campo 'severidad' debe ser ALTA, MEDIA o BAJA");
        }
    }
}