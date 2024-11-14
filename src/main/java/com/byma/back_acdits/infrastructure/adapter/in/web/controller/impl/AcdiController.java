package com.byma.back_acdits.infrastructure.adapter.in.web.controller.impl;

import com.byma.back_acdits.application.port.in.AcdiInPort;
import com.byma.back_acdits.application.util.Validador;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.infrastructure.adapter.in.web.controller.ApiAcdi;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiSolicitud;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiSolicitud;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.response.AcdiRespuestaDTO;
import com.byma.back_acdits.infrastructure.adapter.in.web.mapper.AcdiControllerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/acdis")
public class AcdiController implements ApiAcdi {

    private final AcdiInPort acdiInPort;

    @PostMapping
    public ResponseEntity<AcdiRespuestaDTO> crear(@RequestBody @Valid CrearAcdiSolicitud crearAcdiSolicitud) {
        log.info("Iniciando creacion de ACDI con datos: {}", crearAcdiSolicitud);
        Validador.validarNoNulo(crearAcdiSolicitud);
        Acdi acdiCreado = acdiInPort.crearAcdi(AcdiControllerMapper.acdiCrearSolicitudAAcdiModel(crearAcdiSolicitud));
        log.info("ACDI creado exitosamente: {}", acdiCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(AcdiControllerMapper.acdiModelAAcdiRespuestaDTO(acdiCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcdiRespuestaDTO> actualizar(@PathVariable Long id, @RequestBody @Valid ActualizarAcdiSolicitud actualizarAcdiSolicitud) {
        log.info("Iniciando actualizacion de ACDI con ID: {} y datos: {}", id, actualizarAcdiSolicitud);
        Validador.validarNoNulo(id, actualizarAcdiSolicitud);
        Acdi acdiActualizado = acdiInPort.actualizarAcdi(id, AcdiControllerMapper.acdiActualizarSolicitudAAcdiModel(actualizarAcdiSolicitud));
        log.info("ACDI actualizado exitosamente: {}", acdiActualizado);
        return ResponseEntity.ok().body(AcdiControllerMapper.acdiModelAAcdiRespuestaDTO(acdiActualizado));
    }

    @GetMapping
    public ResponseEntity<List<AcdiRespuestaDTO>> obtenerTodos() {
        log.info("Iniciando obtencion de todos los ACDIs");
        List<Acdi> acdis = acdiInPort.obtenerTodosLosAcdis();
        log.info("Se obtuvieron {} ACDIs", acdis.size());
        return ResponseEntity.ok()
                .body(acdis.stream().map(AcdiControllerMapper::acdiModelAAcdiRespuestaDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcdiRespuestaDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Iniciando obtencion del ACDI con ID: {}", id);
        Validador.validarNoNulo(id);
        Acdi acdi = acdiInPort.obtenerAcdiPorId(id);
        log.info("ACDI obtenido: {}", acdi);
        return ResponseEntity.ok(AcdiControllerMapper.acdiModelAAcdiRespuestaDTO(acdi));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Iniciando eliminacion del ACDI con ID: {}", id);
        Validador.validarNoNulo(id);
        acdiInPort.eliminarAcdi(id);
        log.info("ACDI con ID: {} eliminado exitosamente", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<AcdiRespuestaDTO> darDeBaja(@PathVariable Long id) {
        log.info("Iniciando baja del ACDI con ID: {}", id);
        Validador.validarNoNulo(id);
        Acdi acdiBajado = acdiInPort.darDeBajaAcdi(id);
        log.info("ACDI con ID: {} dado de baja exitosamente", id);
        return ResponseEntity.ok(AcdiControllerMapper.acdiModelAAcdiRespuestaDTO(acdiBajado));
    }

}
