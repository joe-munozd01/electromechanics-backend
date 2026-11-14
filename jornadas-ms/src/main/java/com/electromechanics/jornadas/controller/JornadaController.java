package com.electromechanics.jornadas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electromechanics.jornadas.dto.JornadaRequest;
import com.electromechanics.jornadas.entities.Jornada;
import com.electromechanics.jornadas.services.JornadaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/jornadas")
@RequiredArgsConstructor 
public class JornadaController {

    private final JornadaService service;

    @PostMapping 
    public ResponseEntity<Jornada> crear(@Valid @RequestBody JornadaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping 
    public List<Jornada> listar(@RequestParam(required = false) String idEmpleado, @RequestParam(required = false) String estado) {
        return service.listar(idEmpleado, estado);
    }

    @GetMapping("/{id}")
    public Jornada obtener(@PathVariable String id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}/aprobar")
    public Jornada aprobar(@PathVariable String id, @RequestParam(required = false) String idSupervisor) {
        return service.aprobar(id, idSupervisor);
    }
}
