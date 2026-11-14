package com.electromechanics.jornadas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import com.electromechanics.jornadas.dto.JornadaRequest;
import com.electromechanics.jornadas.entities.Jornada;
import com.electromechanics.jornadas.repository.JornadaRepository;

@ExtendWith(MockitoExtension.class)
class JornadaServiceImplTest {

    @Mock 
    private JornadaRepository repository;

    @InjectMocks
    private JornadaServiceImpl service;

    @Test
    void crear_dejaLaJornadaPendienteYNormalizaElTipo() {
        when(repository.save(any(Jornada.class))).thenAnswer(inv -> inv.getArgument(0));
        JornadaRequest request = new JornadaRequest("emp-001", "Entrada", LocalDateTime.of(2026, 11, 14, 8, 30, 15), -33.51, -70.76, null);

        Jornada resultado = service.crear(request);

        assertNotNull(resultado.getId());
        assertEquals("emp-001", resultado.getIdEmpleado());
        assertEquals("ENTRADA", resultado.getTipoMarcacion());
        assertEquals("PENDIENTE", resultado.getEstadoAprobacion());
    }

    @Test
    void listar_conEmpleadoYEstado_usaLaConsultaCombinada() {
        when(repository.findByIdEmpleadoAndEstadoAprobacion(eq("emp-001"), eq("PENDIENTE"), any(Sort.class)))
                .thenReturn(List.of(new Jornada()));
        
        List<Jornada> resultado = service.listar("emp-001", "pendiente");

        assertEquals(1, resultado.size());
        verify(repository).findByIdEmpleadoAndEstadoAprobacion(eq("emp-001"), eq("PENDIENTE"), any(Sort.class));
    }

    @Test
    void listar_sinFiltros_devuelveTodas() {
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(new Jornada(), new Jornada()));

        assertEquals(2, service.listar(null, null).size());
    }

    @Test
    void obtener_siNoExiste_lanzaNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obtener("x"));

        assertEquals(404, ex.getStatusCode().value());
    }
    
    @Test
    void aprobar_cambiaElEstadoYRegistraAlSupervisor() {
        Jornada pendiente = Jornada.builder().id("j-1").estadoAprobacion("PENDIENTE").build();
        when(repository.findById("j-1")).thenReturn(Optional.of(pendiente));
        when(repository.save(any(Jornada.class))).thenAnswer(inv -> inv.getArgument(0));

        Jornada resultado = service.aprobar("j-1", "sup-001");

        assertEquals("APROBADA", resultado.getEstadoAprobacion());
        assertEquals("sup-001", resultado.getAprobadoPor());
        assertNotNull(resultado.getFechaAprobacion());
    }
}
