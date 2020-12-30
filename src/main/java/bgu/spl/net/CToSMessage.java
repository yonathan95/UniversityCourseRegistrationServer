package bgu.spl.net;

import java.util.Vector;

public class CToSMessage {
    private short Opcode;
    private Vector<String>  stringData ;
    private short numberData;

    public CToSMessage(){
        Opcode = 0;
        stringData = new Vector<>();
        numberData =  -1;
    }
    public short getNumberData() {
        return numberData;
    }

    public short getOpcode() {
        return Opcode;
    }

    public Vector<String> getStringData() {
        return stringData;
    }

    public void setOpcode(short opcode) {
        Opcode = opcode;
    }

    public void addToStringData(String str){
        stringData.add(str);
    }
    public void setNumberData(short num){
        numberData = num;
    }


}
