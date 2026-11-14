package com.electromechanics.jornadas.repository;

import com.electromechanics.jornadas.entities.Jornada;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JornadaRepository extends JpaRepository<Jornada, String> {

    List<Jornada> findByIdEmpleado(String idEmpleado, Sort sort);

    List<Jornada> findByEstadoAprobacion(String estadoAprobacion, Sort sort);

    List<Jornada> findByIdEmpleadoAndEstadoAprobacion(String idEmpleado, String estadoAprobacion, Sort sort);
}
