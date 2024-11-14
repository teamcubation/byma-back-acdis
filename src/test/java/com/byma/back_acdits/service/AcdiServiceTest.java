package com.byma.back_acdits.service;


import com.byma.back_acdits.application.port.out.AcdiOutPort;
import com.byma.back_acdits.application.service.AcdiService;
import com.byma.back_acdits.application.service.exception.AcdiNoEncontradoException;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AcdiServiceTest {

    @Mock
    private AcdiOutPort acdiOutPort;

    @InjectMocks
    private AcdiService acdiService;

    private Acdi acdi;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        acdi = Acdi.builder()
                .idAcdi(1L)
                .idOrganizacionAcdi("1234")
                .denominacion("Denominacion")
                .liquidaEnByma(true)
                .habilitado(true)
                .billeteras(false)
                .observaciones("observacion")
                .fechaAlta(LocalDateTime.now())
                .mail("mail@mail.com")
                .estado(EstadoAcdi.CREADA)
                .build();
    }

    @Test
    void deberiaCrearAcdiExitosamente() {
        when(acdiOutPort.guardarAcdi(any(Acdi.class))).thenReturn(acdi);

        Acdi resultado = acdiService.crearAcdi(acdi);

        assertNotNull(resultado);
        assertEquals(acdi.getIdAcdi(), resultado.getIdAcdi());
        assertEquals(EstadoAcdi.CREADA, resultado.getEstado());

        verify(acdiOutPort, times(1)).guardarAcdi(any(Acdi.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoCrearAcdiEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> acdiService.crearAcdi(null));
    }

    @Test
    void deberiaActualizarAcdiExitosamente() {
        Long acdiId = 1L;
        Acdi acdiActualizado = Acdi.builder()
                .mail("nuevo@mail.com")
                .liquidaEnByma(true)
                .build();

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.of(acdi));
        when(acdiOutPort.guardarAcdi(any(Acdi.class))).thenReturn(acdiActualizado);

        Acdi resultado = acdiService.actualizarAcdi(acdiId, acdiActualizado);

        assertNotNull(resultado);
        assertEquals("nuevo@mail.com", resultado.getMail());
        assertEquals(true, resultado.getLiquidaEnByma());

        verify(acdiOutPort, times(1)).obtenerAcdiPorId(acdiId);
        verify(acdiOutPort, times(1)).guardarAcdi(any(Acdi.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoAcdiNoExisteParaActualizar() {
        Long acdiId = 999L;

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.actualizarAcdi(acdiId, acdi));
    }

    @Test
    void deberiaObtenerTodosLosAcdisExitosamente() {
        List<Acdi> acdis = List.of(acdi);
        when(acdiOutPort.obtenerTodosAcdis()).thenReturn(acdis);

        List<Acdi> resultado = acdiService.obtenerTodosLosAcdis();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(acdiOutPort, times(1)).obtenerTodosAcdis();
    }

    @Test
    void deberiaObtenerAcdiPorIdExitosamente() {
        Long acdiId = 1L;

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.of(acdi));

        Acdi resultado = acdiService.obtenerAcdiPorId(acdiId);

        assertNotNull(resultado);
        assertEquals(acdiId, resultado.getIdAcdi());

        verify(acdiOutPort, times(1)).obtenerAcdiPorId(acdiId);
    }

    @Test
    void deberiaLanzarExcepcionCuandoAcdiNoExisteParaObtener() {
        Long acdiId = 999L;

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.obtenerAcdiPorId(acdiId));
    }

    @Test
    void deberiaEliminarAcdiExitosamente() {
        Long acdiId = 1L;

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.of(acdi));
        doNothing().when(acdiOutPort).eliminarAcdi(acdiId);

        acdiService.eliminarAcdi(acdiId);

        verify(acdiOutPort, times(1)).eliminarAcdi(acdiId);
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAcdiParaEliminar() {
        Long acdiId = 999L;

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.eliminarAcdi(acdiId));
    }

    @Test
    void deberiaDarDeBajaAcdiExitosamente() {
        Long acdiId = 1L;

        Acdi acdiBajado = Acdi.builder()
                .habilitado(false)
                .estado(EstadoAcdi.DESHABILITADA)
                .build();

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.of(acdi));
        when(acdiOutPort.guardarAcdi(any(Acdi.class))).thenReturn(acdiBajado);

        Acdi resultado = acdiService.darDeBajaAcdi(acdiId);

        assertNotNull(resultado);
        assertFalse(resultado.getHabilitado());
        assertEquals(EstadoAcdi.DESHABILITADA, resultado.getEstado());

        verify(acdiOutPort, times(1)).guardarAcdi(any(Acdi.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAcdiParaBaja() {
        Long acdiId = 999L;

        when(acdiOutPort.obtenerAcdiPorId(acdiId)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.darDeBajaAcdi(acdiId));
    }


}
