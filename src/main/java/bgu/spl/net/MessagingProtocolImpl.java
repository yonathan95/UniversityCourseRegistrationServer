package bgu.spl.net;

import bgu.spl.net.api.MessagingProtocol;

public class MessagingProtocolImpl<T> implements MessagingProtocol<T> {
    private boolean shouldTerminate;

    public MessagingProtocolImpl(Database database){
        shouldTerminate = false;
    }

    @Override
    public T process(T msg) {
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
