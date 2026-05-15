package cl.municipalidad.bff.service;

import cl.municipalidad.bff.client.ReportClient;
import cl.municipalidad.bff.dto.LocationDTO;
import cl.municipalidad.bff.dto.ReportDTO;
import cl.municipalidad.bff.dto.ReportMsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportClient reportClient;

    public List<ReportDTO> listAll() {
        return reportClient.listAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ReportDTO> listActive() {
        return reportClient.listActive()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReportDTO findById(Long id) {
        return toDTO(reportClient.findById(id));
    }

    public ReportDTO create(Map<String, Object> body) {
        return toDTO(reportClient.create(body));
    }

    public ReportDTO updateStatus(Long id, String status) {
        return toDTO(reportClient.updateStatus(id, status));
    }

    public ReportDTO updateTitle(Long id, String title) {
        return toDTO(reportClient.updateTitle(id, title));
    }

    public void delete(Long id) {
        reportClient.delete(id);
    }

    private ReportDTO toDTO(ReportMsDTO ms) {
        LocationDTO location = new LocationDTO(ms.latitud(), ms.longitud());
        return new ReportDTO(
                ms.id(),
                ms.titulo(),
                ms.descripcion(),
                ms.tipo(),
                ms.estado(),
                ms.emailUsuario(),
                location,
                ms.fechaCreacion()
        );
    }
}
