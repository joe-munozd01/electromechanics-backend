package com.electromechanics.servicios.services;

import com.electromechanics.servicios.dto.ActividadRequest;
import com.electromechanics.servicios.dto.ServicioRequest;
import com.electromechanics.servicios.entities.Actividad;
import com.electromechanics.servicios.entities.Servicio;
import com.electromechanics.servicios.repository.ActividadRepository;
import com.electromechanics.servicios.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private static final Sort ORDEN_SERVICIOS = Sort.by("fechaProgramada");
    private static final Sort ORDEN_ACTIVIDADES = Sort.by("horaRegistro");

    private final ServicioRepository servicioRepository;
    private final ActividadRepository actividadRepository;

    @Override
    public Servicio crear(ServicioRequest request) {
        Servicio servicio = Servicio.builder()
                .id(UUID.randomUUID().toString())
                .idEmpleadoAsignado(request.idEmpleadoAsignado())
                .idSucursal(request.idSucursal())
                .fechaProgramada(request.fechaProgramada())
                .descripcionTrabajo(request.descripcionTrabajo().trim())
                .estado("Pendiente")
                .build();
        return servicioRepository.save(servicio);
    }

    @Override
    public List<Servicio> listar(String idEmpleado, String estado) {
        boolean hayEmpleado = idEmpleado != null && !idEmpleado.isBlank();
        boolean hayEstado = estado != null && !estado.isBlank();
        String estadoNormalizado = hayEstado ? normalizarEstado(estado) : null;

        if (hayEmpleado && hayEstado) {
            return servicioRepository.findByIdEmpleadoAsignadoAndEstado(idEmpleado, estadoNormalizado, ORDEN_SERVICIOS);
        }
        if (hayEmpleado) {
            return servicioRepository.findByIdEmpleadoAsignado(idEmpleado, ORDEN_SERVICIOS);
        }
        if (hayEstado) {
            return servicioRepository.findByEstado(estadoNormalizado, ORDEN_SERVICIOS);
        }
        return servicioRepository.findAll(ORDEN_SERVICIOS);
    }

    @Override
    public Servicio obtener(String id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
    }

    @Override
    public Servicio asignar(String id, String idEmpleado) {
        if (idEmpleado == null || idEmpleado.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idEmpleado es obligatorio");
        }
        Servicio servicio = obtener(id);
        servicio.setIdEmpleadoAsignado(idEmpleado.trim());
        return servicioRepository.save(servicio);
    }

    @Override
    public Servicio cambiarEstado(String id, String estado) {
        String estadoNormalizado = normalizarEstado(estado);
        Servicio servicio = obtener(id);
        servicio.setEstado(estadoNormalizado);
        return servicioRepository.save(servicio);
    }

    @Override
    public List<Actividad> listarActividades(String idServicio) {
        obtener(idServicio);
        return actividadRepository.findByIdServicio(idServicio, ORDEN_ACTIVIDADES);
    }

    @Override
    public Actividad crearActividad(String idServicio, ActividadRequest request) {
        obtener(idServicio);
        Actividad actividad = Actividad.builder()
                .id(UUID.randomUUID().toString())
                .idServicio(idServicio)
                .detalleTareaEjecutada(request.detalleTareaEjecutada().trim())
                .repuestosUtilizados(request.repuestosUtilizados())
                .comentariosTecnicos(request.comentariosTecnicos())
                .fotoBase64(request.fotoBase64())
                .horaRegistro(LocalDateTime.now())
                .build();
        return actividadRepository.save(actividad);
    }

    private String normalizarEstado(String estado) {
        String valor = estado == null ? "" : estado.trim().toLowerCase();
        return switch (valor) {
            case "pendiente" -> "Pendiente";
            case "en proceso" -> "En Proceso";
            case "finalizado" -> "Finalizado";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "estado debe ser Pendiente, En Proceso o Finalizado");
        };
    }
}