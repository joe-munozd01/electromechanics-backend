package com.electromechanics.jornadas.controller;

import com.electromechanics.jornadas.entities.Jornada;
import com.electromechanics.jornadas.services.JornadaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class JornadaControllerTest {

    @Mock
    private JornadaService service;

    @InjectMocks 
    private JornadaController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void crear_conDatosValidos_responde201() throws Exception {
        Jornada creada = Jornada.builder().id("j-1").idEmpleado("emp-001")
                .tipoMarcacion("ENTRADA").estadoAprobacion("PENDIENTE").build();
        when(service.crear(any())).thenReturn(creada);

        mockMvc.perform(post("/api/jornadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {"idEmpleado": "emp-001", "tipoMarcacion": "Entrada",
                                 "fechaHoraMarcacion": "2026-11-14 08:30:15",
                                 "latitud":-33.51, "longitud":70.76}
                                 """))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value("j-1"))
                        .andExpect(jsonPath("$.estadoAprobacion").value("PENDIENTE"));
    }

    @Test
    void crear_conDatosInvalidos_responde400() throws Exception {
        mockMvc.perform(post("/api/jornadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idEmpleado":"","tipoMarcacion":"Otra",
                                "fechaHoraMarcacion":"2026-11-14 08:30:15",
                                "latitud":200,"longitud":-70.76}
                                """))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void listar_conFiltroDeEstado_responde200() throws Exception {
        when(service.listar(null, "PENDIENTE")).thenReturn(List.of(new Jornada()));

        mockMvc.perform(get("/api/jornadas").param("estado", "PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
