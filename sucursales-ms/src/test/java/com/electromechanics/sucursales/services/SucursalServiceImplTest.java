package com.electromechanics.sucursales.services;

import com.electromechanics.sucursales.dto.SucursalRequest;
import com.electromechanics.sucursales.entities.Sucursal;
import com.electromechanics.sucursales.repository.SucursalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalServiceImplTest {

    @Mock
    private SucursalRepository repository;

    @InjectMocks
    private SucursalServiceImpl service;

    private SucursalRequest request() {
        return new SucursalRequest("  Sucursal Norte ", "Av. Ficticia 100, Quilicura", -33.36, -70.74);
    }

    @Test
    void crear_guardaConIdYSinEspaciosSobrantes() {
        when(repository.save(any(Sucursal.class))).thenAnswer(inv -> inv.getArgument(0));

        Sucursal resultado = service.crear(request());

        assertNotNull(resultado.getId());
        assertEquals("Sucursal Norte", resultado.getNombreCliente());
        assertEquals("Av. Ficticia 100, Quilicura", resultado.getDireccionSucursal());
    }

    @Test
    void crear_conDireccionDuplicada_lanzaConflictoYNoGuarda() {
        when(repository.existsByDireccionSucursalIgnoreCase("Av. Ficticia 100, Quilicura")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.crear(request()));

        assertEquals(409, ex.getStatusCode().value());
        verify(repository, never()).save(any());
    }

    @Test
    void listar_devuelveTodasLasSucursales() {
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(new Sucursal(), new Sucursal()));

        assertEquals(2, service.listar().size());
    }

    @Test
    void obtener_siExiste_devuelveLaSucursal() {
        Sucursal sucursal = Sucursal.builder().id("suc-001").nombreCliente("Sucursal Norte").build();
        when(repository.findById("suc-001")).thenReturn(Optional.of(sucursal));

        assertEquals("Sucursal Norte", service.obtener("suc-001").getNombreCliente());
    }

    @Test
    void obtener_siNoExiste_lanzaNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obtener("x"));

        assertEquals(404, ex.getStatusCode().value());
    }
}