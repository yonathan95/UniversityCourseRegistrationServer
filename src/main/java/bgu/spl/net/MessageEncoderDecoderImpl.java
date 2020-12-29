package bgu.spl.net;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder{
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private CToSMessage output = new CToSMessage();
    private short Opcode = -1;
    private int numberOfZero = 0;
    private boolean endOfMessage = false;
    @Override
    public Object decodeNextByte(byte nextByte) {
        if(Opcode == -1){
            if(len == 2){
                Opcode = bytesToShort(bytes);
                len = 0;
                output.setOpcode(Opcode);
                return null;
            }
        }
        else{
            if(Opcode == 1 | Opcode == 2 | Opcode == 3){
                decodeNextByteTwoStringMessage(nextByte);
            }
            else if(Opcode == 8){
                decodeNextByteOneStringMessage(nextByte);
            }
            else if(Opcode == 4 | Opcode == 11){
                endOfMessage = true;
            }
            else if((Opcode < 11 & Opcode > 4) & Opcode != 8 ){

                decodeNextByteOneIntMessage(nextByte);
            }
        }
        if(!endOfMessage){
            pushByte(nextByte);
            return null;
        }
        else{
            return output;
        }

    }
    private void decodeNextByteTwoStringMessage(byte nextByte){
        if(nextByte == '\0'){
            numberOfZero ++;
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
            output.setNumberData(bytesToShort(bytes));
            endOfMessage = true;
        }
    }


    @Override
    public byte[] encode(Object message) {
        if(Opcode == 12){
            AckMessage msg = (AckMessage) message;
            byte [] OpcodeBytes = shortToBytes(msg.getOpcode());
            byte [] MessageOpcodeBytes = shortToBytes(msg.getMessageOpcode());
            byte [] stringTobePrintedBytes = (msg.getStr() + "\0").getBytes();
            return append(OpcodeBytes,append(MessageOpcodeBytes,stringTobePrintedBytes));

        }
        else{
            ErrorMessage err = (ErrorMessage) message;
            byte [] OpcodeBytes = shortToBytes(err.getOpcode());
            byte [] MessageOpcodeBytes = shortToBytes(err.getMessageOpcode());
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
}
