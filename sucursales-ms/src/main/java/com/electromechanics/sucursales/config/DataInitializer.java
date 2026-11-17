package com.electromechanics.sucursales.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.electromechanics.sucursales.entities.Sucursal;
import com.electromechanics.sucursales.repository.SucursalRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DataInitializer implements CommandLineRunner {

    private final SucursalRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                Sucursal.builder().id("suc-001").nombreCliente("Sucursal Norte")
                        .direccionSucursal("Av. Ficticia 100, Quilicura")
                        .latitudRef(-33.3600).longitudRef(-70.7400).build(),
                Sucursal.builder().id("suc-002").nombreCliente("Sucursal Centro")
                        .direccionSucursal("Calle Ficticia 200, Santiago")
                        .latitudRef(-33.4489).longitudRef(-70.6693).build(),
                Sucursal.builder().id("suc-003").nombreCliente("Sucursal Sur")
                        .direccionSucursal("Pasaje Ficticia 300, San Bernardo")
                        .latitudRef(-33.5922).longitudRef(-70.6996).build()  
        ));
    }
}
