package com.byma.back_acdits.infrastructure.adapter.in.web.controller.impl;

import com.byma.back_acdits.application.port.in.AcdiInPort;
import com.byma.back_acdits.application.util.Validador;
import com.byma.back_acdits.domain.model.Acdi;
import com.byma.back_acdits.infrastructure.adapter.in.web.controller.ApiAcdi;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.response.AcdiResponseDTO;
import com.byma.back_acdits.infrastructure.adapter.in.web.mapper.AcdiControllerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/acdis")
public class AcdiController implements ApiAcdi {

    private final AcdiInPort acdiInPort;

    @PostMapping
    public ResponseEntity<AcdiResponseDTO> crear(@RequestBody @Valid CrearAcdiRequest crearAcdiRequest) {
        log.info("Iniciando creacion de ACDI con datos: {}", crearAcdiRequest);
        Validador.validarNoNulo(crearAcdiRequest);
        Acdi acdiCreado = acdiInPort.crearAcdi(AcdiControllerMapper.mapRequestToModel(crearAcdiRequest));
        log.info("ACDI creado exitosamente: {}", acdiCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(AcdiControllerMapper.mapModelToResponseDTO(acdiCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcdiResponseDTO> actualizar(@PathVariable Long id, @RequestBody @Valid ActualizarAcdiRequest actualizarAcdiRequest) {
        log.info("Iniciando actualizacion de ACDI con ID: {} y datos: {}", id, actualizarAcdiRequest);
        Validador.validarNoNulo(id, actualizarAcdiRequest);
        Acdi acdiActualizado = acdiInPort.actualizarAcdi(id, AcdiControllerMapper.acdiActualizarSolicitudAAcdiModel(actualizarAcdiRequest));
        log.info("ACDI actualizado exitosamente: {}", acdiActualizado);
        return ResponseEntity.ok().body(AcdiControllerMapper.mapModelToResponseDTO(acdiActualizado));
    }

    @GetMapping
    public ResponseEntity<List<AcdiResponseDTO>> obtenerTodos() {
        log.info("Iniciando obtencion de todos los ACDIs");
        List<Acdi> acdis = acdiInPort.obtenerTodosLosAcdis();
        log.info("Se obtuvieron {} ACDIs", acdis.size());
        return ResponseEntity.ok()
                .body(acdis.stream().map(AcdiControllerMapper::mapModelToResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcdiResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Iniciando obtencion del ACDI con ID: {}", id);
        Validador.validarNoNulo(id);
        Acdi acdi = acdiInPort.obtenerAcdiPorId(id);
        log.info("ACDI obtenido: {}", acdi);
        return ResponseEntity.ok(AcdiControllerMapper.mapModelToResponseDTO(acdi));
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
    public ResponseEntity<AcdiResponseDTO> darDeBaja(@PathVariable Long id) {
        log.info("Iniciando baja del ACDI con ID: {}", id);
        Validador.validarNoNulo(id);
        Acdi acdiBajado = acdiInPort.darDeBajaAcdi(id);
        log.info("ACDI con ID: {} dado de baja exitosamente", id);
        return ResponseEntity.ok(AcdiControllerMapper.mapModelToResponseDTO(acdiBajado));
    }

}
