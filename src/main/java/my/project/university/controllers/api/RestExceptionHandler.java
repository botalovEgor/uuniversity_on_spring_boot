package my.project.university.controllers.api;


import my.project.university.exceptions.CustomErrorBody;
import my.project.university.exceptions.NotFoundEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "my.project.university.controllers.api")
public class RestExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<CustomErrorBody> handleEntityNotFoundException(NotFoundEntityException e,
                                              WebRequest request) {
        LOG.error("handled exception", e);

        CustomErrorBody error = new CustomErrorBody(e.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorBody> handleIllegalArgumentException(IllegalArgumentException e,
                                               WebRequest request) {
        LOG.error("handled exception", e);

        CustomErrorBody error = new CustomErrorBody(e.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorBody> handleCBindException(MethodArgumentNotValidException e,
                                     WebRequest request) {

        LOG.error("handled exception", e);

        StringBuilder builder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(o -> builder.append(o.getDefaultMessage()).append(","));
        builder.deleteCharAt(builder.length()-1);

        CustomErrorBody error = new CustomErrorBody(builder.toString(), request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorBody> handleConstraintViolationException(ConstraintViolationException e,
                                                   WebRequest request) {
        LOG.error("handled exception", e);

        CustomErrorBody error = new CustomErrorBody(e.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorBody> handleHZException(DataIntegrityViolationException e, WebRequest request) {
        LOG.error("handled exception", e);

        CustomErrorBody error = new CustomErrorBody("Already exists", request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorBody> handleAnyException(Exception e,
                                   WebRequest request) {
        LOG.error("handled exception", e);

        CustomErrorBody error = new CustomErrorBody(e.getMessage(), request);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
