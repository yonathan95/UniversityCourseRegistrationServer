package bgu.spl.net;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.OpMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class MessageEncoderDecoderImpl <T>  implements MessageEncoderDecoder<OpMessage>{
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private RegisterLoginMessage registerLoginMessage = new RegisterLoginMessage();
    private StudentStatMessage studentStatMessage = new StudentStatMessage();
    private CourseNumberMessage courseNumberMessage = new CourseNumberMessage();
    private OpMessage message;
    private short Opcode = Consts.NOT_DECODE_YET;
    private int numberOfZero = 0;
    private boolean endOfMessage = false;
    private String [] stringArr = new String[2];
    private short[] OpcodeOfTwoStringMessage ={Consts.ADMINREG,Consts.STUDENTREG,Consts.LOGIN};
    private short[] OpcodeOfOneStringMessage ={Consts.STUDENTSTAT};
    private short[] OpcodeOfOneShortMessage = {Consts.COURSEREG,Consts.KDAMCHECK,Consts.COURSESTAT,Consts.ISREGISTERED,Consts.UNREGISTER};
    private boolean firstZero = true;



    @Override
    public OpMessage decodeNextByte(byte nextByte) {
        if(Opcode == Consts.NOT_DECODE_YET){
            if(len == 1){
                pushByte(nextByte);
                Opcode = bytesToShort(bytes);
                len = 0;
                if(Opcode == Consts.LOGOUT | Opcode == Consts.MYCOURSES) {
                    message = new LogoutMyCoursesMessages(Opcode);
                    Opcode = Consts.NOT_DECODE_YET;
                    return message;
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
            Opcode = Consts.NOT_DECODE_YET;
            return message;
        }

    }
    private void decodeNextByteTwoStringMessage(byte nextByte){
        if(nextByte == '\0'){
            numberOfZero++;
            if(numberOfZero == 2) {
                registerLoginMessage.setPassword(popString());
                registerLoginMessage.setOpcode(Opcode);
                message = registerLoginMessage;
                endOfMessage = true;
            }
            else{
                registerLoginMessage.setUserName(popString());
            }
        }
    }

    private void decodeNextByteOneStringMessage(byte nextByte){
        if(nextByte == '\0'){
            studentStatMessage.setUserName(popString());
            message = studentStatMessage;
            endOfMessage = true;
        }
    }

    private void decodeNextByteOneIntMessage(byte nextByte){
        if(len == 2){
            pushByte(nextByte);
            bytes[0] = bytes[1];
            bytes[1] = bytes[2];
            courseNumberMessage.setCourseNumber(bytesToShort(bytes));
            courseNumberMessage.setOpcode(Opcode);
            message = courseNumberMessage;
            len = 0;
            endOfMessage = true;
        }
    }

    @Override
    public byte[] encode(OpMessage message) {
        if(message.getOpcode().equals(Consts.ACK)){
            AckMessage msg = (AckMessage) message;
            byte [] OpcodeBytes = shortToBytes(msg.getOpcode());
            byte [] MessageOpcodeBytes = shortToBytes(msg.getMessageOpcode());
            byte [] stringTobePrintedBytes = (msg.getStr() + "\0").getBytes(); // TODo to add Space byte between the bytes..
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

    private short bytesToShort (byte[] byteArr){
        short result = (short)((byteArr[0] & 0Xff) << 8);
        result += (short)(byteArr[1] & 0Xff);
        return result;
    }

    private byte[] shortToBytes(short num){
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
