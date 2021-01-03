package bgu.spl.net;

import bgu.spl.net.api.OpMessage;

public class ErrorMessage implements OpMessage<Short> {
    private Short Opcode = 13;
    private short MessageOpcode;

    public ErrorMessage(short _messageOpcode){
        MessageOpcode = _messageOpcode;
    }

    public void setMessageOpcode(short messageOpcode) {
        MessageOpcode = messageOpcode;
    }
    public short getMessageOpcode() {
        return MessageOpcode;
    }
    public Short getOpcode() {
        return Opcode;
    }
}
