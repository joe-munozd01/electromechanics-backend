package com.electromechanics.colaboradores.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.electromechanics.colaboradores.dto.ColaboradorRequest;
import com.electromechanics.colaboradores.entities.Colaborador;
import com.electromechanics.colaboradores.repository.ColaboradorRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ColaboradorServiceImpl implements ColaboradorService {

    private static final Sort ORDEN = Sort.by("NombreCompleto");

    private final ColaboradorRepository repository;

    @Override
    public Colaborador crear(ColaboradorRequest request) {
        String rut = request.rut().trim();
        String correo = request.correo().trim().toLowerCase();

        if (repository.existsByRut(rut)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un colaborador con ese RUT");
        }
        if (repository.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un colaborador con ese correo");
        }

        Colaborador colaborador = Colaborador.builder()
                .id(UUID.randomUUID().toString())
                .rut(rut)
                .nombreCompleto(request.nombreCompleto().trim())
                .rol(request.rol().trim().toUpperCase())
                .especialidadTecnica(request.especialidadTecnica())
                .correo(correo)
                .activo(true)
                .build();
            return repository.save(colaborador);
    }

    @Override 
    public List<Colaborador> listar(String rol) {
        if (rol == null || rol.isBlank()) {
            return repository.findAll(ORDEN);
        }
        return repository.findByRol(rol.trim().toUpperCase(), ORDEN);
    }

    @Override 
    public Colaborador obtener(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador no encontrado"));
    }
}
