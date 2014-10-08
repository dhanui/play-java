package controllers;

import akka.actor.TypedActor;
import akka.actor.TypedProps;
import application.actors.ApplicationActor;
import application.exceptions.BadRequestException;
import application.exceptions.UnauthorizedException;
import application.models.AppInstance;
import application.services.ApplicationService;
import application.services.Authorization;
import com.fasterxml.jackson.databind.JsonNode;
import application.models.App;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import scala.concurrent.Future;
import settings.Global;
import views.html.*;

import java.util.concurrent.Callable;

import static akka.dispatch.Futures.future;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    @Transactional
    public static Result register() {
        try {
            // Check authorization
            App app = Authorization.authorizeApp(request().getHeader("Authorization"));
            if (app == null) {
                return unauthorized();
            }

            JsonNode json = request().body().asJson();
            if (json == null) {
                return badRequest("Expecting JSON data");
            } else {
                // Fetch JSON data
                String deviceType = json.findPath("device_type").textValue();
                String deviceOs = json.findPath("device_os").textValue();
                String udid = json.findPath("device_uuid").textValue();
                String pushToken = json.findPath("push_token").textValue();

                // Register instance
                AppInstance appInstance = ApplicationService.registerInstance(app, deviceType, deviceOs, udid, pushToken);
                ObjectNode result = Json.newObject();
                result.put("access_token", appInstance.getAccessToken());
                return created(result);
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public static F.Promise<Result> promiseRegister() {
        // Check authorization
        try {
            String authString = request().getHeader("Authorization");
            JsonNode jsonBody = request().body().asJson();
            ApplicationActor.Operation appOp = TypedActor.get(Akka.system()).typedActorOf(
                    new TypedProps<ApplicationActor.OperationImplementation>
                            (ApplicationActor.Operation.class, ApplicationActor.OperationImplementation.class));
            Future<Object> futureResult = future(() -> appOp.registerInstance(authString, jsonBody),
                    Akka.system().dispatcher());
            System.out.println("Message sent to actor");
            return F.Promise.wrap(futureResult).map((Object o) -> {
                // Error encountered
                if (o instanceof UnauthorizedException) {
                    return unauthorized(((UnauthorizedException)o).getMessage());
                } else if (o instanceof BadRequestException) {
                    return badRequest(((BadRequestException)o).getMessage());
                } else if (o instanceof Throwable) {
                    return internalServerError(((Throwable) o).getMessage());
                }

                // No error encountered
                return ok((ObjectNode)o);
            });
        } catch (Throwable e) {
            return F.Promise.promise(() -> internalServerError(e.getMessage()));
        }
    }

}
