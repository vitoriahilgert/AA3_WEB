package br.ufscar.dc.dsw.AA2.exceptions.controller;

import br.ufscar.dc.dsw.AA2.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Error> resourceAlreadyExists(ResourceAlreadyExistsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        Error err = new Error();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Resource already exists exception.");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Error> invalidCredentials(InvalidCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Error err = new Error();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Invalid credentials exception.");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Error err = new Error();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Resource not found exception.");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Error> badRequest(BadRequestException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error err = new Error();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Invalid request argument exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UnauthorizedExeption.class)
    public ResponseEntity<Error> unauthorizedException(UnauthorizedExeption e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Error err = new Error();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Unauthorized access exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Error> storage(StorageException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Error err = new Error();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Storage exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }
}
