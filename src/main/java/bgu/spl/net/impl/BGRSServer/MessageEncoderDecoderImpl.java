package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.impl.BGRSServer.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGRSServer.api.OpMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<OpMessage<Short>>{
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private RegisterLoginMessage registerLoginMessage = new RegisterLoginMessage();
    private StudentStatMessage studentStatMessage = new StudentStatMessage();
    private CourseNumberMessage courseNumberMessage = new CourseNumberMessage();
    private OpMessage message;
    private short opCode = Consts.NOT_DECODED_YET;
    private int numberOfZeros = 0;
    private boolean endOfMessage = false;
    private final short[] opCodeOfTwoStringMessage ={Consts.ADMINREG,Consts.STUDENTREG,Consts.LOGIN};
    private final short[] opCodeOfOneStringMessage ={Consts.STUDENTSTAT};
    private final short[] opCodeOfOneShortMessage = {Consts.COURSEREG,Consts.KDAMCHECK,Consts.COURSESTAT,Consts.ISREGISTERED,Consts.UNREGISTER};
    private boolean firstZero = true;

    @Override
    public OpMessage decodeNextByte(byte nextByte) {
        if(opCode == Consts.NOT_DECODED_YET){
            if(len == 1){
                pushByte(nextByte);
                opCode = bytesToShort(bytes);
                len = 0;
                if(opCode == Consts.LOGOUT | opCode == Consts.MYCOURSES) {
                    message = new LogoutMyCoursesMessages(opCode);
                    opCode = Consts.NOT_DECODED_YET;
                    return message;
                }
                return null;
            }
        }
        else{
            if(contains(opCodeOfTwoStringMessage, opCode)){
                decodeNextByteTwoStringsMessage(nextByte);
                if(numberOfZeros == 1 & (firstZero)){
                    firstZero = false;
                    return null;
                }
            }
            else if(contains(opCodeOfOneStringMessage, opCode)){
                decodeNextByteOneStringMessage(nextByte);
            }
            else if(contains(opCodeOfOneShortMessage, opCode)){
                decodeNextByteOneIntMessage(nextByte);
            }
        }
        if(!endOfMessage){
            pushByte(nextByte);
            return null;
        }
        else{
            clear();
            return message;
        }
    }
    private void decodeNextByteTwoStringsMessage(byte nextByte){
        if(nextByte == '\0'){
            numberOfZeros++;
            if(numberOfZeros == 2) {
                registerLoginMessage.setPassword(popString());
                registerLoginMessage.setOpcode(opCode);
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
        if(len == 1){
            pushByte(nextByte);
            courseNumberMessage.setCourseNumber(bytesToShort(bytes));
            courseNumberMessage.setOpcode(opCode);
            message = courseNumberMessage;
            len = 0;
            endOfMessage = true;
        }
    }

    @Override
    public byte[] encode(OpMessage message) {
        if(message.getOpcode().equals(Consts.ACK)){
            AckMessage msg = (AckMessage) message;
            byte [] opCodeBytes = shortToBytes(msg.getOpcode());
            byte [] messageOpcodeBytes = shortToBytes(msg.getMessageOpcode());
            byte [] stringToBePrintedBytes = (msg.getStr() + "\0").getBytes();
            return append(opCodeBytes,append(messageOpcodeBytes,stringToBePrintedBytes));

        }
        else{
            ErrorMessage err = (ErrorMessage) message;
            byte [] opcodeBytes = shortToBytes(err.getOpcode());
            byte [] messageOpcodeBytes = shortToBytes(err.getMessageOpcode());
            return append(opcodeBytes,messageOpcodeBytes);
        }
    }

    private byte[] append(byte[] one,byte [] two ){
        byte[] allByteArray = new byte[one.length + two.length ];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(one);
        buff.put(two);
        return buff.array();
    }

    private String popString(){
        String result = new String(bytes,0,len, StandardCharsets.UTF_8);
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
        for (short value : shortArr) {
            if (value == Opcode) {
                return true;
            }
        }
        return false;
    }

    private void clear(){
        len = 0;
        registerLoginMessage = new RegisterLoginMessage();
        studentStatMessage = new StudentStatMessage();
        courseNumberMessage = new CourseNumberMessage();
        opCode = Consts.NOT_DECODED_YET;
        numberOfZeros = 0;
        endOfMessage = false;
        firstZero = true;
    }
}
