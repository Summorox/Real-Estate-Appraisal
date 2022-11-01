package pt.ipp.isep.controller;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.rule.ConsequenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.ipp.isep.model.ApiException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ApiException.class, ConsequenceException.class})
    public ResponseEntity<String> handleApiException(ApiException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus())
                            .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception ex) {
        log.error("Request failed", ex);
        return ResponseEntity.internalServerError().build();
    }
}
