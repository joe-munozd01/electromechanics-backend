package com.electromechanics.colaboradores.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "colaboradores")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Colaborador {

    @Id 
    private String id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String nombreCompleto;

    //EMPLEADO O SUPERVISOR
    @Column(nullable = false)
    private String rol;
    
    private String especialidadTecnica;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private Boolean activo;
}
