package com.electromechanics.sucursales.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SucursalRequest(

    @NotBlank(message = "nombreCliente es obligatorio")
    @Size(min = 3, max = 100, message = "nombreCliente debe tener entre 3 y 100 caracteres")
    String nombreCliente,

    @NotBlank(message = "direccionSucursal es obligatoria")
    @Size(min = 5, max = 200, message = "direccionSucursal debe tener entre 5 y 200 caracteres")
    String direccionSucursal,

    @NotNull(message = "latitudRef es obligatoria")
    @DecimalMin(value = "-90.0", message = "latitudRef fuera de rango")
    @DecimalMax(value = "90.0", message = "latitudRef fuera de rango")
    Double latitudRef,

    @NotNull(message = "longitudRef es obligatoria")
    @DecimalMin(value = "-180.0", message = "longitudRef fuera de rango")
    @DecimalMax(value = "180.0", message = "longitudRef fuera de rango")
    Double longitudRef
) {

}
