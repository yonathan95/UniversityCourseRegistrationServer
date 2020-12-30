package bgu.spl.net;

import bgu.spl.net.api.MessagingProtocol;

import java.util.HashMap;

public class MessagingProtocolImpl<T> implements MessagingProtocol<bgu.spl.net.ProcessingMessage> {
    private boolean shouldTerminate;
    private Database database;
    private HashMap<Short,Instruction<bgu.spl.net.ProcessingMessage>> instructions;


    public MessagingProtocolImpl(Database _database){
        shouldTerminate = false;
        database = _database;
        instructions.put(Consts.ADMINREG,(database, msg)->{return null;});
    }

    @Override
    public ProcessingMessage process(ProcessingMessage msg){
        return instructions.get(msg.opCode()).process(database,msg);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
