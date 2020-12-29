package bgu.spl.net;

import bgu.spl.net.impl.newsfeed.NewsFeed;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class Main {
    public static void main(String[] args) {
        Database d = Database.getInstance();
        boolean b = d.initialize("./Courses.txt");

    }
}
