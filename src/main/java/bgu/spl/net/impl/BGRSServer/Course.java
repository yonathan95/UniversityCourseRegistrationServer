package bgu.spl.net.impl.BGRSServer;

import java.util.*;

public class Course {
    private final int courseNum;
    private final String courseName;
    private final int[] kdamCoursesList;
    private int numOfMaxStudents;
    private TreeSet<String> registeredStudents;

    /**
     * Course constructor
     * @param _courseNum the course number.
     * @param _courseName- the course namee.
     * @param _kdamCoursesList- a list of kdam courses needed to register for this course.
     * @param _numOfMaxStudents- the number of max student that ca register to this course.
     */
    public Course(int _courseNum, String _courseName, int[] _kdamCoursesList, int _numOfMaxStudents){
        courseNum = _courseNum;
        courseName = _courseName;
        kdamCoursesList = _kdamCoursesList;
        numOfMaxStudents = _numOfMaxStudents;
        registeredStudents = new TreeSet<>();
    }

    /**
     * useed to add a new student user name to the student registered TreeSet.
     * @param studentUsername - student username to be add to the register student TreeSet.
     */
    public synchronized void registerStudent(String studentUsername){
        registeredStudents.add(studentUsername);
    }

    /**
     * course nunmber getter.
     * @return course number.
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * course kdam courses getter.
     * @return kdam courses list needed to register to the course.
     */
    public int[] getKdamCoursesList() {
        return kdamCoursesList;
    }

    /**
     * course number of max student getter.
     * @return max number of student the can be register to the course name.
     */

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }
    /**
     * course registered student getter.
     * @return an TreeSet of the registered student.
     */

    public synchronized TreeSet<String> getRegisteredStudents() {
        return registeredStudents;
    }

    /**
     * Cuseed to remove a student user name from the student registered TreeSet.
     * @param studentUsername - student username to be to be removed from the registered student TreeSet.
     */
    public synchronized void unregisterStudent(String studentUsername) {
        registeredStudents.remove(studentUsername);
    }
}
