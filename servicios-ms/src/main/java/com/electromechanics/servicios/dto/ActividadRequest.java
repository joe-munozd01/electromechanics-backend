package com.electromechanics.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActividadRequest(
        @NotBlank(message = "detalleTareaEjecutada es obligatorio")
        @Size(min = 10, max = 1000, message = "detalleTareaEjecutada debe tener entre 10 y 1000 caracteres")
        String detalleTareaEjecutada,

        String repuestosUtilizados,

        String comentariosTecnicos,

        String fotoBase64
) {
}