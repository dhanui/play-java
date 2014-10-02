package application.actors;

import application.exceptions.BadRequestException;
import application.exceptions.UnauthorizedException;
import application.models.App;
import application.models.AppInstance;
import application.services.ApplicationService;
import application.services.Authorization;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.jpa.JPA;
import play.libs.Json;

/**
 * Created by Danny on 10/1/2014.
 */
public class ApplicationActor {
    static public interface Operation {
        Object registerInstance(String authString, JsonNode jsonBody);
    }

    static public class OperationImplementation implements Operation {
        public Object registerInstance(String authString, JsonNode jsonBody) {
            try {
                // Check authorization
                App app = JPA.withTransaction(() -> Authorization.authorizeApp(authString));
                if (app == null) {
                    return new UnauthorizedException("You are not authorized to use this API");
                }

                // Parse request
                if (jsonBody == null) {
                    return new BadRequestException("Expecting JSON data");
                }
                String deviceType = jsonBody.findPath("device_type").textValue();
                String deviceOs = jsonBody.findPath("device_os").textValue();
                String udid = jsonBody.findPath("device_uuid").textValue();
                String pushToken = jsonBody.findPath("push_token").textValue();

                // Register instance
                AppInstance appInstance = JPA.withTransaction(() ->
                        ApplicationService.registerInstance(app, deviceType, deviceOs, udid, pushToken));
                ObjectNode result = Json.newObject();
                result.put("access_token", appInstance.getAccessToken());
                System.out.println("Operation successful");

                return result;
            } catch (Throwable e) {
                return e;
            }
        }
    }
}
