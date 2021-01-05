package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class StudentStatMessage implements OpMessage<Short> {

    private final Short Opcode = 8;
    private String userName;

    /**
     * StudentStatMessage constructor.
     */
    public StudentStatMessage(){
        userName = null;
    }

    /**
     * StudentStatMessage opcode getter.
     * @return opcode of a StudentStatMessage message.
     */
    public Short getOpcode() {
        return Opcode;
    }

    /**
     * StudentStatMessage username getter.
     * @return username of the student for whom the stats was asked in the message.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * StudentStatMessage username setter.
     * @param userName- username to  be set as new username to the client refer to in the message.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
