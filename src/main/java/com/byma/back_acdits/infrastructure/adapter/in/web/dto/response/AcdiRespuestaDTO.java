package com.byma.back_acdits.infrastructure.adapter.in.web.dto.response;

import com.byma.back_acdits.domain.model.EstadoAcdi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcdiRespuestaDTO {

    private Long idAcdi;
    private String idOrganizacionAcdi;
    private String denominacion;
    private Boolean liquidaEnByma;
    private Boolean habilitado;
    private Boolean billeteras;
    private String observaciones;
    private LocalDateTime fechaAlta;
    private String mail;
    private EstadoAcdi estado;

}
