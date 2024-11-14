package com.byma.back_acdits.adapter;

import com.byma.back_acdits.application.service.exception.AcdiNoEncontradoException;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import com.byma.back_acdits.infrastructure.adapter.out.persistance.AcdiOutAdapter;
import com.byma.back_acdits.infrastructure.adapter.out.persistance.entity.AcdiEntity;
import com.byma.back_acdits.infrastructure.adapter.out.persistance.repository.AcdiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public class AcdiOutAdapterTest {

    @Mock
    private AcdiRepository acdiRepository;

    @InjectMocks
    private AcdiOutAdapter acdiOutAdapter;

    private Acdi acdi;
    private AcdiEntity acdiEntity;

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

        acdiEntity = AcdiEntity.builder()
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
    void deberiaGuardarAcdiExitosamente() {
        when(acdiRepository.save(any(AcdiEntity.class))).thenReturn(acdiEntity);

        Acdi resultado = acdiOutAdapter.guardarAcdi(acdi);

        assertNotNull(resultado);
        assertEquals(acdi.getIdAcdi(), resultado.getIdAcdi());
        assertEquals(EstadoAcdi.CREADA, resultado.getEstado());

        verify(acdiRepository, times(1)).save(any(AcdiEntity.class));
    }

    @Test
    void deberiaLanzarExcepcionCuandoGuardarAcdiEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> acdiOutAdapter.guardarAcdi(null));
    }

    @Test
    void deberiaObtenerAcdiPorIdExitosamente() {
        Long acdiId = 1L;

        when(acdiRepository.findById(acdiId)).thenReturn(Optional.of(acdiEntity));

        Optional<Acdi> resultado = acdiOutAdapter.obtenerAcdiPorId(acdiId);

        assertTrue(resultado.isPresent());
        assertEquals(acdiId, resultado.get().getIdAcdi());

        verify(acdiRepository, times(1)).findById(acdiId);
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAcdiPorId() {
        Long acdiId = 99L;

        when(acdiRepository.findById(acdiId)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiOutAdapter.obtenerAcdiPorId(acdiId));
    }

    @Test
    void deberiaObtenerTodosLosAcdisExitosamente() {
        when(acdiRepository.findAll()).thenReturn(List.of(acdiEntity));

        List<Acdi> resultado = acdiOutAdapter.obtenerTodosAcdis();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(acdiRepository, times(1)).findAll();
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoHayAcdis() {
        when(acdiRepository.findAll()).thenReturn(List.of());

        List<Acdi> resultado = acdiOutAdapter.obtenerTodosAcdis();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        verify(acdiRepository, times(1)).findAll();
    }

    @Test
    void deberiaEliminarAcdiExitosamente() {
        Long idAcdi = 1L;

        when(acdiRepository.existsById(idAcdi)).thenReturn(true);

        acdiOutAdapter.eliminarAcdi(idAcdi);

        verify(acdiRepository, times(1)).deleteById(idAcdi);

    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAcdiParaEliminar() {
        Long acdiId = 999L;

        when(acdiRepository.findById(acdiId)).thenReturn(Optional.empty());

        assertThrows(AcdiNoEncontradoException.class, () -> acdiOutAdapter.eliminarAcdi(acdiId));
    }

}
