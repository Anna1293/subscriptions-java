package ru.kudryaeva.subscriptions.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kudryaeva.subscriptions.api.dto.ErrorResponse;
import ru.kudryaeva.subscriptions.service.NotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponse> badRequest(Exception ex) {
        String message = ex instanceof MethodArgumentNotValidException
                ? "invalid json"
                : ex.getMessage();
        if (ex instanceof HttpMessageNotReadableException) {
            message = "invalid json";
        }
        if (ex instanceof MissingServletRequestParameterException) {
            message = "from and to are required (MM-YYYY)";
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }
}
