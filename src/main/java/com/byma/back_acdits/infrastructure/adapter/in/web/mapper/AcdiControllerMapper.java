package com.byma.back_acdits.infrastructure.adapter.in.web.mapper;

import com.byma.back_acdits.application.util.Validador;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiSolicitud;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiSolicitud;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.response.AcdiRespuestaDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AcdiControllerMapper {

    public static Acdi acdiCrearSolicitudAAcdiModel(CrearAcdiSolicitud crearAcdiSolicitud) {
        Validador.validarNoNulo(crearAcdiSolicitud);
        return Acdi.builder()
                .idOrganizacionAcdi(crearAcdiSolicitud.getIdOrganizacion())
                .denominacion(crearAcdiSolicitud.getDenominacion())
                .liquidaEnByma(crearAcdiSolicitud.getLiquidaEnByma())
                .habilitado(crearAcdiSolicitud.getHabilitado())
                .billeteras(crearAcdiSolicitud.getBilleteras())
                .observaciones(crearAcdiSolicitud.getObservaciones())
                .fechaAlta(LocalDateTime.now())
                .mail(crearAcdiSolicitud.getMail())
                .estado(EstadoAcdi.CREADA)
                .build();
    }

    public static AcdiRespuestaDTO acdiModelAAcdiRespuestaDTO(Acdi acdi) {
        Validador.validarNoNulo(acdi);
        return AcdiRespuestaDTO.builder()
                .idAcdi(acdi.getIdAcdi())
                .idOrganizacionAcdi(acdi.getIdOrganizacionAcdi())
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

    public static Acdi acdiActualizarSolicitudAAcdiModel(ActualizarAcdiSolicitud actualizarAcdiSolicitud) {
        Validador.validarNoNulo(actualizarAcdiSolicitud);
        return Acdi.builder()
                .mail(actualizarAcdiSolicitud.getMail())
                .liquidaEnByma(actualizarAcdiSolicitud.getLiquidaEnByma())
                .build();
    }

}
