package com.electromechanics.servicios.repository;

import com.electromechanics.servicios.entities.Servicio;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, String> {

    List<Servicio> findByIdEmpleadoAsignado(String idEmpleadoAsignado, Sort sort);

    List<Servicio> findByEstado(String estado, Sort sort);

    List<Servicio> findByIdEmpleadoAsignadoAndEstado(String idEmpleadoAsignado, String estado, Sort sort);
}