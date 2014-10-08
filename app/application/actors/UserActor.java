package application.actors;

import akka.actor.UntypedActor;
import application.actors.protocols.UserProtocol;
import application.exceptions.BadRequestException;
import application.exceptions.NotFoundException;
import application.exceptions.UnauthorizedException;
import application.models.AppInstance;
import application.models.GlobalUser;
import application.services.Authorization;
import application.services.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.jpa.JPA;
import play.libs.Json;

/**
 * Created by Danny on 10/1/2014.
 */
public class UserActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof UserProtocol.Login) {
            // Check authorization
            final AppInstance appInstance;
            try {
                appInstance = JPA.withTransaction(() -> Authorization.authorizeInstance(((UserProtocol.Login) message).authString));
                if (appInstance == null) {
                    getSender().tell(new UnauthorizedException("You are not authorized to use this API"), getSelf());
                    return;
                }
            } catch (Throwable e) {
                getSender().tell(e, getSelf());
                return;
            }

            // Parse data
            if (((UserProtocol.Login) message).jsonBody == null) {
                getSender().tell(new BadRequestException("Expecting JSON data"), getSelf());
                return;
            }
            String email = ((UserProtocol.Login) message).jsonBody.findPath("email").textValue();
            if (email == null) {
                getSender().tell(new BadRequestException("No email data"), getSelf());
                return;
            }

            // Login
            try {
                GlobalUser user;
                user = JPA.withTransaction(() -> UserService.login(appInstance, email));
                if (user == null) {
                    getSender().tell(new NotFoundException("User not found"), getSelf());
                    return;
                }
                ObjectNode result = Json.newObject();
                result.put("user_uuid", user.getUuid());
                System.out.println("Operation successful");

                getSender().tell(result, getSelf());
            } catch (Throwable e) {
                getSender().tell(e, getSelf());
            }
        } else if (message instanceof UserProtocol.Logout) {
            // Check authorization
            final AppInstance appInstance;
            try {
                appInstance = JPA.withTransaction(() -> Authorization.authorizeInstance(((UserProtocol.Logout) message).authString));
                if (appInstance == null) {
                    getSender().tell(new UnauthorizedException("You are not authorized to use this API"), getSelf());
                    return;
                }
            } catch (Throwable e) {
                getSender().tell(e, getSelf());
                return;
            }

            // Logout
            try {
                boolean loggedOut = JPA.withTransaction(() -> UserService.logout(appInstance));
                getSender().tell(loggedOut, getSelf());
            } catch (Throwable e) {
                getSender().tell(e, getSelf());
            }
        }
    }
}
