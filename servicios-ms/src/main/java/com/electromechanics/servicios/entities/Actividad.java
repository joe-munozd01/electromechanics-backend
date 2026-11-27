package com.electromechanics.servicios.entities;

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

import java.time.LocalDateTime;

@Entity
@Table(name = "actividades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {

    @Id
    private String id;

    @Column(nullable = false)
    private String idServicio;

    @Column(nullable = false, length = 1000)
    private String detalleTareaEjecutada;

    private String repuestosUtilizados;

    @Column(length = 1000)
    private String comentariosTecnicos;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime horaRegistro;

    @Column(columnDefinition = "TEXT")
    private String fotoBase64;
}