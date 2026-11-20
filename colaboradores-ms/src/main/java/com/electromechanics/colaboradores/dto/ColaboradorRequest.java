package com.electromechanics.colaboradores.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ColaboradorRequest(
    @NotBlank(message = "rut es obligatorio")
    @Pattern(regexp = "^\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dKK]$", message = "rut debe tener el formato correcto 11.111.111-1")
    String rut,

    @NotBlank(message = "nombreCompleto es obligatorio")
    @Size(min = 3, max = 100, message = "nombreCompleto debe tener entre 3 y 100 caracteres")
    String nombreCompleto,

    @NotBlank(message = "rol es obligatorio")
    @Pattern(regexp = "(?i)^(empleado|supervisor)$", message = "rol debe ser EMPLEADO o SUPERVISOR")
    String rol,

    String especialidadTecnica,

    @NotBlank(message = "correo es obligatorio")
    @Email(message = "correo no es valido")
    String correo
) {
}