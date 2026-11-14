package com.electromechanics.jornadas.services;

import java.util.List;

import com.electromechanics.jornadas.dto.JornadaRequest;
import com.electromechanics.jornadas.entities.Jornada;

public interface JornadaService {

    Jornada crear(JornadaRequest request);

    List<Jornada> listar(String idEmpleado, String estado);

    Jornada obtener(String id);

    Jornada aprobar(String id, String idSupervisor);
}
