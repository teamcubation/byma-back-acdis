package com.byma.back_acdits.controller;

import com.byma.back_acdits.application.port.in.AcdiInPort;
import com.byma.back_acdits.application.service.exception.AcdiNoEncontradoException;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import com.byma.back_acdits.exceptionHandler.GlobalExceptionHandler;
import com.byma.back_acdits.infrastructure.adapter.in.web.controller.impl.AcdiController;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiSolicitud;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiSolicitud;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AcdiControllerTest {

    public static final String ERROR_VALIDACION = "Los datos enviados no cumplen con los criterios de validacion.";
    public static final String ACDI_NO_ENCOTRADO = "ACDI no encontrado.";

    @Mock
    private AcdiInPort acdiInPort;

    @InjectMocks
    private AcdiController acdiController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(acdiController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deberiaCrearAcdiCuandoEsValido() throws Exception {
        CrearAcdiSolicitud crearAcdiSolicitud = CrearAcdiSolicitud.builder()
                .idOrganizacion("Org1")
                .denominacion("Denom1")
                .liquidaEnByma(true)
                .habilitado(true)
                .mail("test@mail.com")
                .build();

        Acdi acdiCreado = Acdi.builder()
                .idAcdi(1L)
                .idOrganizacionAcdi("Org1")
                .denominacion("Denom1")
                .liquidaEnByma(true)
                .habilitado(true)
                .fechaAlta(LocalDateTime.now())
                .estado(EstadoAcdi.CREADA)
                .mail("test@mail.com")
                .build();

        when(acdiInPort.crearAcdi(any(Acdi.class))).thenReturn(acdiCreado);

        mockMvc.perform(post("/api/v1/acdis")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(crearAcdiSolicitud)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAcdi").value(1L))
                .andExpect(jsonPath("$.idOrganizacionAcdi").value("Org1"))
                .andExpect(jsonPath("$.denominacion").value("Denom1"));

    }

    @Test
    void deberiaDevolverError400CuandoCrearAcdiEsInvalido() throws Exception {
        CrearAcdiSolicitud crearAcdiSolicitud = CrearAcdiSolicitud.builder()
                .idOrganizacion("")
                .denominacion("Denom1")
                .liquidaEnByma(true)
                .habilitado(true)
                .mail("test@mail.com")
                .build();

        mockMvc.perform(post("/api/v1/acdis")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(crearAcdiSolicitud)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ERROR_VALIDACION));
    }

    @Test
    void deberiaActualizarAcdiExitosamenteCuandoAcdiEsValido() throws Exception {
        Long acdiId = 1L;
        ActualizarAcdiSolicitud actualizarAcdiSolicitud = ActualizarAcdiSolicitud.builder()
                .mail("nuevo@mail.com")
                .liquidaEnByma(false)
                .build();

        Acdi acdiActualizado = Acdi.builder()
                .idAcdi(acdiId)
                .idOrganizacionAcdi("Org1")
                .denominacion("Denom1")
                .liquidaEnByma(false)
                .habilitado(true)
                .fechaAlta(LocalDateTime.now())
                .mail("nuevo@mail.com")
                .build();

        when(acdiInPort.actualizarAcdi(eq(acdiId), any())).thenReturn(acdiActualizado);

        mockMvc.perform(put("/api/v1/acdis/{id}", acdiId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(actualizarAcdiSolicitud)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAcdi").value(acdiId))
                .andExpect(jsonPath("$.mail").value("nuevo@mail.com"));

    }

    @Test
    void deberiaDevolverStatus404CuandoAcdiNoExisteParaActualizar() throws Exception {
        final Long acdiId = 99L;
        ActualizarAcdiSolicitud actualizarAcdiSolicitud = ActualizarAcdiSolicitud.builder()
                .mail("nuevo@mail.com")
                .liquidaEnByma(true)
                .build();

        when(acdiInPort.actualizarAcdi(eq(acdiId), any())).thenThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO));

        mockMvc.perform(put("/api/v1/acdis/{id}", acdiId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarAcdiSolicitud)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));
    }

    @Test
    void deberiaObtenerTodosLosAcdisExitosamente() throws Exception {
        List<Acdi> acdis = Arrays.asList(
                Acdi.builder().idAcdi(1L).idOrganizacionAcdi("Org1").fechaAlta(LocalDateTime.now()).build(),
                Acdi.builder().idAcdi(2L).idOrganizacionAcdi("Org2").fechaAlta(LocalDateTime.now()).build()
        );

        when(acdiInPort.obtenerTodosLosAcdis()).thenReturn(acdis);

        mockMvc.perform(get("/api/v1/acdis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idOrganizacionAcdi").value("Org1"));

    }

    @Test
    void deberiaDevolverListaVaciaCuandoNoExistenAcdis() throws Exception {
        when(acdiInPort.obtenerTodosLosAcdis()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/acdis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    void deberiaObtenerAcdiPorIdCuandoExiste() throws Exception {
        Long acdiId = 1L;
        Acdi acdi = Acdi.builder()
                .idAcdi(acdiId)
                .idOrganizacionAcdi("Org1")
                .denominacion("Denom1")
                .liquidaEnByma(true)
                .habilitado(true)
                .fechaAlta(LocalDateTime.now())
                .mail("test@mail.com")
                .build();

        when(acdiInPort.obtenerAcdiPorId(acdiId)).thenReturn(acdi);

        mockMvc.perform(get("/api/v1/acdis/{id}", acdiId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAcdi").value(acdiId))
                .andExpect(jsonPath("$.idOrganizacionAcdi").value("Org1"));

    }

    @Test
    void deberiaDevolverStatus404CuandoNoSeEncuentraAcdiPorId() throws Exception {
        Long acdiId = 1L;

        when(acdiInPort.obtenerAcdiPorId(acdiId)).thenThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO));

        mockMvc.perform(get("/api/v1/acdis/{id}", acdiId))
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));

    }

    @Test
    void deberiaEliminarAcdiExitosamente() throws Exception {
        Long acdiId = 1L;

        doNothing().when(acdiInPort).eliminarAcdi(acdiId);

        mockMvc.perform(delete("/api/v1/acdis/{id}", acdiId))
                .andExpect(status().isNoContent());

    }

    @Test
    void deberiaDevolverStatus404CuandoAcdiNoExisteParaEliminar() throws Exception {
        Long acdiId = 1L;

        doThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO)).when(acdiInPort).eliminarAcdi(acdiId);

        mockMvc.perform(delete("/api/v1/acdis/{id}", acdiId))
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));

    }

    @Test
    void deberiaDevolverOkCuandoDarDeBajaEsExitoso() throws Exception {
        final long acdiId = 1L;

        Acdi acdiBajado = Acdi.builder()
                .idAcdi(acdiId)
                .idOrganizacionAcdi("1234")
                .denominacion("Denominacion")
                .liquidaEnByma(true)
                .habilitado(true)
                .billeteras(false)
                .observaciones("observacion")
                .fechaAlta(LocalDateTime.now())
                .mail("mail@mail.com")
                .estado(EstadoAcdi.DESHABILITADA)
                .build();

        when(acdiInPort.darDeBajaAcdi(acdiId)).thenReturn(acdiBajado);

        mockMvc.perform(put("/api/v1/acdis/{id}/baja", acdiId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAcdi").value(acdiId))
                .andExpect(jsonPath("$.estado").value(EstadoAcdi.DESHABILITADA.toString()));
    }

    @Test
    void deberiaDevolverNotFoundCuandoNoSeEncuentraElAcdiParaBaja() throws Exception {
        final long acdiId = 99L;

        when(acdiInPort.darDeBajaAcdi(acdiId)).thenThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO));

        mockMvc.perform(put("/api/v1/acdis/{id}/baja", acdiId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));
    }

}
