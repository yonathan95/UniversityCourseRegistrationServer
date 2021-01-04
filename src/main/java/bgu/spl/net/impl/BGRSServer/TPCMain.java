package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        int port = Integer.parseInt(args[0]);
        database.initialize("./Courses.txt");
        Server.threadPerClient(
                port, //port
                () ->  new MessagingProtocolImpl(database), //protocol factory
                () -> new MessageEncoderDecoderImpl() //message encoder decoder factory
        ).serve();
    }
}
