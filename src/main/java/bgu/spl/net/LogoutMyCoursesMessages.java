package bgu.spl.net;

import bgu.spl.net.api.OpMessage;

public class LogoutMyCoursesMessages implements OpMessage<Short> {
    private short Opcode = -1;

    public LogoutMyCoursesMessages(short _Opcode){
        Opcode = _Opcode;
    }
   public Short getOpcode(){
        return Opcode;
    }
}
