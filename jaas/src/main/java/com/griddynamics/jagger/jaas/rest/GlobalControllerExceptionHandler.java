package com.griddynamics.jagger.jaas.rest;

import com.griddynamics.jagger.jaas.exceptions.ResourceAlreadyExistsException;
import com.griddynamics.jagger.jaas.exceptions.ResourceNotFoundException;
import com.griddynamics.jagger.jaas.exceptions.TestSuiteNotFoundException;
import com.griddynamics.jagger.jaas.exceptions.WrongTestEnvironmentStatusException;
import com.griddynamics.jagger.jaas.rest.error.ErrorResponse;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.griddynamics.jagger.jaas.rest.error.ErrorResponse.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Handles all exceptional situations for all rest controllers.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Catches an {@link org.hibernate.StaleStateException} exception which occurs if we try delete or update a row that
     * does not exist.
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(StaleStateException.class)
    public void noDataFound(StaleStateException exception) {
        LOGGER.error(exception.getMessage(), exception);
    }

    /**
     * Catches an {@link com.griddynamics.jagger.jaas.exceptions.ResourceNotFoundException} exception which occurs
     * once requested resource not found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException rnfe) {
        LOGGER.error(rnfe.getMessage(), rnfe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse(rnfe.getMessage(), NOT_FOUND), httpHeaders, NOT_FOUND);
    }

    /**
     * Catches an {@link com.griddynamics.jagger.jaas.exceptions.ResourceAlreadyExistsException} exception which occurs
     * once somebody tries to create already existing resource.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> resourceExists(ResourceAlreadyExistsException exception) {
        LOGGER.error(exception.getMessage(), exception);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse(exception.getMessage(), CONFLICT), httpHeaders, CONFLICT);
    }


    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> foreignKeyException(DataIntegrityViolationException error) {
        LOGGER.error(error.getMessage(), error);
        ResponseEntity<?> responseEntity;
        if (error.getMessage().contains("FOREIGN KEY")) {
            responseEntity = new ResponseEntity<>("There is no such database.", HttpStatus.BAD_REQUEST);
        } else {
            responseEntity = new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * Handles an exception which occurs if we try to access unavailable endpoint.
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceAccessException.class)
    public void serverUnavailable(ResourceAccessException exception) {
        LOGGER.error(exception.getMessage(), exception);
    }

    /**
     * Handles an exception which occurs if client provided incorrect request
     * such as 404 http response.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public void resourceNotFound(HttpClientErrorException exception, HttpServletResponse response) throws IOException {
        LOGGER.error(exception.getMessage(), exception);
        response.setContentType(APPLICATION_JSON.getType());
        response.setStatus(exception.getRawStatusCode());
    }

    /**
     * Handles exceptions which occur if client tries to set:
     * - runningTestSuite which doesn't belong to that Test Environment or
     * - status which doesn't corresponds to runningTestSuite value.
     */
    @ExceptionHandler({TestSuiteNotFoundException.class, WrongTestEnvironmentStatusException.class})
    public ResponseEntity<ErrorResponse> badTestSuite(TestSuiteNotFoundException exception) {
        LOGGER.error(exception.getMessage(), exception);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse(exception.getMessage(), BAD_REQUEST), httpHeaders, BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public void otherExceptions(Throwable exception) {
        LOGGER.error(exception.getMessage(), exception);
    }
}
