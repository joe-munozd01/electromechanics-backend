package com.electromechanics.servicios.repository;

import com.electromechanics.servicios.entities.Actividad;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, String> {

    List<Actividad> findByIdServicio(String idServicio, Sort sort);
}