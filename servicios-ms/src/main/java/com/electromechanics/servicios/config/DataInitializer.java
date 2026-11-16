package com.electromechanics.servicios.config;

import com.electromechanics.servicios.entities.Servicio;
import com.electromechanics.servicios.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ServicioRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                Servicio.builder().id("srv-001").idEmpleadoAsignado("emp-001").idSucursal("suc-001")
                        .fechaProgramada(LocalDateTime.of(2026, 11, 19, 9, 0))
                        .descripcionTrabajo("Mantención preventiva de tablero eléctrico").estado("Pendiente").build(),
                Servicio.builder().id("srv-002").idEmpleadoAsignado("emp-001").idSucursal("suc-002")
                        .fechaProgramada(LocalDateTime.of(2026, 11, 19, 11, 30))
                        .descripcionTrabajo("Revisión de motor trifásico").estado("En Proceso").build(),
                Servicio.builder().id("srv-003").idEmpleadoAsignado("emp-001").idSucursal("suc-003")
                        .fechaProgramada(LocalDateTime.of(2026, 11, 19, 15, 0))
                        .descripcionTrabajo("Cambio de rodamientos en bomba").estado("Finalizado").build()
        ));
    }
}