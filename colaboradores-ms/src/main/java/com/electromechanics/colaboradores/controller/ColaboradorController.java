package com.electromechanics.colaboradores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electromechanics.colaboradores.dto.ColaboradorRequest;
import com.electromechanics.colaboradores.entities.Colaborador;
import com.electromechanics.colaboradores.services.ColaboradorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor 
public class ColaboradorController {

    private final ColaboradorService service;

    @GetMapping 
    public List<Colaborador> listar(@RequestParam(required = false) String rol) {
        return service.listar(rol);
    }

    @GetMapping("/{id}")
    public Colaborador obtener(@PathVariable String id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Colaborador> crear(@Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
}
