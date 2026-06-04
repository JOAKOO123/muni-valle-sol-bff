package cl.municipalidad.bff.service;

import cl.municipalidad.bff.client.ReportClient;
import cl.municipalidad.bff.dto.ReportDTO;
import cl.municipalidad.bff.dto.ReportMsDTO;
import cl.municipalidad.bff.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio de reportes del BFF.
 * Orquesta las llamadas al MS-Reportes y transforma las respuestas
 * al formato requerido por el frontend.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: expone interfaz simplificada al controller</li>
 *   <li>DTO Pattern: transforma modelos internos a DTOs del frontend</li>
 *   <li>Single Responsibility: solo gestiona logica de reportes</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportClient reportClient;
    private final ReportMapper reportMapper;

    /**
     * Lista todos los reportes de incendios.
     *
     * @return lista de ReportDTO con todos los reportes
     */
    public List<ReportDTO> listAll() {
        return reportClient.listAll()
                .stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    /**
     * Lista solo los reportes con estado ACTIVO.
     *
     * @return lista de ReportDTO con reportes activos
     */
    public List<ReportDTO> listActive() {
        return reportClient.listActive()
                .stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    /**
     * Busca un reporte por su identificador unico.
     *
     * @param id identificador del reporte
     * @return ReportDTO con los datos del reporte
     */
    public ReportDTO findById(Long id) {
        return reportMapper.toDTO(reportClient.findById(id));
    }

    /**
     * Crea un nuevo reporte de incendio.
     *
     * @param body mapa con los datos del reporte a crear
     * @return ReportDTO con el reporte creado
     */
    public ReportDTO create(Map<String, Object> body) {
        return reportMapper.toDTO(reportClient.create(body));
    }

    /**
     * Actualiza el estado de un reporte existente.
     *
     * @param id     identificador del reporte
     * @param status nuevo estado del reporte
     * @return ReportDTO con el reporte actualizado
     */
    public ReportDTO updateStatus(Long id, String status) {
        return reportMapper.toDTO(reportClient.updateStatus(id, status));
    }

    /**
     * Actualiza el titulo de un reporte existente.
     *
     * @param id    identificador del reporte
     * @param title nuevo titulo del reporte
     * @return ReportDTO con el reporte actualizado
     */
    public ReportDTO updateTitle(Long id, String title) {
        return reportMapper.toDTO(reportClient.updateTitle(id, title));
    }

    /**
     * Elimina un reporte por su identificador.
     *
     * @param id identificador del reporte a eliminar
     */
    public void delete(Long id) {
        reportClient.delete(id);
    }
}