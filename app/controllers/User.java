package controllers;

import application.models.*;
import application.services.Authorization;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import play.libs.Json;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Danny on 9/25/2014.
 */
public class User extends Controller {

    private static GlobalUser registerUser(AppInstance appInstance, String email, String firstName, String lastName, Date dateOfBirth, String gender)
    {
        // Create new user
        GlobalUser user = new GlobalUser();
        user.setUuid(RandomStringUtils.randomAlphanumeric(50));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        JPA.em().persist(user);

        // Add user details
        IcebergUser userDetail = new IcebergUser();
        userDetail.setUser(user);
        userDetail.setEmail(email);
        userDetail.setDateOfBirth(dateOfBirth);
        userDetail.setGender(gender);
        JPA.em().persist(userDetail);

        return user;
    }

    private static GlobalUser login(AppInstance appInstance, String email) {
        IcebergUser userDetail;
        TypedQuery<IcebergUser> query = JPA.em().createQuery("SELECT u from iceberg_user u " +
                "WHERE u.email = :email", IcebergUser.class);
        try {
            query.setParameter("email", email);
            userDetail = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }

        // Link user with app instance
        GlobalUser user = userDetail.getUser();
        AppInstanceUser appInstanceUser = new AppInstanceUser();
        appInstanceUser.setAppInstance(appInstance);
        appInstanceUser.setUser(user);
        JPA.em().persist(appInstanceUser);

        return user;
    }

    private static boolean logout(AppInstance appInstance) {
        AppInstanceUser appInstanceUser;
        TypedQuery<AppInstanceUser> query = JPA.em().createQuery("SELECT aiu from app_instance_user aiu " +
                "WHERE aiu.appInstance = :appInstance", AppInstanceUser.class);
        try {
            query.setParameter("appInstance", appInstance);
            appInstanceUser = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return false;
        }
        JPA.em().remove(appInstanceUser);

        return true;
    }

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

                GlobalUser user = registerUser(appInstance, email, firstName, lastName, dateOfBirth, gender);
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
                GlobalUser user = login(appInstance, email);
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

    @Transactional
    public static Result logout() {
        try {
            // Check authorization
            AppInstance appInstance = Authorization.authorizeInstance(request().getHeader("Authorization"));
            if (appInstance == null) {
                return unauthorized();
            }

            if (logout(appInstance)) {
                return noContent();
            }
            else {
                return notFound();
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
}
