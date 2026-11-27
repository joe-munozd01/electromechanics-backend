package com.electromechanics.servicios.services;

import com.electromechanics.servicios.dto.ActividadRequest;
import com.electromechanics.servicios.dto.ServicioRequest;
import com.electromechanics.servicios.entities.Actividad;
import com.electromechanics.servicios.entities.Servicio;
import com.electromechanics.servicios.repository.ActividadRepository;
import com.electromechanics.servicios.repository.ServicioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioServiceImplTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ServicioServiceImpl service;

    @Test
    void crear_dejaElServicioPendiente() {
        when(servicioRepository.save(any(Servicio.class))).thenAnswer(inv -> inv.getArgument(0));
        ServicioRequest request = new ServicioRequest(null, "suc-001",
                LocalDateTime.of(2026, 11, 19, 9, 0), "Mantención preventiva de tablero");

        Servicio resultado = service.crear(request);

        assertNotNull(resultado.getId());
        assertEquals("Pendiente", resultado.getEstado());
        assertEquals("suc-001", resultado.getIdSucursal());
    }

    @Test
    void listar_conEmpleadoYEstado_normalizaElEstado() {
        when(servicioRepository.findByIdEmpleadoAsignadoAndEstado(eq("emp-001"), eq("En Proceso"), any(Sort.class)))
                .thenReturn(List.of(new Servicio()));

        assertEquals(1, service.listar("emp-001", "en proceso").size());
    }

    @Test
    void obtener_siNoExiste_lanzaNotFound() {
        when(servicioRepository.findById("x")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obtener("x"));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void cambiarEstado_conEstadoInvalido_lanzaBadRequestYNoGuarda() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.cambiarEstado("srv-1", "terminado"));

        assertEquals(400, ex.getStatusCode().value());
        verify(servicioRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_actualizaYGuarda() {
        Servicio servicio = Servicio.builder().id("srv-1").estado("Pendiente").build();
        when(servicioRepository.findById("srv-1")).thenReturn(Optional.of(servicio));
        when(servicioRepository.save(any(Servicio.class))).thenAnswer(inv -> inv.getArgument(0));

        Servicio resultado = service.cambiarEstado("srv-1", "en proceso");

        assertEquals("En Proceso", resultado.getEstado());
    }

    @Test
    void crearActividad_siElServicioNoExiste_lanzaNotFoundYNoGuarda() {
        when(servicioRepository.findById("x")).thenReturn(Optional.empty());
        ActividadRequest request = new ActividadRequest("Cambio de contactor principal", null, null, null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.crearActividad("x", request));

        assertEquals(404, ex.getStatusCode().value());
        verify(actividadRepository, never()).save(any());
    }

    @Test
    void crearActividad_guardaLaActividadConSuHora() {
        when(servicioRepository.findById("srv-1")).thenReturn(Optional.of(new Servicio()));
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(inv -> inv.getArgument(0));
        ActividadRequest request = new ActividadRequest("Cambio de contactor principal", "Contactor 25A", "Sin novedades", null);

        Actividad resultado = service.crearActividad("srv-1", request);

        assertEquals("srv-1", resultado.getIdServicio());
        assertNotNull(resultado.getHoraRegistro());
    }

    @Test 
    void crearActividad_guardaLaFotoDeEvidencia() {
        when(servicioRepository.findById("srv-1")).thenReturn(Optional.of(new Servicio()));
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(inv -> inv.getArgument(0));
        ActividadRequest request = new ActividadRequest("Cambio de contactor principal", null, null, "Zm90bw==");

        Actividad resultado = service.crearActividad("srv-1", request);

        assertEquals("Zm90bw==", resultado.getFotoBase64());
    }
}