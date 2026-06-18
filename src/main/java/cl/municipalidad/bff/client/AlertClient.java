package cl.municipalidad.bff.client;

import cl.municipalidad.bff.dto.AlertMsResponseDTO;
import cl.municipalidad.bff.exception.MsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Cliente HTTP para el MS-Alertas.
 * Encapsula todas las llamadas al microservicio de alertas via WebClient.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Gateway Pattern: punto unico de acceso al MS-Alertas</li>
 *   <li>Single Responsibility: solo gestiona comunicacion con MS-Alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class AlertClient {

    @Qualifier("msAlertasClient")
    private final WebClient msAlertasClient;

    /**
     * Obtiene todas las alertas activas del MS-Alertas.
     *
     * @return lista de AlertMsResponseDTO con alertas en estado ACTIVE
     * @throws MsException si ocurre un error interno en el MS-Alertas (500)
     */
    public List<AlertMsResponseDTO> listActive() {
        return msAlertasClient.get()
                .uri("/api/alerts")
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Error al obtener alertas", HttpStatus.INTERNAL_SERVER_ERROR)))
                .bodyToFlux(AlertMsResponseDTO.class)
                .collectList()
                .block();
    }

    /**
     * Crea una nueva alerta en el MS-Alertas.
     *
     * @param title       titulo de la alerta
     * @param description descripcion de la alerta
     * @param severity    severidad: HIGH, MEDIUM o LOW
     * @return AlertMsResponseDTO con la alerta creada
     * @throws MsException si los datos son invalidos (400)
     */
    public AlertMsResponseDTO create(String title, String description, String severity) {
        Map<String, Object> body = Map.of(
                "title", title,
                "description", description,
                "severity", severity
        );
        return msAlertasClient.post()
                .uri("/api/alerts")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                    response -> response.bodyToMono(String.class)
                        .map(b -> new MsException("Error al crear alerta", HttpStatus.BAD_REQUEST)))
                .bodyToMono(AlertMsResponseDTO.class)
                .block();
    }
}