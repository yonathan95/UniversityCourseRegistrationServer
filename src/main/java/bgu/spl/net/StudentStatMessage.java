package bgu.spl.net;

import bgu.spl.net.api.OpMessage;

public class StudentStatMessage implements OpMessage<Short> {

    private Short Opcode = 8;
    private String userName;

    public StudentStatMessage(String _userName){
        userName = _userName;
    }
    public Short getOpcode() {
        return Opcode;
    }

    public String getUserName() {
        return userName;
    }

}