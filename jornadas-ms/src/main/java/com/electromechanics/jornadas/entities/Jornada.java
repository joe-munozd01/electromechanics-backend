package com.electromechanics.jornadas.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table (name = "jornadas")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Jornada {
    
    @Id 
    private String id;

    @Column(nullable = false) 
    private String idEmpleado;

    //ENTRADA O SALIDA
    @Column(nullable = false)
    private String tipoMarcacion;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaHoraMarcacion;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    private String fotoRespaldoPath;

    //PENDIENTE O APROBADA
    @Column(nullable = false)
    private String estadoAprobacion;

    private String aprobadoPor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaAprobacion;
}
