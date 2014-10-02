package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import application.actors.UserActor;
import application.actors.protocols.UserProtocol;
import application.exceptions.BadRequestException;
import application.exceptions.NotFoundException;
import application.exceptions.UnauthorizedException;
import application.models.*;
import application.services.Authorization;
import application.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.db.jpa.Transactional;
import play.mvc.*;
import scala.concurrent.Future;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static akka.pattern.Patterns.ask;

/**
 * Created by Danny on 9/25/2014.
 */
public class User extends Controller {

    public static Result index() {
        return noContent();
    }

    @Transactional
    public static Result register() {
        try {
            // Check authorization
            AppInstance appInstance = Authorization.authorizeInstance(request().getHeader("Authorization"));
            if (appInstance == null) {
                return unauthorized();
            }

            JsonNode json = request().body().asJson();
            if (json == null) {
                return badRequest("Expecting JSON data");
            } else {
                // Fetch JSON data
                String email = json.findPath("email").textValue();
                String firstName = json.findPath("first_name").textValue();
                String lastName = json.findPath("last_name").textValue();
                Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(json.findPath("date_of_birth").textValue());
                String gender = json.findPath("gender").textValue();

                GlobalUser user = UserService.registerUser(appInstance, email, firstName, lastName, dateOfBirth, gender);
                ObjectNode result = Json.newObject();
                result.put("user_uuid", user.getUuid());

                return created(result);
            }
        } catch (ParseException e) {
            return badRequest(e.getMessage());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    @Transactional
    public static Result login() {
        try {
            // Check authorization
            AppInstance appInstance = Authorization.authorizeInstance(request().getHeader("Authorization"));
            if (appInstance == null) {
                return unauthorized();
            }

            JsonNode json = request().body().asJson();
            if (json == null) {
                return badRequest("Expeccting JSON data");
            } else {
                // Fetch JSON data
                String email = json.findPath("email").textValue();
                GlobalUser user = UserService.login(appInstance, email);
                if (user == null) {
                    return notFound();
                } else {
                    ObjectNode result = Json.newObject();
                    result.put("user_uuid", user.getUuid());

                    return ok(result);
                }
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public static F.Promise<Result> promiseLogin() {
        ActorRef userActor = Akka.system().actorOf(Props.create(UserActor.class));
        Future<Object> futureResult = ask(userActor, new UserProtocol.Login(request().getHeader("Authorization"),
                request().body().asJson()), 3000);
        System.out.println("Message sent to actor");
        return F.Promise.wrap(futureResult)
                .map((Object o) -> {
                    // Error encountered
                    if (o instanceof UnauthorizedException) {
                        UnauthorizedException e = (UnauthorizedException) o;
                        return unauthorized(e.getJsonMessage());
                    } else if (o instanceof BadRequestException) {
                        BadRequestException e = (BadRequestException) o;
                        return badRequest(e.getJsonMessage());
                    } else if (o instanceof NotFoundException) {
                        NotFoundException e = (NotFoundException) o;
                        return notFound(e.getJsonMessage());
                    } else if (o instanceof Throwable) {
                        Throwable e = (Throwable) o;
                        return internalServerError(e.getMessage());
                    }

                    // Login successful
                    return ok((ObjectNode) o);
                });
    }

    @Transactional
    public static Result logout() {
        try {
            // Check authorization
            AppInstance appInstance = Authorization.authorizeInstance(request().getHeader("Authorization"));
            if (appInstance == null) {
                return unauthorized();
            }

            if (UserService.logout(appInstance)) {
                return noContent();
            }
            else {
                return notFound();
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public static F.Promise<Result> promiseLogout() {
        ActorRef userActor = Akka.system().actorOf(Props.create(UserActor.class));
        return F.Promise.wrap(ask(userActor, new UserProtocol.Logout(request().getHeader("Authorization")), 3000))
                .map((Object o) -> {
                    // Error encountered
                    if (o instanceof UnauthorizedException) {
                        UnauthorizedException e = (UnauthorizedException) o;
                        return unauthorized(e.getJsonMessage());
                    } else if (o instanceof Throwable) {
                        Throwable e = (Throwable) o;
                        return internalServerError(e.getMessage());
                    }

                    // No error encountered
                    if ((boolean) o) {
                        return noContent();
                    } else {
                        return notFound();
                    }
                });
    }
}
