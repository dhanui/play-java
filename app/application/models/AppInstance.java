package application.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 9/24/2014.
 */
@Entity(name = "app_instance")
public class AppInstance implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "access_token", nullable = false, length = 100)
    private String accessToken;
    @Column(name = "access_token_expired_time", nullable = false)
    private Date accessTokenExpiredTime;
    @Column(name = "refresh_token", nullable = false, length = 100)
    private String refreshToken;
    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;
    @ManyToOne
    @JoinColumn(name = "device_instance_id")
    private DeviceInstance deviceInstance;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessTokenExpiredTime(Date accessTokenExpiredTime) {
        this.accessTokenExpiredTime = accessTokenExpiredTime;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setDeviceInstance(DeviceInstance deviceInstance) {
        this.deviceInstance = deviceInstance;
    }

    public long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Date getAccessTokenExpiredTime() {
        return accessTokenExpiredTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public App getApp()
    {
        return app;
    }

    public DeviceInstance getDeviceInstance() {
        return deviceInstance;
    }
}
