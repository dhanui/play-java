package application.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Danny on 9/25/2014.
 */
@Entity(name = "app_instance_user")
public class AppInstanceUser implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "app_instance_id")
    private AppInstance appInstance;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private GlobalUser user;

    public void setAppInstance(AppInstance appInstance) {
        this.appInstance = appInstance;
    }

    public void setUser(GlobalUser user) {
        this.user = user;
    }

    public AppInstance getAppInstance() {
        return appInstance;
    }

    public GlobalUser getUser() {
        return user;
    }
}
