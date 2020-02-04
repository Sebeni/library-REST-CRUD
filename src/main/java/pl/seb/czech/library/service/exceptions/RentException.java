package pl.seb.czech.library.service.exceptions;

public class RentException extends RuntimeException {
    public RentException() {
    }

    public RentException(String message) {
        super(message);
    }
}
