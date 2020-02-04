package pl.seb.czech.library.service.exceptions;

public class DataAlreadyFoundException extends RuntimeException {

    public DataAlreadyFoundException() {
    }

    public DataAlreadyFoundException(String message) {
        super(message);
    }
}
