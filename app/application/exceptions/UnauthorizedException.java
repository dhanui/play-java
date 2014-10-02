package application.exceptions;

/**
 * Created by Danny on 10/2/2014.
 */
public class UnauthorizedException extends ResponseException {
    public UnauthorizedException() {
        // do nothing
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
