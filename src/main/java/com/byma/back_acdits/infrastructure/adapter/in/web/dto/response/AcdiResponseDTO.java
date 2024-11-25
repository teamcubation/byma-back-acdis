package com.byma.back_acdits.infrastructure.adapter.in.web.dto.response;

import com.byma.back_acdits.domain.model.EstadoAcdi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcdiResponseDTO {

    private Long idAcdi;
    private String idOrganizacion;
    private String denominacion;
    private Boolean liquidaEnByma;
    private Boolean habilitado;
    private Boolean billeteras;
    private String observaciones;
    private String fechaAlta;
    private String mail;
    private EstadoAcdi estado;

}
