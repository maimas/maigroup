package com.mg.web.rest.errorhandlers;


import com.mg.persistence.exceptions.BizItemSchemaValidationException;
import com.mg.persistence.exceptions.RuntimeConfigurationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * Global exception handler for the application's Rest Controllers.
 * Dedicated to handle rest exceptions and map return corresponding response <code>ResponseEntity<>(String, HttpStatus)</></code>
 */
@ControllerAdvice
@Log4j2
public class RestExceptionHandler {


    @ExceptionHandler({
            BizItemSchemaValidationException.class,
    })
    public ResponseEntity handleBusinessErrors(HttpServletRequest request, Exception ex) {
        logError(ex);
        String bodyOfResponse = Optional.of(ex.getMessage()).orElse("Business error occurred. Please contact System Administrator");

        HttpErrorModel error = new HttpErrorModel(request, ex);
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(bodyOfResponse);
        return new ResponseEntity<>(error.getMapped(), error.getStatus());
    }


    @ExceptionHandler({RuntimeConfigurationException.class})
    public ResponseEntity handleMetadataInitializationErrors(HttpServletRequest request, Exception ex) {
        logError(ex);

        HttpErrorModel error = new HttpErrorModel(request, ex);
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage("Failed to initialize system metadata. See serve logs for more details.");
        return new ResponseEntity<>(error.getMapped(), error.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleAllErrors(HttpServletRequest request, Exception ex) {
        logError(ex);

        HttpErrorModel error = new HttpErrorModel(request, ex);
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage("Internal Server error. Please contact system administrator.");
        error.setErrorDetails(ex.getLocalizedMessage());

        return new ResponseEntity<>(error.getMapped(), error.getStatus());
    }


    /**
     * Log the exception. If debug is enabled log full stacktrace otherwise log only exception LocalizedMessage.
     *
     * @param e - Rest Exception that was handled.
     */
    private void logError(Exception e) {
        log.error("Rest Exception Handled: " + e.getLocalizedMessage());
        log.debug("Error: ", e);

    }
}
