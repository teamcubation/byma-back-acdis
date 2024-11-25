package com.byma.back_acdits.controller;

import com.byma.back_acdits.application.port.in.AcdiInPort;
import com.byma.back_acdits.application.service.exception.AcdiNoEncontradoException;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.domain.model.EstadoAcdi;
import com.byma.back_acdits.exceptionHandler.GlobalExceptionHandler;
import com.byma.back_acdits.infrastructure.adapter.in.web.controller.impl.AcdiController;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiRequest;
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

    public static final String API_URL = "/api/v1/acdis";
    public static final String API_URL_BAJA = "/api/v1/acdis/{id}/baja";
    public static final String API_URL_BY_ID = "/api/v1/acdis/{id}";

    public static final String ORGANIZACION_1 = "Org1";
    public static final String DENOMINACION_1 = "Denom1";
    public static final String MAIL_TEST = "test@mail.com";
    public static final String MAIL_NUEVO = "nuevo@mail.com";

    public static final Long ACDI_ID_1 = 1L;
    public static final Long ACDI_ID_99 = 99L;

    public static final boolean LIQUIDA_EN_BYMA = true;
    public static final boolean HABILITADO = true;

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
        CrearAcdiRequest crearAcdiRequest = CrearAcdiRequest.builder()
                .idOrganizacion(ORGANIZACION_1)
                .denominacion(DENOMINACION_1)
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .habilitado(HABILITADO)
                .mail(MAIL_TEST)
                .build();

        Acdi acdiCreado = Acdi.builder()
                .idAcdi(ACDI_ID_1)
                .idOrganizacionAcdi(ORGANIZACION_1)
                .denominacion(DENOMINACION_1)
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .habilitado(HABILITADO)
                .fechaAlta(LocalDateTime.now())
                .estado(EstadoAcdi.CREADA)
                .mail(MAIL_TEST)
                .build();

        when(acdiInPort.crearAcdi(any(Acdi.class))).thenReturn(acdiCreado);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearAcdiRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAcdi").value(ACDI_ID_1))
                .andExpect(jsonPath("$.idOrganizacion").value(ORGANIZACION_1))
                .andExpect(jsonPath("$.denominacion").value(DENOMINACION_1));
    }

    @Test
    void deberiaDevolverError400CuandoCrearAcdiEsInvalido() throws Exception {
        CrearAcdiRequest crearAcdiRequest = CrearAcdiRequest.builder()
                .idOrganizacion("") // Invalid data
                .denominacion(DENOMINACION_1)
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .habilitado(HABILITADO)
                .mail(MAIL_TEST)
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearAcdiRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ERROR_VALIDACION));
    }

    @Test
    void deberiaActualizarAcdiExitosamenteCuandoAcdiEsValido() throws Exception {
        ActualizarAcdiRequest actualizarAcdiRequest = ActualizarAcdiRequest.builder()
                .mail(MAIL_NUEVO)
                .liquidaEnByma(false)
                .build();

        Acdi acdiActualizado = Acdi.builder()
                .idAcdi(ACDI_ID_1)
                .idOrganizacionAcdi(ORGANIZACION_1)
                .denominacion(DENOMINACION_1)
                .liquidaEnByma(false)
                .habilitado(HABILITADO)
                .fechaAlta(LocalDateTime.now())
                .mail(MAIL_NUEVO)
                .build();

        when(acdiInPort.actualizarAcdi(eq(ACDI_ID_1), any())).thenReturn(acdiActualizado);

        mockMvc.perform(put(API_URL_BY_ID, ACDI_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarAcdiRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAcdi").value(ACDI_ID_1))
                .andExpect(jsonPath("$.mail").value(MAIL_NUEVO));
    }

    @Test
    void deberiaDevolverStatus404CuandoAcdiNoExisteParaActualizar() throws Exception {
        ActualizarAcdiRequest actualizarAcdiRequest = ActualizarAcdiRequest.builder()
                .mail(MAIL_NUEVO)
                .liquidaEnByma(true)
                .build();

        when(acdiInPort.actualizarAcdi(eq(ACDI_ID_99), any())).thenThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO));

        mockMvc.perform(put(API_URL_BY_ID, ACDI_ID_99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarAcdiRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));
    }

    @Test
    void deberiaObtenerTodosLosAcdisExitosamente() throws Exception {
        List<Acdi> acdis = Arrays.asList(
                Acdi.builder().idAcdi(ACDI_ID_1).idOrganizacionAcdi(ORGANIZACION_1).fechaAlta(LocalDateTime.now()).build(),
                Acdi.builder().idAcdi(2L).idOrganizacionAcdi("Org2").fechaAlta(LocalDateTime.now()).build()
        );

        when(acdiInPort.obtenerTodosLosAcdis()).thenReturn(acdis);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idOrganizacion").value(ORGANIZACION_1));
    }

    @Test
    void deberiaDevolverListaVaciaCuandoNoExistenAcdis() throws Exception {
        when(acdiInPort.obtenerTodosLosAcdis()).thenReturn(Arrays.asList());

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deberiaObtenerAcdiPorIdCuandoExiste() throws Exception {
        Acdi acdi = Acdi.builder()
                .idAcdi(ACDI_ID_1)
                .idOrganizacionAcdi(ORGANIZACION_1)
                .denominacion(DENOMINACION_1)
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .habilitado(HABILITADO)
                .fechaAlta(LocalDateTime.now())
                .mail(MAIL_TEST)
                .build();

        when(acdiInPort.obtenerAcdiPorId(ACDI_ID_1)).thenReturn(acdi);

        mockMvc.perform(get(API_URL_BY_ID, ACDI_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAcdi").value(ACDI_ID_1))
                .andExpect(jsonPath("$.idOrganizacion").value(ORGANIZACION_1));
    }

    @Test
    void deberiaDevolverStatus404CuandoNoSeEncuentraAcdiPorId() throws Exception {
        when(acdiInPort.obtenerAcdiPorId(ACDI_ID_1)).thenThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO));

        mockMvc.perform(get(API_URL_BY_ID, ACDI_ID_1))
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));
    }

    @Test
    void deberiaEliminarAcdiExitosamente() throws Exception {
        doNothing().when(acdiInPort).eliminarAcdi(ACDI_ID_1);

        mockMvc.perform(delete(API_URL_BY_ID, ACDI_ID_1))
                .andExpect(status().isNoContent());
    }

    @Test
    void deberiaDevolverStatus404CuandoAcdiNoExisteParaEliminar() throws Exception {
        doThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO)).when(acdiInPort).eliminarAcdi(ACDI_ID_1);

        mockMvc.perform(delete(API_URL_BY_ID, ACDI_ID_1))
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));
    }

    @Test
    void deberiaDevolverOkCuandoDarDeBajaEsExitoso() throws Exception {
        Acdi acdiBajado = Acdi.builder()
                .idAcdi(ACDI_ID_1)
                .idOrganizacionAcdi("1234")
                .denominacion("Denominacion")
                .liquidaEnByma(LIQUIDA_EN_BYMA)
                .habilitado(HABILITADO)
                .billeteras(false)
                .observaciones("observacion")
                .fechaAlta(LocalDateTime.now())
                .mail("mail@mail.com")
                .estado(EstadoAcdi.DESHABILITADA)
                .build();

        when(acdiInPort.darDeBajaAcdi(ACDI_ID_1)).thenReturn(acdiBajado);

        mockMvc.perform(put(API_URL_BAJA, ACDI_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAcdi").value(ACDI_ID_1))
                .andExpect(jsonPath("$.estado").value(EstadoAcdi.DESHABILITADA.toString()));
    }

    @Test
    void deberiaDevolverNotFoundCuandoNoSeEncuentraElAcdiParaBaja() throws Exception {
        when(acdiInPort.darDeBajaAcdi(ACDI_ID_99)).thenThrow(new AcdiNoEncontradoException(ACDI_NO_ENCOTRADO));

        mockMvc.perform(put(API_URL_BAJA, ACDI_ID_99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ACDI_NO_ENCOTRADO));
    }
}
