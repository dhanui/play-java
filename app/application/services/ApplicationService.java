package application.services;

import application.models.App;
import application.models.AppInstance;
import application.models.DeviceInstance;
import application.models.DeviceModel;
import org.apache.commons.lang3.RandomStringUtils;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;

/**
 * Created by Danny on 10/1/2014.
 */
public class ApplicationService {
    public static AppInstance registerInstance(App app, String deviceType, String deviceOs, String deviceId, String pushToken) {
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
}
