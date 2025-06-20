package com.fola.EtherStockPro.exceptions;

import com.fola.EtherStockPro.DTO.Response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleNotFoundException( NotFoundException ex) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleInvalidCredentialsException( InvalidCredentialsException ex) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return  new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValueRequiredException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleValueRequiredException( ValueRequiredException ex) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<ApiResponse<T>> handleGlobalException(Exception ex) throws Exception {

        if (ex instanceof AccessDeniedException || ex instanceof AuthenticationException) {
            throw ex; // rethrow to let Spring Security handle it
        }
        log.error("handler caught exception class: {}", ex.getClass().getName());

        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred " + ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
