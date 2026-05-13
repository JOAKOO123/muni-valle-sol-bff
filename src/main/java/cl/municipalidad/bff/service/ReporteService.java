package cl.municipalidad.bff.service;

import java.util.List;

import cl.municipalidad.bff.dto.ReporteDTO;

public interface ReporteService {

        public List<ReporteDTO> findAll();
}
