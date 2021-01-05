package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.api.OpMessage;

public class CourseNumberMessage implements OpMessage<Short> {

    private Short opCode;
    private int courseNumber;

    /**
     * CourseNumberMessage constructor.
     */
    public CourseNumberMessage(){
        opCode = null;
        courseNumber = -1;
    }

    /**
     * CourseNumberMessage opcode getter.
     * @return opcode of an CourseNumberMessage message.
     */
    public Short getOpcode() {
        return opCode;
    }

    /**
     * CourseNumberMessage course number getter.
     * @return the course number asked about at a CourseNumberMessage message.
     */
    public int getCourseNumber() {
        return courseNumber;
    }

    /**
     * CourseNumberMessage opcode setter.
     * @param opcode- opcode to  be set as new opcode to the message.
     */
    public void setOpcode(Short opcode) {
        opCode = opcode;
    }

    /**
     * CourseNumberMessage course number getter..
     * @param  courseNumber - course number to be set as a new course number to the message.
     */
    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }
}
