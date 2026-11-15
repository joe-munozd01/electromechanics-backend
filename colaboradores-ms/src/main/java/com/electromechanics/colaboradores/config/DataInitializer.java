package com.electromechanics.colaboradores.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.electromechanics.colaboradores.entities.Colaborador;
import com.electromechanics.colaboradores.repository.ColaboradorRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DataInitializer implements CommandLineRunner {
    
    private final ColaboradorRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                Colaborador.builder().id("emp-001").rut("11.111.111-1")
                        .nombreCompleto("Técnico Demo Uno").rol("EMPLEADO")
                        .especialidadTecnica("Electricidad").correo("tecnico1@demo.cl").activo(true).build(),
                Colaborador.builder().id("emp-002").rut("22.222.222-2")
                        .nombreCompleto("Técnico Demo Dos").rol("EMPLEADO")
                        .especialidadTecnica("Mecanica").correo("tecnico2@demo.cl").activo(true).build(),
                Colaborador.builder().id("sup-001").rut("33.333.333-3")
                        .nombreCompleto("Supervisor Demo").rol("SUPERVISOR")
                        .especialidadTecnica("Operaciones").correo("supervisor@demo.cl").activo(true).build()  
        ));
    }
}
