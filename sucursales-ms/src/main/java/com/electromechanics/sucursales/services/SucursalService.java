package com.electromechanics.sucursales.services;

import java.util.List;

import com.electromechanics.sucursales.dto.SucursalRequest;
import com.electromechanics.sucursales.entities.Sucursal;

public interface SucursalService {

    Sucursal crear(SucursalRequest request);

    List<Sucursal> listar();

    Sucursal obtener(String id);
}
