package bgu.spl.net;

public class RegisterLoginMessage {
    private String userName;
    private String password;

    public RegisterLoginMessage() {
        super();
        userName = null;
        password = null;
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

}
