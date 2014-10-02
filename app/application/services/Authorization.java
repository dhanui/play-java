package application.services;

import application.models.App;
import application.models.AppInstance;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

/**
 * Created by Danny on 9/25/2014.
 */
public class Authorization {
    public static App authorizeApp(String authorization) {
        if (authorization == null) {
            return null;
        } else {
            String apiToken = authorization.split(",")[0].split("Token token=")[1];
            TypedQuery<App> query = JPA.em().createQuery("SELECT a from app a WHERE a.apiKey = :apiKey", App.class);
            try {
                query.setParameter("apiKey", apiToken);
                App app = query.getSingleResult();
                return app;
            } catch (NoResultException | NonUniqueResultException e) {
                return null;
            }
        }
    }

    public static AppInstance authorizeInstance(String authorization) {
        App app = authorizeApp(authorization);
        if (app == null) {
            return null;
        } else {
            String accessToken = authorization.split("Access_Token=")[1];
            TypedQuery<AppInstance> query = JPA.em().createQuery("SELECT ai from app_instance ai " +
                    "WHERE ai.accessToken = :accessToken", AppInstance.class);
            try {
                query.setParameter("accessToken", accessToken);
                AppInstance appInstance = query.getSingleResult();
                return appInstance;
            } catch (NoResultException | NonUniqueResultException e) {
                return null;
            }
        }
    }


}
