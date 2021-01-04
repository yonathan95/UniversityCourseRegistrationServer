package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.impl.BGRSServer.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        int port = Integer.parseInt(args[0]);
        int threadNum = Integer.parseInt(args[1]);
        database.initialize("./Courses.txt");
        Server.reactor(
                threadNum,
                port, //port
                () ->  new MessagingProtocolImpl(database), //protocol factory
                () -> new MessageEncoderDecoderImpl<>()//message encoder decoder factory
        ).serve();
    }
}
