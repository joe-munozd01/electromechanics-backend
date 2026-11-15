package com.electromechanics.colaboradores.services;

import java.util.List;

import com.electromechanics.colaboradores.dto.ColaboradorRequest;
import com.electromechanics.colaboradores.entities.Colaborador;

public interface ColaboradorService {
    
    Colaborador crear(ColaboradorRequest request);

    List<Colaborador> listar(String rol);

    Colaborador obtener(String id);
}
