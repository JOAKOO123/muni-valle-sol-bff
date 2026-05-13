package cl.municipalidad.bff.service;

import cl.municipalidad.bff.client.ReporteClient;
import cl.municipalidad.bff.dto.ReporteDTO;
import cl.municipalidad.bff.dto.ReporteMsDTO;
import cl.municipalidad.bff.dto.UbicacionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteClient reporteClient;

    @Override
    public List<ReporteDTO> findAll() {
        return reporteClient.listarTodos()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ReporteDTO> listarActivos() {
        return reporteClient.listarActivos()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReporteDTO buscarPorId(Long id) {
        return toDTO(reporteClient.buscarPorId(id));
    }

    public ReporteDTO crear(Map<String, Object> body) {
        return toDTO(reporteClient.crear(body));
    }

    public ReporteDTO actualizarEstado(Long id, String estado) {
        return toDTO(reporteClient.actualizarEstado(id, estado));
    }

    private ReporteDTO toDTO(ReporteMsDTO ms) {
        UbicacionDTO ubicacion = new UbicacionDTO(ms.latitud(), ms.longitud());
        return new ReporteDTO(
                ms.id(),
                ms.titulo(),
                ms.descripcion(),
                ms.tipo(),
                ms.estado(),
                ms.emailUsuario(),
                ubicacion,
                ms.fechaCreacion()
        );
    }
}
