package com.byma.back_acdits.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrearAcdiSolicitud {

    @NotBlank(message = "El ID de la organizacion es obligatorio")
    private String idOrganizacion;

    @NotBlank(message = "La denominacion es obligatoria")
    private String denominacion;

    @NotNull(message = "El campo liquida en Byma es obligatorio")
    private Boolean liquidaEnByma;

    @NotNull(message = "El campo habilitado es obligatorio")
    private Boolean habilitado;

    private Boolean billeteras;

    private String observaciones;

    @Email
    @NotBlank(message = "El correo es obligatorio")
    private String mail;

}
