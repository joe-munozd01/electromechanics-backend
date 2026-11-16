package com.electromechanics.servicios.controller;

import com.electromechanics.servicios.entities.Actividad;
import com.electromechanics.servicios.entities.Servicio;
import com.electromechanics.servicios.services.ServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ServicioControllerTest {

    @Mock
    private ServicioService service;

    @InjectMocks
    private ServicioController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void crear_conDatosValidos_responde201() throws Exception {
        Servicio creado = Servicio.builder().id("srv-1").estado("Pendiente").build();
        when(service.crear(any())).thenReturn(creado);

        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idSucursal":"suc-001","fechaProgramada":"2026-11-19 09:00",
                                 "descripcionTrabajo":"Mantención preventiva de tablero"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("srv-1"))
                .andExpect(jsonPath("$.estado").value("Pendiente"));
    }

    @Test
    void crear_conDatosInvalidos_responde400() throws Exception {
        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idSucursal":"","descripcionTrabajo":"a"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_conEmpleado_responde200() throws Exception {
        when(service.listar("emp-001", null)).thenReturn(List.of(new Servicio(), new Servicio()));

        mockMvc.perform(get("/api/servicios").param("idEmpleado", "emp-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtener_siNoExiste_responde404() throws Exception {
        when(service.obtener("x")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/servicios/x"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearActividad_conDatosValidos_responde201() throws Exception {
        Actividad creada = Actividad.builder().id("a-1").idServicio("srv-001").build();
        when(service.crearActividad(eq("srv-001"), any())).thenReturn(creada);

        mockMvc.perform(post("/api/servicios/srv-001/actividades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"detalleTareaEjecutada":"Cambio de contactor principal",
                                 "repuestosUtilizados":"Contactor 25A"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("a-1"));
    }
}