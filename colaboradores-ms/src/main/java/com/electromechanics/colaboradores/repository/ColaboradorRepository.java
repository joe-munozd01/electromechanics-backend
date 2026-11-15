package com.electromechanics.colaboradores.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.electromechanics.colaboradores.entities.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, String> {
    
    List<Colaborador> findByRol(String rol, Sort sort);

    boolean existsByRut(String rut);

    boolean existsByCorreo(String correo);
}
