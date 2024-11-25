package com.byma.back_acdits.exceptionHandler;

import com.byma.back_acdits.application.service.exception.AcdiNoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AcdiNoEncontradoException.class)
    public ResponseEntity<?> manejarAcdiNoEcontradoException(Exception exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(this.createErrorMessageResponse(exception, request, HttpStatus.NOT_FOUND, ErrorMessages.ACDI_NO_ENCOTRADO));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorMessageResponse> manejarNoResourceFoundException(NoResourceFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(this.createErrorMessageResponse(exception, request, HttpStatus.NOT_FOUND, ErrorMessages.RECURSO_NO_ENCONTRADO));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> menejarExcepcionesDeValidacion(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorMessageResponse errorMessageResponse = ConstruirErrorMessageResponseDeValidacion(e, request);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessageResponse);
    }

    private ErrorMessageResponse ConstruirErrorMessageResponseDeValidacion(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult result = e.getBindingResult();
        return ErrorMessageResponse.builder()
                .exception(e.getClass().getName())
                .message(ErrorMessages.ERROR_VALIDACION)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .details(result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessageResponse> manejarArgumentTypeMismatch(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(this.createErrorMessageResponse(exception, request, HttpStatus.BAD_REQUEST, ErrorMessages.ARGUMENTO_INVALIDO));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarErroGenerico(Exception e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(this.createErrorMessageResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.ERROR_GENERICO));
    }

    private ErrorMessageResponse createErrorMessageResponse(Exception exception, HttpServletRequest request, HttpStatus status, String message) {
        return ErrorMessageResponse.builder()
                .exception(exception.getClass().getName())
                .message(message)
                .status(status.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
    }



}
