package com.byma.back_acdits.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Acdi {

    private Long idAcdi;
    private String idOrganizacionAcdi;
    private String denominacion;
    private Boolean liquidaEnByma = true;
    private Boolean habilitado;
    private Boolean billeteras;
    private String observaciones;
    private LocalDateTime fechaAlta;
    private String mail;
    private EstadoAcdi estado = EstadoAcdi.CREADA;

}
