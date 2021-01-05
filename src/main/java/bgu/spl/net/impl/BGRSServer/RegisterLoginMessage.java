package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class RegisterLoginMessage implements OpMessage<Short> {
    private Short Opcode;
    private String userName;
    private String password;



    public RegisterLoginMessage() {
        Opcode = -1;
        userName = null;
        password = null;
    }

    public void setOpcode(short opcode) {
        Opcode = opcode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
    public Short getOpcode() {
        return Opcode;
    }

}
