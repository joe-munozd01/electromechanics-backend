package com.electromechanics.colaboradores.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import com.electromechanics.colaboradores.dto.ColaboradorRequest;
import com.electromechanics.colaboradores.entities.Colaborador;
import com.electromechanics.colaboradores.repository.ColaboradorRepository;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceImplTest {

    @Mock 
    private ColaboradorRepository repository;

    @InjectMocks 
    private ColaboradorServiceImpl service;

    private ColaboradorRequest request() {
        return new ColaboradorRequest("11.111.111-1", "Técnico Demo Uno", "empleado", "Electricidad", "Tecnico1@demo.cl");
    }

    @Test 
    void crear_normalizaRolYCorreoYQuedaActivo() {
        when(repository.save(any(Colaborador.class))).thenAnswer(inv -> inv.getArgument(0));

        Colaborador resultado = service.crear(request());

        assertNotNull(resultado.getId());
        assertEquals("EMPLEADO", resultado.getRol());
        assertEquals("tecnico1@demo.cl", resultado.getCorreo());
        assertTrue(resultado.getActivo());
    }

    @Test 
    void crear_conRutDuplicado_lanzaConflicoYNoGuarda() {
        when(repository.existsByRut("11.111.111-1")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.crear(request()));

        assertEquals(409, ex.getStatusCode().value());
        verify(repository, never()).save(any());
    }

    @Test 
    void crear_conCorreoDuplicado_lanzaConflicoYNoGuarda() {
        when(repository.existsByCorreo("tecnico1@demo.cl")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.crear(request()));

        assertEquals(409, ex.getStatusCode().value());
        verify(repository, never()).save(any());
    }

    @Test 
    void listar_conRol_filtraEnMayusculas() {
        when(repository.findByRol(eq("EMPLEADO"), any(Sort.class))).thenReturn(List.of(new Colaborador()));

        assertEquals(1, service.listar("empleado").size());
    }

    @Test 
    void listar_sinRol_devuelveTodos() {
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(new Colaborador(), new Colaborador()));

        assertEquals(2, service.listar(null).size());
    }

    @Test
    void obtener_siNoExiste_lanzaNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obtener("x"));

        assertEquals(404, ex.getStatusCode().value());
    }
}
