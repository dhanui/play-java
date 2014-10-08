package application.actors;

import akka.actor.UntypedActor;
import application.actors.protocols.HelloProtocol;

/**
 * Created by Danny on 9/25/2014.
 */
public class HelloActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof HelloProtocol.SayHello) {
            sender().tell("Hello, " + ((HelloProtocol.SayHello)message).name, self());
        }
    }
}

