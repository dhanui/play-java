package application.exceptions;

/**
 * Created by Danny on 10/2/2014.
 */
public class BadRequestException extends ResponseException {
    public BadRequestException() {
        // do nothing
    }

    public BadRequestException(String message) {
        super(message);
    }
}
