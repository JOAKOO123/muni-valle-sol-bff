package cl.municipalidad.bff.controller;

import cl.municipalidad.bff.dto.AlertDTO;
import cl.municipalidad.bff.service.AlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertController - pruebas de integración web")
class AlertControllerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertController alertController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AlertDTO mockAlerta = new AlertDTO(
            "uuid-123", "Incendio Norte", "Fuego activo", "ALTA", LocalDateTime.now());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(alertController).build();
    }

    @Test
    @DisplayName("GET /api/alertas debería retornar 200 y la lista de alertas")
    void listAlerts_retorna200ConAlertas() throws Exception {
        when(alertService.listAlerts()).thenReturn(List.of(mockAlerta));

        mockMvc.perform(get("/api/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Incendio Norte"))
                .andExpect(jsonPath("$[0].severidad").value("ALTA"))
                .andExpect(jsonPath("$[0].id").value("uuid-123"));
    }

    @Test
    @DisplayName("GET /api/alertas debería retornar 200 con lista vacía si no hay alertas")
    void listAlerts_retorna200ListaVacia() throws Exception {
        when(alertService.listAlerts()).thenReturn(List.of());

        mockMvc.perform(get("/api/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/alertas debería llamar al service exactamente una vez")
    void listAlerts_llamaServiceUnaVez() throws Exception {
        when(alertService.listAlerts()).thenReturn(List.of());

        mockMvc.perform(get("/api/alertas")).andExpect(status().isOk());

        verify(alertService, times(1)).listAlerts();
    }

    @Test
    @DisplayName("POST /api/alertas debería retornar 201 y la alerta creada")
    void create_retorna201ConAlertaCreada() throws Exception {
        Map<String, String> body = Map.of(
                "titulo", "Nueva alerta",
                "descripcion", "Descripción",
                "severidad", "ALTA");

        when(alertService.create("Nueva alerta", "Descripción", "ALTA")).thenReturn(mockAlerta);

        mockMvc.perform(post("/api/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Incendio Norte"))
                .andExpect(jsonPath("$.severidad").value("ALTA"));
    }

    @Test
    @DisplayName("POST /api/alertas debería llamar al service con los parámetros correctos")
    void create_llamaServiceConParametrosCorrectos() throws Exception {
        Map<String, String> body = Map.of(
                "titulo", "Alerta test",
                "descripcion", "Desc test",
                "severidad", "MEDIA");

        when(alertService.create(any(), any(), any())).thenReturn(mockAlerta);

        mockMvc.perform(post("/api/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        verify(alertService).create("Alerta test", "Desc test", "MEDIA");
    }

    @Test
    @DisplayName("POST /api/alertas debería retornar 400 si falta el titulo")
    void create_retorna400SiFaltaTitulo() throws Exception {
        Map<String, String> body = Map.of(
                "descripcion", "Desc",
                "severidad", "ALTA");

        mockMvc.perform(post("/api/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/alertas debería retornar 400 si la severidad es inválida")
    void create_retorna400SiSeveridadInvalida() throws Exception {
        Map<String, String> body = Map.of(
                "titulo", "Test",
                "descripcion", "Desc",
                "severidad", "CRITICA");

        mockMvc.perform(post("/api/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}