package controllers;

import application.models.AppInstance;
import application.models.DeviceInstance;
import application.models.DeviceModel;
import application.services.Authorization;
import com.fasterxml.jackson.databind.JsonNode;
import application.models.App;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;

public class Application extends Controller {

    private static AppInstance registerInstance(App app, String deviceType, String deviceOs, String deviceId, String pushToken) {
        String manufacturer = deviceType.split(";")[0];
        String model = deviceType.split(";")[1];
        String osType = deviceOs.split(";")[0];
        String osVersion = deviceOs.split(";")[1];

        // Find device model
        DeviceModel deviceModel;
        TypedQuery<DeviceModel> query = JPA.em().createQuery("SELECT dm FROM device_model dm " +
                "WHERE dm.manufacturer = :manufacturer AND dm.model = :model", DeviceModel.class);
        try{
            query.setParameter("manufacturer", manufacturer);
            query.setParameter("model", model);
            deviceModel = query.getSingleResult();
        } catch (NoResultException e) {
            // Create new device model
            deviceModel = new DeviceModel();
            deviceModel.setManufacturer(manufacturer);
            deviceModel.setModel(model);
            JPA.em().persist(deviceModel);
        }

        // Create new device instance
        DeviceInstance deviceInstance = new DeviceInstance();
        deviceInstance.setDeviceId(deviceId);
        deviceInstance.setDeviceModel(deviceModel);
        deviceInstance.setDevicePushToken(pushToken);
        deviceInstance.setOsType(osType);
        deviceInstance.setOsVersion(osVersion);
        JPA.em().persist(deviceInstance);

        // Create new app instance
        AppInstance appInstance = new AppInstance();
        appInstance.setAccessToken(RandomStringUtils.randomAlphanumeric(50));
        appInstance.setAccessTokenExpiredTime(new Date());
        appInstance.setApp(app);
        appInstance.setDeviceInstance(deviceInstance);
        appInstance.setRefreshToken("randomstring");
        JPA.em().persist(appInstance);

        return appInstance;
    }

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
                AppInstance appInstance = registerInstance(app, deviceType, deviceOs, udid, pushToken);
                ObjectNode result = Json.newObject();
                result.put("access_token", appInstance.getAccessToken());
                return created(result);
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

}
