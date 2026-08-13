package com.posref.pos.exception;

import com.posref.pos.dto.error.ApiErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            RecursoNoEncontradoException.class
    )
    public ResponseEntity<ApiErrorResponse>
    manejarNoEncontrado(
            RecursoNoEncontradoException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        crearError(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(
            ReglaNegocioException.class
    )
    public ResponseEntity<ApiErrorResponse>
    manejarReglaNegocio(
            ReglaNegocioException exception
    ) {

        return ResponseEntity
                .badRequest()
                .body(
                        crearError(
                                HttpStatus.BAD_REQUEST,
                                exception.getMessage()
                        )
                );
    }

    private ApiErrorResponse crearError(
            HttpStatus status,
            String mensaje
    ) {

        return ApiErrorResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(status.value())
                .mensaje(mensaje)
                .build();
    }

}
