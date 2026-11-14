package com.electromechanics.jornadas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record JornadaRequest (
    @NotBlank(message = "idEmpleado es obligatorio")
    String idEmpleado,

    @NotBlank(message = "tipoMarcacion es obligatorio")
    @Pattern(regexp = "(?i)^(entrada|salida)$", message = "tipoMarcacion debe ser Entrada o Salida")
    String tipoMarcacion,

    @NotNull(message = "FechaHoraMarcacion es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime fechaHoraMarcacion,

    @NotNull(message = "latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "latitud fuera de rango")
    @DecimalMax(value = "90.0", message = "latitud fuera de rango")
    Double latitud,

    @NotNull(message = "longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "longotud fuera de rango")
    @DecimalMax(value = "180.0", message = "longitud fuera de rango")
    Double longitud,

    String fotoRespaldoPath
) {
}
