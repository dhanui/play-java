package application.services;

import application.models.AppInstance;
import application.models.AppInstanceUser;
import application.models.GlobalUser;
import application.models.IcebergUser;
import org.apache.commons.lang3.RandomStringUtils;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.Date;

/**
 * Created by Danny on 10/1/2014.
 */
public class UserService {
    public static GlobalUser registerUser(AppInstance appInstance, String email, String firstName, String lastName,
                                          Date dateOfBirth, String gender) throws Exception {
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

    public static GlobalUser login(AppInstance appInstance, String email) throws Exception {
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

    public static boolean logout(AppInstance appInstance) throws Exception {
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
}
