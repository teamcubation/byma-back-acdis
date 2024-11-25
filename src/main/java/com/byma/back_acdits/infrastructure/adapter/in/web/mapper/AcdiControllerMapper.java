package com.byma.back_acdits.infrastructure.adapter.in.web.mapper;

import com.byma.back_acdits.application.util.Validador;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.response.AcdiResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AcdiControllerMapper {

    public static Acdi mapRequestToModel(CrearAcdiRequest crearAcdiRequest) {
        Validador.validarNoNulo(crearAcdiRequest);
        return Acdi.builder()
                .idOrganizacionAcdi(crearAcdiRequest.getIdOrganizacion())
                .denominacion(crearAcdiRequest.getDenominacion())
                .liquidaEnByma(crearAcdiRequest.getLiquidaEnByma())
                .habilitado(crearAcdiRequest.getHabilitado())
                .billeteras(crearAcdiRequest.getBilleteras())
                .observaciones(crearAcdiRequest.getObservaciones())
                .fechaAlta(LocalDateTime.now())
                .mail(crearAcdiRequest.getMail())
                .estado(EstadoAcdi.CREADA)
                .build();
    }

    public static AcdiResponseDTO mapModelToResponseDTO(Acdi acdi) {
        Validador.validarNoNulo(acdi);
        return AcdiResponseDTO.builder()
                .idAcdi(acdi.getIdAcdi())
                .idOrganizacion(acdi.getIdOrganizacionAcdi())
                .denominacion(acdi.getDenominacion())
                .liquidaEnByma(acdi.getLiquidaEnByma())
                .habilitado(acdi.getHabilitado())
                .billeteras(acdi.getBilleteras())
                .observaciones(acdi.getObservaciones())
                .fechaAlta(acdi.getFechaAlta().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .mail(acdi.getMail())
                .estado(acdi.getEstado())
                .build();
    }

    public static Acdi acdiActualizarSolicitudAAcdiModel(ActualizarAcdiRequest actualizarAcdiRequest) {
        Validador.validarNoNulo(actualizarAcdiRequest);
        return Acdi.builder()
                .mail(actualizarAcdiRequest.getMail())
                .liquidaEnByma(actualizarAcdiRequest.getLiquidaEnByma())
                .build();
    }

}
