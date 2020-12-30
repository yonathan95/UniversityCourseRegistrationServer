package bgu.spl.net;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;



public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<T>{
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private CToSMessage output = new CToSMessage();
    private short Opcode = -1;
    private int numberOfZero = 0;
    private boolean endOfMessage = false;
    private short[] OpcodeOfTwoStringMessage ={Consts.ADMINREG,Consts.STUDENTREG,Consts.LOGIN};
    private short[] OpcodeOfOneStringMessage ={Consts.STUDENTSTAT};
    private short[] OpcodeOfOneShortMessage = {Consts.COURSEREG,Consts.KDAMCHECK,Consts.COURSESTAT,Consts.ISREGISTERED,Consts.UNREGISTER};
    private boolean firstZero = true;


    @Override
    public T decodeNextByte(byte nextByte) {
        if(Opcode == -1){
            if(len == 1){
                pushByte(nextByte);
                Opcode = bytesToShort(bytes);
                len = 0;
                output.setOpcode(Opcode);
                if(Opcode == Consts.LOGOUT | Opcode == Consts.MYCOURSES) {
                    return null;
                }
                return null;
            }
        }
        else{
            if(contains(OpcodeOfTwoStringMessage,Opcode)){
                decodeNextByteTwoStringMessage(nextByte);
                if(numberOfZero == 1 & (firstZero)){
                    firstZero = false;
                    return null;
                }
            }
            else if(contains(OpcodeOfOneStringMessage,Opcode)){
                decodeNextByteOneStringMessage(nextByte);
            }
            else if(contains(OpcodeOfOneShortMessage,Opcode)){
                decodeNextByteOneIntMessage(nextByte);
            }
        }
        if(!endOfMessage){
            pushByte(nextByte);
            return null;
        }
        else{
            return null;
        }

    }
    private void decodeNextByteTwoStringMessage(byte nextByte){
        if(nextByte == '\0'){
            numberOfZero++;
            output.addToStringData(popString());
            if(numberOfZero == 2) {
                endOfMessage = true;
            }
        }
    }

    private void decodeNextByteOneStringMessage(byte nextByte){
        if(nextByte == '\0'){
            output.addToStringData(popString());
            endOfMessage = true;
        }
    }

    private void decodeNextByteOneIntMessage(byte nextByte){
        if(len == 2){
            pushByte(nextByte);
            bytes[0] = bytes[1];
            bytes[1] = bytes[2]; // TODO dell comnnt , did this so the space byte will be dell.
            output.setNumberData(bytesToShort(bytes));
            endOfMessage = true;
        }
    }


    @Override
    public byte[] encode(T message) {
        if(Opcode == Consts.ACK){
            AckMessage msg = (AckMessage) message;
            byte [] OpcodeBytes = shortToBytes(msg.getOpcode());
            byte [] MessageOpcodeBytes = shortToBytes(msg.getMessageOpcode());
            byte [] stringTobePrintedBytes = (msg.getStr() + "\0").getBytes(); // TODo to add Space bye between the bytes..
            return append(OpcodeBytes,append(MessageOpcodeBytes,stringTobePrintedBytes));

        }
        else{
            ErrorMessage err = (ErrorMessage) message;
            byte [] OpcodeBytes = shortToBytes(err.getOpcode());
            byte [] MessageOpcodeBytes = shortToBytes(err.getMessageOpcode()); // TODO add space byte between the arryes
            return append(OpcodeBytes,MessageOpcodeBytes);
        }

    }

    private byte[] append(byte[] one,byte [] two ){
        byte[] allByteArray = new byte[one.length + two.length ];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(one);
        buff.put(two);
        byte[] combined = buff.array();
        return combined;
    }

    private String popString(){
        String result = new String(bytes,1,len, StandardCharsets.UTF_8); // TOdo DELL THE COOMENT start from one so the we wont get the space to the strings
        len = 0;
        return result;
    }

     private void pushByte(byte nextByte){
        if(len >= bytes.length){
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
     }

    public short bytesToShort (byte[] byteArr){
        short result = (short)((byteArr[0] & 0Xff) << 8);
        result += (short)(byteArr[1] & 0Xff);
        return result;
    }

    public byte[] shortToBytes(short num){
        byte[] bytesArr =  new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private boolean contains (short[] shortArr,short Opcode){
        for (short i = 0; i <shortArr.length ; i++){
            if(shortArr[i] == Opcode){
                return true;
            }
        }
        return false;
    }
}
