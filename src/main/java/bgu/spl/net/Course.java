package bgu.spl.net;

import java.util.*;

public class Course {
    private final int courseNum;
    private final String courseName;
    private final int[] kdamCoursesList;
    private int numOfMaxStudents;
    private TreeSet<String> registeredStudents;

    public Course(int _courseNum, String _courseName, int[] _kdamCoursesList, int _numOfMaxStudents){
        courseNum = _courseNum;
        courseName = _courseName;
        kdamCoursesList = _kdamCoursesList;
        numOfMaxStudents = _numOfMaxStudents;
        registeredStudents = new TreeSet<>();
    }

    public synchronized void registerStudent(String studentUsername){
        registeredStudents.add(studentUsername);
    }

    public int getCourseNum() {
        return courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public int[] getKdamCoursesList() {
        return kdamCoursesList;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public synchronized TreeSet<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public synchronized void unregisterStudent(String studentUsername) {
        registeredStudents.remove(studentUsername);
    }
}
