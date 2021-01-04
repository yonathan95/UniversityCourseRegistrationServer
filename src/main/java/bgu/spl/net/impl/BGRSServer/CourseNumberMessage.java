package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class CourseNumberMessage implements OpMessage<Short> {

    private Short Opcode;
    private int courseNumber;


    public CourseNumberMessage(Short _Opcode, int _courseNumber){
        Opcode = _Opcode;
        courseNumber = _courseNumber;
    }
    public CourseNumberMessage(){
        Opcode = null;
        courseNumber = -1;
    }
    public Short getOpcode() {
        return Opcode;
    }

    public int getCourseNumber() {
        return courseNumber;
    }
    public void setOpcode(Short opcode) {
        Opcode = opcode;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }
}
