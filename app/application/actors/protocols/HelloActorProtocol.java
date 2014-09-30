package application.actors.protocols;

import java.io.Serializable;

/**
 * Created by Danny on 9/25/2014.
 */
public class HelloActorProtocol {
    public static class SayHello implements Serializable {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }
}
