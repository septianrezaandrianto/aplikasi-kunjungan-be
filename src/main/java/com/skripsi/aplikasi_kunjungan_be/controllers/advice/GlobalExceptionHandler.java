package com.skripsi.aplikasi_kunjungan_be.controllers.advice;

import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.handler.BadRequestCustomException;
import com.skripsi.aplikasi_kunjungan_be.handler.DataExistException;
import com.skripsi.aplikasi_kunjungan_be.handler.GeneralErrorException;
import com.skripsi.aplikasi_kunjungan_be.handler.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(mappingError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFoundException(NotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response<?>> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Response<?>> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataExistException.class)
    public final ResponseEntity<Response<?>> handleRuntimeExceptions(DataExistException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestCustomException.class)
    public final ResponseEntity<Response<?>> handleBadRequestCustomException(BadRequestCustomException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeneralErrorException.class)
    public final ResponseEntity<Response<?>> generalErrorException(GeneralErrorException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(mappingError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Response<Object> mappingError(int statusCode, String statusMessage, List<String> errors) {
        return Response.builder()
                .statusCode(statusCode)
                .statusMessage(statusMessage)
                .errorList(errors)
                .build();
    }

}
