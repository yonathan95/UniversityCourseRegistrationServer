package bgu.spl.net;

public class AckMessage {
    private short Opcode = 12;
    private short MessageOpcode;
    private String str;
    public AckMessage(short _MessageOpcode, String _str){
        MessageOpcode = _MessageOpcode;
        str =_str;
    }

    public short getOpcode() {
        return Opcode;
    }

    public short getMessageOpcode() {
        return MessageOpcode;
    }

    public String getStr() {
        return str;
    }
}
