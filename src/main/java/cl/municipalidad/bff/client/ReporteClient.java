package cl.municipalidad.bff.client;

import cl.municipalidad.bff.dto.ReporteMsDTO;
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
public class ReporteClient {

    @Qualifier("msReportesClient")
    private final WebClient msReportesClient;

    public List<ReporteMsDTO> listarTodos() {
        return msReportesClient.get()
                .uri("/api/reportes")
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Error al obtener reportes", HttpStatus.INTERNAL_SERVER_ERROR)))
                .bodyToFlux(ReporteMsDTO.class)
                .collectList()
                .block();
    }

    public List<ReporteMsDTO> listarActivos() {
        return msReportesClient.get()
                .uri("/api/reportes/activos")
                .retrieve()
                .bodyToFlux(ReporteMsDTO.class)
                .collectList()
                .block();
    }

    public ReporteMsDTO buscarPorId(Long id) {
        return msReportesClient.get()
                .uri("/api/reportes/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Reporte no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(ReporteMsDTO.class)
                .block();
    }

    public ReporteMsDTO crear(Map<String, Object> body) {
        return msReportesClient.post()
                .uri("/api/reportes")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                    response -> response.bodyToMono(String.class)
                        .map(b -> new MsException("Error al crear reporte", HttpStatus.BAD_REQUEST)))
                .bodyToMono(ReporteMsDTO.class)
                .block();
    }

    public ReporteMsDTO actualizarEstado(Long id, String estado) {
        return msReportesClient.put()
                .uri("/api/reportes/{id}/estado", id)
                .bodyValue(Map.of("estado", estado))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(String.class)
                        .map(body -> new MsException("Reporte no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(ReporteMsDTO.class)
                .block();
    }
}
