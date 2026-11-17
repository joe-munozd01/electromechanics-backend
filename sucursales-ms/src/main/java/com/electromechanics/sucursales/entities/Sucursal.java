package com.electromechanics.sucursales.entities;

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
@Table(name = "sucursales")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Sucursal {

    @Id 
    private String id;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String direccionSucursal;

    @Column(nullable = false)
    private Double latitudRef;

    @Column(nullable = false)
    private Double longitudRef;
}
