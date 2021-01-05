package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class RegisterLoginMessage implements OpMessage<Short> {
    private Short opCode;
    private String userName;
    private String password;


    /**
     * RegisterLoginMessage constructor.
     */
    public RegisterLoginMessage() {
        opCode = -1;
        userName = null;
        password = null;
    }

    /**
     * RegisterLoginMessage opcode setter.
     * @param opcode- opcode to  be set as new opcode to the message.
     */
    public void setOpcode(short opcode) {
        opCode = opcode;
    }

    /**
     * RegisterLoginMessage username setter.
     * @param userName- username to  be set as new username to the client refer to in the message.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * RegisterLoginMessage username setter.
     * @param password- password to  be set as new password to the client refer to in the message.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * RegisterLoginMessage userName getter.
     * @return username of the client message.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * RegisterLoginMessage password getter.
     * @return password of the client.
     */
    public String getPassword() {
        return password;
    }

    /**
     * RegisterLoginMessage opcode getter.
     * @return opcode of a RegisterLoginMessage message.
     */
    public Short getOpcode() {
        return opCode;
    }

}
