package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class LogoutMyCoursesMessages implements OpMessage<Short> {
    private final Short Opcode;

    public LogoutMyCoursesMessages(short _Opcode){
        Opcode = _Opcode;
    }

    public Short getOpcode(){
        return Opcode;
    }
}
