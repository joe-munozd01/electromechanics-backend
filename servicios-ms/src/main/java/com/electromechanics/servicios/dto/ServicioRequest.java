package com.electromechanics.servicios.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ServicioRequest(
        String idEmpleadoAsignado,

        @NotBlank(message = "idSucursal es obligatorio")
        String idSucursal,

        @NotNull(message = "fechaProgramada es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime fechaProgramada,

        @NotBlank(message = "descripcionTrabajo es obligatoria")
        @Size(min = 5, max = 500, message = "descripcionTrabajo debe tener entre 5 y 500 caracteres")
        String descripcionTrabajo
) {
}