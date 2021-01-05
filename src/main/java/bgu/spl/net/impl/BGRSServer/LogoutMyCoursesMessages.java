package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class LogoutMyCoursesMessages implements OpMessage<Short> {
    private final Short opCode;

    /**
     * LogoutMyCoursesMessages constructor.
     * @param _opCode- the opcode of the message.
     */
    public LogoutMyCoursesMessages(short _opCode){
        opCode = _opCode;
    }

    /**
     * LogoutMyCoursesMessages opcode getter.
     * @return opcode of an LogoutMyCoursesMessages message.
     */
    public Short getOpcode(){
        return opCode;
    }
}
