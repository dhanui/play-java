package application.exceptions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * Created by Danny on 10/2/2014.
 */
public class ResponseException extends Exception {
    private ObjectNode jsonMessage;

    public ResponseException() {
        // do nothing
    }

    public ResponseException(String message) {
        jsonMessage = Json.newObject();
        jsonMessage.put("error", message);
    }

    public ObjectNode getJsonMessage() {
        return jsonMessage;
    }
}
