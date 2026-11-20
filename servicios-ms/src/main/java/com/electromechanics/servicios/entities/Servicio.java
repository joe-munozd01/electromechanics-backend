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
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    private String id;

    // Puede ser null mientras el supervisor no asigna a un técnico
    private String idEmpleadoAsignado;

    @Column(nullable = false)
    private String idSucursal;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fechaProgramada;

    @Column(nullable = false, length = 500)
    private String descripcionTrabajo;

    // Pendiente, En Proceso o Finalizado
    @Column(nullable = false)
    private String estado;
}