package com.electromechanics.servicios.services;

import com.electromechanics.servicios.dto.ActividadRequest;
import com.electromechanics.servicios.dto.ServicioRequest;
import com.electromechanics.servicios.entities.Actividad;
import com.electromechanics.servicios.entities.Servicio;

import java.util.List;

public interface ServicioService {

    Servicio crear(ServicioRequest request);

    List<Servicio> listar(String idEmpleado, String estado);

    Servicio obtener(String id);

    Servicio asignar(String id, String idEmpleado);

    Servicio cambiarEstado(String id, String estado);

    List<Actividad> listarActividades(String idServicio);

    Actividad crearActividad(String idServicio, ActividadRequest request);
}