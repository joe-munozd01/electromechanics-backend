package com.electromechanics.sucursales.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.electromechanics.sucursales.dto.SucursalRequest;
import com.electromechanics.sucursales.entities.Sucursal;
import com.electromechanics.sucursales.repository.SucursalRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SucursalServiceImpl implements SucursalService {

    private static final Sort ORDEN = Sort.by("NombreCliente");

    private final SucursalRepository repository;

    @Override
    public Sucursal crear(SucursalRequest request) {
        String direccion = request.direccionSucursal().trim();

        if (repository.existsByDireccionSucursalIgnoreCase(direccion)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una sucursal con esa direccion");
        }

        Sucursal sucursal = Sucursal.builder()
                .id(UUID.randomUUID().toString())
                .nombreCliente(request.nombreCliente().trim())
                .direccionSucursal(direccion)
                .latitudRef(request.latitudRef())
                .longitudRef(request.longitudRef())
                .build();
        return repository.save(sucursal);
    }

    @Override 
    public List<Sucursal> listar() {
        return repository.findAll(ORDEN);
    }

    @Override
    public Sucursal obtener(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
    }
}
