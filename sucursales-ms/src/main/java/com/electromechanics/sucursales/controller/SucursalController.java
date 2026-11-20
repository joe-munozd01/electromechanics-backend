package com.electromechanics.sucursales.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.electromechanics.sucursales.dto.SucursalRequest;
import com.electromechanics.sucursales.entities.Sucursal;
import com.electromechanics.sucursales.services.SucursalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor 
public class SucursalController {

    private final SucursalService service;

    @GetMapping 
    public List<Sucursal> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Sucursal obtener(@PathVariable String id) {
        return service.obtener(id);
    }

    @PostMapping 
    public ResponseEntity<Sucursal> crear(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
}
