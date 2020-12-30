package bgu.spl.net;

public class ErrorMessage {


    private short Opcode = 13;
    private short MessageOpcode;
    public ErrorMessage(short _MessageOpcode){
        MessageOpcode = _MessageOpcode;
    }

    public short getMessageOpcode() {
        return MessageOpcode;
    }
    public short getOpcode() {
        return Opcode;
    }
}
