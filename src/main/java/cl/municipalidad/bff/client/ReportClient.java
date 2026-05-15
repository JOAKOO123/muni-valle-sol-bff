package cl.municipalidad.bff.client;

import cl.municipalidad.bff.dto.ReportMsDTO;
import cl.municipalidad.bff.exception.MsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportClient {

    @Qualifier("msReportesClient")
    private final WebClient msReportesClient;

    public List<ReportMsDTO> listAll() {
        return msReportesClient.get()
                .uri("/api/reportes")
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Error al obtener reportes", HttpStatus.INTERNAL_SERVER_ERROR)))
                .bodyToFlux(ReportMsDTO.class)
                .collectList()
                .block();
    }

    public List<ReportMsDTO> listActive() {
        return msReportesClient.get()
                .uri("/api/reportes/activos")
                .retrieve()
                .bodyToFlux(ReportMsDTO.class)
                .collectList()
                .block();
    }

    public ReportMsDTO findById(Long id) {
        return msReportesClient.get()
                .uri("/api/reportes/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Reporte no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(ReportMsDTO.class)
                .block();
    }

    public ReportMsDTO create(Map<String, Object> body) {
        return msReportesClient.post()
                .uri("/api/reportes")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                    response -> response.bodyToMono(String.class)
                        .map(b -> new MsException("Error al crear reporte", HttpStatus.BAD_REQUEST)))
                .bodyToMono(ReportMsDTO.class)
                .block();
    }

    public ReportMsDTO updateStatus(Long id, String status) {
        return msReportesClient.put()
                .uri("/api/reportes/{id}/estado", id)
                .bodyValue(Map.of("estado", status))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Reporte no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(ReportMsDTO.class)
                .block();
    }

    public ReportMsDTO updateTitle(Long id, String title) {
        return msReportesClient.put()
                .uri("/api/reportes/{id}", id)
                .bodyValue(Map.of("titulo", title))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Reporte no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(ReportMsDTO.class)
                .block();
    }

    public void delete(Long id) {
        msReportesClient.delete()
                .uri("/api/reportes/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Reporte no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(Void.class)
                .block();
    }
}
