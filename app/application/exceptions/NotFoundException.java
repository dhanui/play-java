package application.exceptions;

/**
 * Created by Danny on 10/2/2014.
 */
public class NotFoundException extends ResponseException {
    public NotFoundException() {
        // do nothing
    }

    public NotFoundException(String message) {
        super(message);
    }
}
