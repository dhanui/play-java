package application.actors.protocols;

import application.models.AppInstance;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * Created by Danny on 10/1/2014.
 */
public class UserProtocol {
    public static class Login implements Serializable {
        public final String authString;
        public final JsonNode jsonBody;

        public Login(String authString, JsonNode jsonBody) {
            this.authString = authString;
            this.jsonBody = jsonBody;
        }
    }

    public static class Logout implements Serializable {
        public final String authString;

        public Logout(String authString) {
            this.authString = authString;
        }
    }
}
