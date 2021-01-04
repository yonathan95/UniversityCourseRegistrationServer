package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.impl.BGRSServer.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        database.initialize("./Courses.txt");
        Server.reactor(
                Integer.getInteger(args[1]),
                (Integer.getInteger(args[0])), //port
                () ->  new MessagingProtocolImpl(database), //protocol factory
                () -> new MessageEncoderDecoderImpl<>()//message encoder decoder factory
        ).serve();
    }
}
