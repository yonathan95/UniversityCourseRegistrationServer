package bgu.spl.net;

import bgu.spl.net.api.OpMessage;

public class AckMessage implements OpMessage<Short> {

    private Short Opcode = 12;
    private short MessageOpcode;
    private String str;



    public AckMessage(short _MessageOpcode, String _str){
        MessageOpcode = _MessageOpcode;
        str =_str;
    }
    public AckMessage(){
        MessageOpcode = -1;
        str = null;
    }

    public Short getOpcode() {
        return Opcode;
    }

    public short getMessageOpcode() {
        return MessageOpcode;
    }

    public String getStr() {
        return str;
    }
    public void setMessageOpcode(short messageOpcode) {
        MessageOpcode = messageOpcode;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
