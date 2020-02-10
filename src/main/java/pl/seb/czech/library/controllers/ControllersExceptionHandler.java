package pl.seb.czech.library.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.seb.czech.library.service.exceptions.BookException;
import pl.seb.czech.library.service.exceptions.DataAlreadyFoundException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.service.exceptions.RentException;

@Slf4j
@ControllerAdvice
public class ControllersExceptionHandler {
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public void handleNotFound(DataNotFoundException e) {
        log.error(e.getMessage(), e);
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataAlreadyFoundException.class)
    public void handleAlreadyFound(DataAlreadyFoundException e) {
        log.error(e.getMessage(), e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RentException.class)
    public void handleRentException(RentException e) {
        log.error(e.getMessage(), e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BookException.class)
    public void handleBookException(BookException e) {
        log.error(e.getMessage(), e);
    }
    
    
}
