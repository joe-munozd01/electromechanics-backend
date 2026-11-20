package com.electromechanics.jornadas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.electromechanics.jornadas.dto.JornadaRequest;
import com.electromechanics.jornadas.entities.Jornada;
import com.electromechanics.jornadas.repository.JornadaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class JornadaServiceImpl implements JornadaService {

    private static final Sort ORDEN = Sort.by(Sort.Direction.DESC, "fechaHoraMarcacion");

    private final JornadaRepository repository;

    @Override 
    public Jornada crear(JornadaRequest request) {
        Jornada jornada = Jornada.builder()
            .id(UUID.randomUUID().toString())
            .idEmpleado(request.idEmpleado())
            .tipoMarcacion(request.tipoMarcacion().toUpperCase())
            .fechaHoraMarcacion(request.fechaHoraMarcacion())
            .latitud(request.latitud())
            .longitud(request.longitud())
            .fotoRespaldoPath(request.fotoRespaldoPath())
            .estadoAprobacion("PENDIENTE")
            .build();
        return repository.save(jornada);
    }

    @Override 
    public List<Jornada> listar(String idEmpleado, String estado) {
        boolean hayEmpleado = idEmpleado != null && !idEmpleado.isBlank();
        boolean hayEstado = estado != null && !estado.isBlank();
        String estadoNormalizado = hayEstado ? estado.trim().toUpperCase() : null;

        if (hayEmpleado && hayEstado) {
            return repository.findByIdEmpleadoAndEstadoAprobacion(idEmpleado, estadoNormalizado, ORDEN);
        }

        if (hayEmpleado) {
            return repository.findByIdEmpleado(idEmpleado, ORDEN);
        }

        if (hayEstado) {
            return repository.findByEstadoAprobacion(estadoNormalizado, ORDEN);
        }

        return repository.findAll(ORDEN);
    }

    @Override 
    public Jornada obtener(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jornada no encontrada"));
    }

    @Override 
    public Jornada aprobar(String id, String idSupervisor) {
        Jornada jornada = obtener(id);
        jornada.setEstadoAprobacion("APROBADA");
        jornada.setAprobadoPor(idSupervisor);
        jornada.setFechaAprobacion(LocalDateTime.now());
        return repository.save(jornada);
    }
}
