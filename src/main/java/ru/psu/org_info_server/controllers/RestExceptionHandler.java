package ru.psu.org_info_server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.Response;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {
    private ResponseEntity<Response<String>> internalHandling(Exception ex, HttpStatus status) {
        return new ResponseEntity<>(Response.<String>builder().data(ex.getMessage()).build(), status);
    }

    @ExceptionHandler(HasChildrenException.class)
    protected ResponseEntity<Response<String>> hasChildren(HasChildrenException ex){
        return internalHandling(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Response<String>> notFound(NotFoundException ex){
        return internalHandling(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Response<String>> validationFailed(ConstraintViolationException ex) {
        return internalHandling(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnacceptableParamsException.class)
    protected ResponseEntity<Response<String>> unacceptableParams(UnacceptableParamsException ex){
        return internalHandling(ex, HttpStatus.NOT_ACCEPTABLE);
    }
}
