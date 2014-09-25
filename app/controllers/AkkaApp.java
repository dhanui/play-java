package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import application.actors.HelloActor;
import application.actors.protocols.HelloActorProtocol;
import play.libs.Akka;
import play.libs.F.*;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

import static akka.pattern.Patterns.ask;

/**
 * Created by Danny on 9/25/2014.
 */
public class AkkaApp extends Controller {

    public static Promise<Result> sayHello(String name) {
        ActorRef helloActor = Akka.system().actorOf(HelloActor.props);
        return Promise.wrap(ask(helloActor, new HelloActorProtocol.SayHello(name), 10000)).map(
                new Function<Object, Result>() {
                    @Override
                    public Result apply(Object o) throws Throwable {
                        return ok((String)o);
                    }
                }
        );
    }
}
