package bgu.spl.net;

public class CourseNumberMessage {
    private Short Opcode;
    private short courseNumber;


    public CourseNumberMessage(Short _Opcode, short _courseNumber){
        Opcode = _Opcode;
        courseNumber = _courseNumber;
    }
    public Short getOpcode() {
        return Opcode;
    }

    public short getCourseNumber() {
        return courseNumber;
    }
}
