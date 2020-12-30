package bgu.spl.net;

public class ErrorMessage implements OpMessage{


    private short Opcode = 13;
    private short MessageOpcode;
    public ErrorMessage(short _MessageOpcode){
        MessageOpcode = _MessageOpcode;
    }

    public short getMessageOpcode() {
        return MessageOpcode;
    }
    public Short getOpcode() {
        return Opcode;
    }
}
