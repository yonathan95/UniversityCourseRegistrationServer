package bgu.spl.net;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.OpMessage;

import java.util.HashMap;

public class MessagingProtocolImpl<T> implements MessagingProtocol<OpMessage> {
    private boolean shouldTerminate;
    private Database database;
    private HashMap<Short,Instruction<OpMessage>> instructions;


    public MessagingProtocolImpl(Database _database){
        shouldTerminate = false;
        database = _database;
        instructions.put(Consts.ADMINREG,(database, msg)->{return null;});
    }

    @Override
    public OpMessage process(OpMessage msg){
        return instructions.get(msg.getOpcode()).process(database,msg);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
