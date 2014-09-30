package application.actors;

import akka.actor.UntypedActor;
import application.actors.protocols.HelloActorProtocol;

/**
 * Created by Danny on 9/25/2014.
 */
public class HelloActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof HelloActorProtocol.SayHello) {
            sender().tell("Hello, " + ((HelloActorProtocol.SayHello)message).name, self());
        }
    }
}

