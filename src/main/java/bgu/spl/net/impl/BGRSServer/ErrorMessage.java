package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class ErrorMessage implements OpMessage<Short> {
    private final Short opCode = 13;
    private short MessageOpcode;

    /**
     * Error constructor.
     * @param _messageOpcode the message opcode for which the Error was received.
     */
    public ErrorMessage(short _messageOpcode){
        MessageOpcode = _messageOpcode;
    }

    /**
     * Error message opcode getter.
     * @return message opcode for which the Error message was send.
     */
    public short getMessageOpcode() {
        return MessageOpcode;
    }

    /**
     * Error opcode getter.
     * @return opcode of an Error message.
     */
    public Short getOpcode() {
        return opCode;
    }
}
