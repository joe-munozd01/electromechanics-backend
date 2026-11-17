package com.electromechanics.sucursales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electromechanics.sucursales.entities.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, String> {

    boolean existsByDireccionSucursalIgnoreCase(String direccionSucursal);
}
