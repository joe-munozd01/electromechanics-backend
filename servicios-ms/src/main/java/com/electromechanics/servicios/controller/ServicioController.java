package com.electromechanics.servicios.controller;

import com.electromechanics.servicios.dto.ActividadRequest;
import com.electromechanics.servicios.dto.ServicioRequest;
import com.electromechanics.servicios.entities.Actividad;
import com.electromechanics.servicios.entities.Servicio;
import com.electromechanics.servicios.services.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService service;

    @PostMapping
    public ResponseEntity<Servicio> crear(@Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public List<Servicio> listar(@RequestParam(required = false) String idEmpleado,
                                 @RequestParam(required = false) String estado) {
        return service.listar(idEmpleado, estado);
    }

    @GetMapping("/{id}")
    public Servicio obtener(@PathVariable String id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}/asignar")
    public Servicio asignar(@PathVariable String id, @RequestParam String idEmpleado) {
        return service.asignar(id, idEmpleado);
    }

    @PutMapping("/{id}/estado")
    public Servicio cambiarEstado(@PathVariable String id, @RequestParam String estado) {
        return service.cambiarEstado(id, estado);
    }

    @GetMapping("/{id}/actividades")
    public List<Actividad> listarActividades(@PathVariable String id) {
        return service.listarActividades(id);
    }

    @PostMapping("/{id}/actividades")
    public ResponseEntity<Actividad> crearActividad(@PathVariable String id,
                                                    @Valid @RequestBody ActividadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearActividad(id, request));
    }
}