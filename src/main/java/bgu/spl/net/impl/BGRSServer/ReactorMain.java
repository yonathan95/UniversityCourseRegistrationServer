package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.Database;
import bgu.spl.net.MessageEncoderDecoderImpl;
import bgu.spl.net.MessagingProtocolImpl;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        database.initialize(args[1]);
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                (Integer.getInteger(args[0])), //port
                () ->  new MessagingProtocolImpl(database), //protocol factory
                () -> new MessageEncoderDecoderImpl<>()//message encoder decoder factory
        ).serve();
    }
}
