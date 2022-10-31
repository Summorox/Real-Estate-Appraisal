package pt.ipp.isep.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.ipp.isep.model.ApiException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handle(ApiException apiException) {
        log.warn(apiException.getMessage());
        return ResponseEntity.status(apiException.getHttpStatus())
                            .body(apiException.getMessage());
    }

    public ResponseEntity handle(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
