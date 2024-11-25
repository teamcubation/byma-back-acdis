package com.byma.back_acdits.infrastructure.adapter.in.web.controller;

import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.ActualizarAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.request.CrearAcdiRequest;
import com.byma.back_acdits.infrastructure.adapter.in.web.dto.response.AcdiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ApiAcdi {

    @Operation(summary = "Crear ACDI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ACDI creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<AcdiResponseDTO> crear(@RequestBody @Valid CrearAcdiRequest crearAcdiRequest);

    @Operation(summary = "Obtener todos los ACDI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ACDIs encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<AcdiResponseDTO>> obtenerTodos();

    @Operation(summary = "Obtener un ACDI por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ACDI encontrada"),
            @ApiResponse(responseCode = "404", description = "Error: ACDI no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<AcdiResponseDTO> obtenerPorId(@PathVariable Long id);

    @Operation(summary = "Actualizar un ACDI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ACDI actualizada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<AcdiResponseDTO> actualizar(@PathVariable Long id, @RequestBody @Valid ActualizarAcdiRequest actualizarAcdiRequest);

    @Operation(summary = "Dar de baja un ACDI(Queda en estado INHABILITADA)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ACDI dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Error: ACDI no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<Void> eliminar(@PathVariable Long id);

    @Operation(summary = "Eliminar un ACDI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ACDI eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Error: ACDI no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<AcdiResponseDTO> darDeBaja(@PathVariable Long id);

}
