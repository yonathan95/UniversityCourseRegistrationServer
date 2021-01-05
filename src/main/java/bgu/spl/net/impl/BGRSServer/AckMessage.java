package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class AckMessage implements OpMessage<Short> {

    private Short Opcode = 12;
    private short MessageOpcode;
    private String str;

    /**
     * Ack constructor.
     * @param _MessageOpcode the message opcode for which the ack was received.
     * @param _str - a message attached to the ack message to be printed to the clint side.
     */

    public AckMessage(short _MessageOpcode, String _str){
        MessageOpcode = _MessageOpcode;
        str =_str;
    }
    /**
     * Ack opcode getter.
     * @return opcode of an ack message.
     */
    public Short getOpcode() {
        return Opcode;
    }
    /**
     * Ack message opcode getter.
     * @return message opcode for which the ack message was send.
     */
    public short getMessageOpcode() {
        return MessageOpcode;
    }
    /**
     * Ack string getter.
     * @return Ack string.
     */
    public String getStr() {
        return str;
    }
}
