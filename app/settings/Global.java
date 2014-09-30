package settings;

import akka.actor.ActorSystem;
import akka.actor.Props;
import application.actors.HelloActor;
import com.typesafe.config.ConfigFactory;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

/**
 * Created by Danny on 9/30/2014.
 */
public class Global extends GlobalSettings {
    public static ActorSystem actorSystem;

    @Override
    public void onStart(Application application) {
        actorSystem = ActorSystem.create("HelloSystem", ConfigFactory.load().getConfig("hello"));
        actorSystem.actorOf(Props.create(HelloActor.class), "hello");
    }

    @Override
    public void onStop(Application application) {
        actorSystem.shutdown();
    }
}
