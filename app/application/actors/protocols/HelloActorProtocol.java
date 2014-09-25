package application.actors.protocols;

/**
 * Created by Danny on 9/25/2014.
 */
public class HelloActorProtocol {
    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }
}
