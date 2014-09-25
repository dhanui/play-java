package application.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Danny on 9/25/2014.
 */
@Entity(name = "device_instance")
public class DeviceInstance implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "device_model_id")
    private DeviceModel deviceModel;
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;
    @Column(name = "device_push_token", nullable = false, length = 100)
    private String devicePushToken;
    @Column(name = "os_type", nullable = false, length = 100)
    private String osType;
    @Column(name = "os_version", nullable = false, length = 100)
    private String osVersion;

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDevicePushToken(String devicePushToken) {
        this.devicePushToken = devicePushToken;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public long getId() {
        return id;
    }

    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDevicePushToken() {
        return devicePushToken;
    }

    public String getOsType() {
        return osType;
    }

    public String getOsVersion() {
        return osVersion;
    }
}
