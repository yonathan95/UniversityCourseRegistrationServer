package bgu.spl.net.impl.BGRSServer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
    private final HashMap<String,String> students;
    private final HashMap<String,String> administrators;
    private final HashMap<String,ArrayList<Integer>> studentCourses;
    private final HashMap<Integer,Course> courses;
    private final ArrayList<String> loggedIn;
    private final ArrayList<Integer> coursesOrder;
    private final ReentrantReadWriteLock studentsReadWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock administratorsReadWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock studentCoursesReadWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock loggedInReadWriteLock = new ReentrantReadWriteLock();
    private final Object registrationLock = new Object();

    public TreeSet<String> getRegisteredStudents(int courseNumber) {
        return courses.get(courseNumber).getRegisteredStudents();
    }

    private static class DatabaseHolder{
        private static Database instance = new Database();
    }
    /**
     * Constructs the only Database instance of this class.
     */
    //to prevent user from creating new Database
    private Database() {
        students = new HashMap<>();
        administrators = new HashMap<>();
        studentCourses= new HashMap<>();
        courses = new HashMap<>();
        loggedIn = new ArrayList<>();
        coursesOrder = new ArrayList<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return DatabaseHolder.instance;
    }

    /**
     * loads the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {
        try {
            ArrayList<String> coursesFile = (ArrayList<String>)Files.readAllLines(Paths.get(coursesFilePath));
            for (int i = 0; i < coursesFile.size(); ++i){
                String[] line = coursesFile.get(i).split("\\|");
                int  courseNum = Integer.parseInt(line[0]);
                coursesOrder.add(courseNum);
                String courseName = line[1];
                int[] kdamCoursesList;
                if (line[2].equals("[]")){
                    kdamCoursesList = new int[0];
                }
                else{
                    kdamCoursesList = Stream.of(line[2].substring(1,line[2].length()-1).split(",")).mapToInt(Integer::parseInt).toArray();
                }
                int numOfMaxStudents = Integer.parseInt(line[3]);
                Course course = new Course(courseNum, courseName, kdamCoursesList, numOfMaxStudents);
                courses.put(courseNum,course);
            }
        }catch(Exception ignored){
            return false;
        }
        return true;
    }

    /**
     * Registers a student to the system
     */
    public int registerStudent(String studentUsername, String studentPassword){
        synchronized (registrationLock){
            if(isRegisteredToServer(studentUsername) == Consts.IS_REGISTERED){
                return Consts.IS_REGISTERED;
            }
            studentsReadWriteLock.writeLock().lock();
            students.put(studentUsername,studentPassword);
            studentsReadWriteLock.writeLock().unlock();
            studentCoursesReadWriteLock.writeLock().lock();
            studentCourses.put(studentUsername, new ArrayList<Integer>());
            studentCoursesReadWriteLock.writeLock().unlock();
        }
        return Consts.REGISTERED_STUDENT_SUCCESSFULLY;
    }

    /**
     * Registers administrator to the system
     */
    public int registerAdministrator(String administratorUsername, String administratorPassword){
        synchronized (registrationLock){
            if(isRegisteredToServer(administratorUsername) == Consts.IS_REGISTERED){
                return Consts.IS_REGISTERED;
            }
            administratorsReadWriteLock.writeLock().lock();
            administrators.put(administratorUsername,administratorPassword);
            administratorsReadWriteLock.writeLock().unlock();
        }
        return Consts.REGISTERED_ADMINISTRATOR_SUCCESSFULLY;
    }

    /**
     * Checks if the user is Registered
     */
    public int isRegisteredToServer(String username){
        studentsReadWriteLock.readLock().lock();
        administratorsReadWriteLock.readLock().lock();
        try{
            if(students.containsKey(username)| administrators.containsKey(username)){
                return Consts.IS_REGISTERED;
            }
            return Consts.NOT_REGISTERED;
        }finally {
            studentsReadWriteLock.readLock().unlock();
            administratorsReadWriteLock.readLock().unlock();
        }

    }

    /**
     * Checks if the user is logged in
     */
    public int isLoggedIn(String username){
        loggedInReadWriteLock.readLock().lock();
        try{
            if(loggedIn.contains(username)){
                return Consts.IS_LOGGED_IN;
            }
            return Consts.NOT_LOGGED_IN;
        }
        finally {
            loggedInReadWriteLock.readLock().unlock();
        }
    }

    /**
     * Login the user if possible
     */
    public int login(String username, String password){
        loggedInReadWriteLock.writeLock().lock();
        studentsReadWriteLock.readLock().lock();
        administratorsReadWriteLock.readLock().lock();
        try{
            if(isLoggedIn(username) == Consts.IS_LOGGED_IN){
                return Consts.IS_LOGGED_IN;
            }
            if (students.containsKey(username)){
                if (students.get(username).equals(password)) {
                    loggedIn.add(username);
                    return Consts.LOGGED_IN_STUDENT_SUCCESSFULLY;
                } else {
                    return Consts.WRONG_PASSWORD;
                }
            }
            else if (administrators.containsKey(username)){
                    if (administrators.get(username).equals(password)) {
                        loggedIn.add(username);
                        return Consts.LOGGED_IN_ADMINISTRATOR_SUCCESSFULLY;
                    } else {
                        return Consts.WRONG_PASSWORD;
                    }
            }
            else {
                return Consts.NOT_REGISTERED;
            }
        }finally {
            loggedInReadWriteLock.writeLock().unlock();
            studentsReadWriteLock.readLock().unlock();
            administratorsReadWriteLock.readLock().unlock();


        }
    }

    /**
     * Logout the user if possible.
     */
    public int logout(String username){
        loggedInReadWriteLock.writeLock().lock();
        if (isLoggedIn(username) == Consts.NOT_LOGGED_IN){
            loggedInReadWriteLock.writeLock().unlock();
            return Consts.NOT_LOGGED_IN;
        }
        loggedIn.remove(username);
        loggedInReadWriteLock.writeLock().unlock();
        return Consts.LOGGED_OUT_SUCCESSFULLY;
    }

    /**
     * Check if the user is registered to the kdam courses.
     */
    public int canRegisterToCourse(String studentUsername, Course course){
        studentCoursesReadWriteLock.readLock().lock();
        int [] courseKdams = course.getKdamCoursesList();
        for (int i = 0 ;i < courseKdams.length; ++i){
            if (!studentCourses.get(studentUsername).contains(courseKdams[i])){
                studentCoursesReadWriteLock.readLock().unlock();
                return Consts.DONT_HAVE_KDAMS;
            }
        }
        studentCoursesReadWriteLock.readLock().unlock();
        return Consts.HAVE_KDAMS;
    }

    /**
     * Register the student to the course if possible
     */
    public int registerCourse(String studentUsername, int courseNum){
        loggedInReadWriteLock.readLock().lock();
        if (isLoggedIn(studentUsername) == Consts.NOT_LOGGED_IN){
            loggedInReadWriteLock.readLock().unlock();
            return Consts.NOT_LOGGED_IN;
        }
        loggedInReadWriteLock.readLock().unlock();
        if (isRegisteredToCourse(studentUsername,courseNum) == Consts.IS_REGISTERED_TO_COURSE){
            return Consts.IS_REGISTERED_TO_COURSE;
        }
        if (!courses.containsKey(courseNum)){
            return Consts.NO_SUCH_COURSE;
        }
        Course course = courses.get(courseNum);
        if (course.getNumOfMaxStudents() <= course.getRegisteredStudents().size()){
            return Consts.COURSE_IS_FULL;
        }
        if (canRegisterToCourse(studentUsername,course) == Consts.DONT_HAVE_KDAMS){
            return Consts.DONT_HAVE_KDAMS;
        }
        course.registerStudent(studentUsername);
        studentCoursesReadWriteLock.writeLock().lock();
        studentCourses.get(studentUsername).add(courseNum);
        studentCoursesReadWriteLock.writeLock().unlock();
        return Consts.REGISTERED_COURSE_SUCCESSFULLY;
    }

    /**
     * Returns the kdamCourses list of the course
     */
    public ArrayList<Integer> kdamCheck(int courseNum){
        int [] kdamCourses = courses.get(courseNum).getKdamCoursesList();
        ArrayList<Integer> tmp = new ArrayList<>();
        ArrayList<Integer> output = new ArrayList<>();
        for (int i : kdamCourses){
            tmp.add(i);
        }
        for (int i : coursesOrder){
            if (tmp.contains(i)){
                output.add(i);
            }
        }
        return output;
    }

    /**
     * Returns the course stat
     */
    public String getCourseName(int courseNum){
        return courses.get(courseNum).getCourseName();
    }

    public int getNumOfMaxStudents(int courseNum){
        return courses.get(courseNum).getNumOfMaxStudents();
    }

    public int getRegisteredStudentsSize(int courseNum){
        return courses.get(courseNum).getRegisteredStudents().size();
    }

    /**
     * Checks if a student is registered to a specific course
     */
    public int isRegisteredToCourse(String studentUsername, int courseNum){
        studentCoursesReadWriteLock.readLock().lock();
        try{
            if (studentCourses.get(studentUsername).contains(courseNum)){
                return Consts.IS_REGISTERED_TO_COURSE;
            }
            return Consts.NOT_REGISTERED_TO_COURSE;
        }finally {
            studentCoursesReadWriteLock.readLock().unlock();
        }

    }

    /**
     * Unregisters a student from a specific course if possible
     */
    public int unregisterFromCourse(String studentUsername, int courseNum){
        studentCoursesReadWriteLock.writeLock().lock();
        try{
            if (isRegisteredToCourse(studentUsername,courseNum) == Consts.NOT_REGISTERED_TO_COURSE){
                return Consts.NOT_REGISTERED_TO_COURSE;
            }
            studentCourses.get(studentUsername).remove(studentCourses.get(studentUsername).indexOf(courseNum));
            courses.get(courseNum).unregisterStudent(studentUsername);
            return Consts.UNREGISTERED_FROM_COURSE_SUCCESSFULLY;
        }finally {
            studentCoursesReadWriteLock.writeLock().unlock();
        }
    }

    /**
     * Returns the courses the student is registered to
     */
    public ArrayList<Integer> getStudentCourses(String studentUsername){
        studentCoursesReadWriteLock.readLock().lock();
        try{
            ArrayList<Integer> registeredCourses = studentCourses.get(studentUsername);
            if (registeredCourses == null){
                return new ArrayList<>();
            }
            ArrayList<Integer> output = new ArrayList<>();
            for (int i : coursesOrder){
                if (registeredCourses.contains(i)){
                    output.add(i);
                }
            }
            return output;
        }finally {
            studentCoursesReadWriteLock.readLock().unlock();
        }

    }


}