package com.electromechanics.sucursales.controller;

import com.electromechanics.sucursales.entities.Sucursal;
import com.electromechanics.sucursales.services.SucursalService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SucursalControllerTest {

    @Mock 
    private SucursalService service;

    @InjectMocks 
    private SucursalController controller;

    private MockMvc mockMvc;

    @BeforeEach 
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test 
    void crear_conDatosValidos_responde201() throws Exception {
        Sucursal creada = Sucursal.builder().id("suc-9").nombreCliente("Sucursal Norte").build();
        when(service.crear(any())).thenReturn(creada);

        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreCliente":"Sucursal Norte",
                                 "direccionSucursal":"Av. Ficticia 100, Quilicura",
                                 "latitudRef":-33.36,"longitudRef":-70.74}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("suc-9"));
    }

    @Test
    void crear_conDatosInvalidos_responde400() throws Exception {
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreCliente":"","direccionSucursal":"x",
                                "latitudRef":200,"longitudRef":-70.74}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_responde200ConLaLista() throws Exception {
        when(service.listar()).thenReturn(List.of(new Sucursal(), new Sucursal()));

        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtener_siNoExiste_responde404() throws Exception {
        when(service.obtener("x")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/sucursales/x"))
                .andExpect(status().isNotFound());
    }
}
