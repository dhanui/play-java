package controllers;

import akka.actor.ActorSelection;
import application.actors.protocols.HelloProtocol;
import play.libs.F.*;
import play.mvc.Controller;
import play.mvc.Result;
import settings.Global;

import static akka.pattern.Patterns.ask;

/**
 * Created by Danny on 9/25/2014.
 */
public class AkkaApp extends Controller {

    public static Promise<Result> sayHello(String name) {
        ActorSelection helloActor = Global.actorSystem.actorSelection("user/hello");
        return Promise.wrap(ask(helloActor, new HelloProtocol.SayHello(name), 10000)).map(
                new Function<Object, Result>() {
                    @Override
                    public Result apply(Object o) throws Throwable {
                        return ok((String)o);
                    }
                }
        );
    }
}
