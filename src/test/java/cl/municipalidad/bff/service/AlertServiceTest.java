package cl.municipalidad.bff.service;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.dto.LocationDTO;
import cl.municipalidad.bff.dto.ReportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertService - pruebas unitarias")
class AlertServiceTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private AlertService alertService;

    private LocationDTO location;

    @BeforeEach
    void setUp() {
        location = new LocationDTO(-33.4569, -70.6483);
    }

    @Test
    @DisplayName("listAlerts() debería retornar solo reportes con estado ACTIVO")
    void listAlerts_soloRetornaActivos() {
        ReportDTO activo = new ReportDTO(1L, "Incendio", "Desc", "INCENDIO", "ACTIVO",
                "user@gmail.com", location, LocalDateTime.now());
        ReportDTO enRevision = new ReportDTO(2L, "Humo", "Desc", "HUMO", "EN_REVISION",
                "user@gmail.com", location, LocalDateTime.now());
        ReportDTO pendiente = new ReportDTO(3L, "Sospechoso", "Desc", "SOSPECHOSO", "PENDIENTE",
                "user@gmail.com", location, LocalDateTime.now());

        when(reportService.listAll()).thenReturn(List.of(activo, enRevision, pendiente));

        List<AlertDTO> resultado = alertService.listAlerts();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).titulo()).isEqualTo("Incendio");
    }

    @Test
    @DisplayName("listAlerts() debería retornar lista vacía si no hay reportes activos")
    void listAlerts_retornaVacioSinActivos() {
        ReportDTO pendiente = new ReportDTO(1L, "Reporte", "Desc", "HUMO", "PENDIENTE",
                "user@gmail.com", location, LocalDateTime.now());

        when(reportService.listAll()).thenReturn(List.of(pendiente));

        assertThat(alertService.listAlerts()).isEmpty();
    }

    @Test
    @DisplayName("listAlerts() debería mapear tipo INCENDIO a severidad ALTA")
    void listAlerts_incendioMapeaAAlta() {
        ReportDTO reporte = new ReportDTO(1L, "Incendio", "Desc", "INCENDIO", "ACTIVO",
                "user@gmail.com", location, LocalDateTime.now());
        when(reportService.listAll()).thenReturn(List.of(reporte));

        assertThat(alertService.listAlerts().get(0).severidad()).isEqualTo("ALTA");
    }

    @Test
    @DisplayName("listAlerts() debería mapear tipo HUMO a severidad MEDIA")
    void listAlerts_humoMapeaAMedia() {
        ReportDTO reporte = new ReportDTO(1L, "Humo", "Desc", "HUMO", "ACTIVO",
                "user@gmail.com", location, LocalDateTime.now());
        when(reportService.listAll()).thenReturn(List.of(reporte));

        assertThat(alertService.listAlerts().get(0).severidad()).isEqualTo("MEDIA");
    }

    @Test
    @DisplayName("listAlerts() debería mapear tipo SOSPECHOSO a severidad BAJA")
    void listAlerts_sospechosoMapeaABaja() {
        ReportDTO reporte = new ReportDTO(1L, "Sospechoso", "Desc", "SOSPECHOSO", "ACTIVO",
                "user@gmail.com", location, LocalDateTime.now());
        when(reportService.listAll()).thenReturn(List.of(reporte));

        assertThat(alertService.listAlerts().get(0).severidad()).isEqualTo("BAJA");
    }

    @Test
    @DisplayName("listAlerts() debería mapear tipo desconocido a severidad MEDIA por defecto")
    void listAlerts_tipoDesconocidoMapeaAMedia() {
        ReportDTO reporte = new ReportDTO(1L, "Otro", "Desc", "OTRO", "ACTIVO",
                "user@gmail.com", location, LocalDateTime.now());
        when(reportService.listAll()).thenReturn(List.of(reporte));

        assertThat(alertService.listAlerts().get(0).severidad()).isEqualTo("MEDIA");
    }

    @Test
    @DisplayName("listAlerts() debería preservar título y descripción del reporte original")
    void listAlerts_preservaCamposDelReporte() {
        ReportDTO reporte = new ReportDTO(5L, "Título original", "Descripción original",
                "INCENDIO", "ACTIVO", "user@gmail.com", location, LocalDateTime.now());
        when(reportService.listAll()).thenReturn(List.of(reporte));

        AlertDTO alerta = alertService.listAlerts().get(0);
        assertThat(alerta.titulo()).isEqualTo("Título original");
        assertThat(alerta.descripcion()).isEqualTo("Descripción original");
        assertThat(alerta.id()).isEqualTo("5");
    }

    @Test
    @DisplayName("create() debería retornar AlertDTO con los datos entregados")
    void create_retornaAlertaConDatosCorrectos() {
        AlertDTO resultado = alertService.create("Nueva alerta", "Descripción", "ALTA");

        assertThat(resultado.titulo()).isEqualTo("Nueva alerta");
        assertThat(resultado.descripcion()).isEqualTo("Descripción");
        assertThat(resultado.severidad()).isEqualTo("ALTA");
    }

    @Test
    @DisplayName("create() debería generar un ID único (UUID) para cada alerta")
    void create_generaIdUnico() {
        AlertDTO alerta1 = alertService.create("Alerta 1", "Desc", "ALTA");
        AlertDTO alerta2 = alertService.create("Alerta 2", "Desc", "MEDIA");

        assertThat(alerta1.id()).isNotEqualTo(alerta2.id());
        assertThat(alerta1.id()).isNotBlank();
    }

    @Test
    @DisplayName("create() debería asignar una fecha de creación no nula")
    void create_asignaFechaNoNula() {
        assertThat(alertService.create("Alerta", "Desc", "BAJA").fecha()).isNotNull();
    }

    @Test
    @DisplayName("listAlerts() debería retornar lista vacía si no hay reportes")
    void listAlerts_retornaVacioSinReportes() {
        when(reportService.listAll()).thenReturn(List.of());
        assertThat(alertService.listAlerts()).isEmpty();
    }
}