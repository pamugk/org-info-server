package ru.psu.org_info_server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.ServiceException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.Response;

@RestControllerAdvice
public class RestExceptionHandler {
    private ResponseEntity<Response<String>> internalHandling(ServiceException ex, HttpStatus status) {
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

    @ExceptionHandler(UnacceptableParamsException.class)
    protected ResponseEntity<Response<String>> unacceptableParams(UnacceptableParamsException ex){
        return internalHandling(ex, HttpStatus.NOT_ACCEPTABLE);
    }
}
