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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AcdiServiceTest {

    public static final Long ACDI_ID_1 = 1L;
    public static final Long ACDI_ID_999 = 999L;
    public static final String ORGANIZACION = "1234";
    public static final String DENOMINACION = "Denominacion";
    public static final String MAIL_TEST = "mail@mail.com";
    public static final String MAIL_NUEVO = "nuevo@mail.com";
    public static final String OBSERVACIONES = "observacion";
    public static final boolean LIQUIDA_EN_BYMA = true;
    public static final boolean HABILITADO = true;
    public static final boolean DESHABILITADO = false;
    public static final EstadoAcdi ESTADO_CREADA = EstadoAcdi.CREADA;
    public static final EstadoAcdi ESTADO_DESHABILITADA = EstadoAcdi.DESHABILITADA;

    @Mock
    private AcdiOutPort acdiOutPort;

    @InjectMocks
    private AcdiService acdiService;

    private Acdi acdiMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        acdiMock = Acdi.builder()
                .idAcdi(ACDI_ID_1)
                .idOrganizacionAcdi(ORGANIZACION)
                .denominacion(DENOMINACION)
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .habilitado(HABILITADO)
                .billeteras(false)
                .observaciones(OBSERVACIONES)
                .fechaAlta(LocalDateTime.now())
                .mail(MAIL_TEST)
                .estado(ESTADO_CREADA)
                .build();
    }

    @Test
    void deberiaCrearAcdiExitosamente() {
        when(acdiOutPort.guardarAcdi(any(Acdi.class))).thenReturn(acdiMock);

        Acdi resultado = acdiService.crearAcdi(acdiMock);

        assertNotNull(resultado);
        assertEquals(acdiMock.getIdAcdi(), resultado.getIdAcdi());
        assertEquals(ESTADO_CREADA, resultado.getEstado());

        verify(acdiOutPort, times(1)).guardarAcdi(any(Acdi.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoCrearAcdiEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> acdiService.crearAcdi(null));
    }

    @Test
    void deberiaActualizarAcdiExitosamente() {
        Acdi acdiActualizado = Acdi.builder()
                .mail(MAIL_NUEVO)
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .build();

        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_1)).thenReturn(Optional.of(acdiMock));
        when(acdiOutPort.guardarAcdi(any(Acdi.class))).thenReturn(acdiActualizado);

        Acdi resultado = acdiService.actualizarAcdi(ACDI_ID_1, acdiActualizado);

        assertNotNull(resultado);
        assertEquals(MAIL_NUEVO, resultado.getMail());
        assertTrue(resultado.getLiquidaEnByma());

        verify(acdiOutPort, times(1)).obtenerAcdiPorId(ACDI_ID_1);
        verify(acdiOutPort, times(1)).guardarAcdi(any(Acdi.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoAcdiNoExisteParaActualizar() {
        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_999)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.actualizarAcdi(ACDI_ID_999, acdiMock));
    }

    @Test
    void deberiaObtenerTodosLosAcdisExitosamente() {
        List<Acdi> acdis = List.of(acdiMock);
        when(acdiOutPort.obtenerTodosAcdis()).thenReturn(acdis);

        List<Acdi> resultado = acdiService.obtenerTodosLosAcdis();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(acdiOutPort, times(1)).obtenerTodosAcdis();
    }

    @Test
    void deberiaObtenerAcdiPorIdExitosamente() {
        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_1)).thenReturn(Optional.of(acdiMock));

        Acdi resultado = acdiService.obtenerAcdiPorId(ACDI_ID_1);

        assertNotNull(resultado);
        assertEquals(ACDI_ID_1, resultado.getIdAcdi());

        verify(acdiOutPort, times(1)).obtenerAcdiPorId(ACDI_ID_1);
    }

    @Test
    void deberiaLanzarExcepcionCuandoAcdiNoExisteParaObtener() {
        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_999)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.obtenerAcdiPorId(ACDI_ID_999));
    }

    @Test
    void deberiaEliminarAcdiExitosamente() {
        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_1)).thenReturn(Optional.of(acdiMock));
        doNothing().when(acdiOutPort).eliminarAcdi(ACDI_ID_1);

        acdiService.eliminarAcdi(ACDI_ID_1);

        verify(acdiOutPort, times(1)).eliminarAcdi(ACDI_ID_1);
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAcdiParaEliminar() {
        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_999)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.eliminarAcdi(ACDI_ID_999));
    }

    @Test
    void deberiaDarDeBajaAcdiExitosamente() {
        Acdi acdiBajado = Acdi.builder()
                .habilitado(DESHABILITADO)
                .estado(ESTADO_DESHABILITADA)
                .build();

        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_1)).thenReturn(Optional.of(acdiMock));
        when(acdiOutPort.guardarAcdi(any(Acdi.class))).thenReturn(acdiBajado);

        Acdi resultado = acdiService.darDeBajaAcdi(ACDI_ID_1);

        assertNotNull(resultado);
        assertFalse(resultado.getHabilitado());
        assertEquals(ESTADO_DESHABILITADA, resultado.getEstado());

        verify(acdiOutPort, times(1)).guardarAcdi(any(Acdi.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAcdiParaBaja() {
        when(acdiOutPort.obtenerAcdiPorId(ACDI_ID_999)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiService.darDeBajaAcdi(ACDI_ID_999));
    }
}
