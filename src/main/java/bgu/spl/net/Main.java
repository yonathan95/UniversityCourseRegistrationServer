package bgu.spl.net;

import bgu.spl.net.impl.newsfeed.NewsFeed;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class Main {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        database.initialize(args[0]);
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () ->  new MessagingProtocolImpl(database), //protocol factory
                () -> new MessageEncoderDecoderImpl<>()//message encoder decoder factory
        ).serve();
    }
}
