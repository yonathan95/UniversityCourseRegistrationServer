package bgu.spl.net;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.OpMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MessagingProtocolImpl<T> implements MessagingProtocol<OpMessage<Short>> {
    private boolean shouldTerminate;
    private Database database;
    private HashMap<Short,Instruction<OpMessage<Short>>> instructions;
    private String username;
    private boolean isAdmin;
    private boolean isLoggedIn = false;

    public MessagingProtocolImpl(Database _database){
        shouldTerminate = false;
        database = _database;
        isAdmin = false;
        instructions = new HashMap<>();
        instructions.put(Consts.ADMINREG,(database, msg)->{
            RegisterLoginMessage message = (RegisterLoginMessage)msg;
            if (database.registerAdministrator(message.getUserName(),message.getPassword()) == Consts.REGISTERED_ADMINISTRATOR_SUCCESSFULLY){
                return new AckMessage(message.getOpcode(),"");
            }
            else return new ErrorMessage(message.getOpcode());
        });
        instructions.put(Consts.STUDENTREG,(database, msg)->{
            RegisterLoginMessage message = (RegisterLoginMessage)msg;
            if (database.registerStudent(message.getUserName(),message.getPassword()) == Consts.REGISTERED_STUDENT_SUCCESSFULLY){
                return new AckMessage(message.getOpcode(),"");
            }
            else return new ErrorMessage(message.getOpcode());
        });
        instructions.put(Consts.LOGIN,(database, msg)->{
            RegisterLoginMessage message = (RegisterLoginMessage)msg;
            int loginResult = database.login(message.getUserName(),message.getPassword());
            if (loginResult == Consts.LOGGED_IN_STUDENT_SUCCESSFULLY) {
                username = message.getUserName();
                isLoggedIn = true;
                return new AckMessage(message.getOpcode(),"");
            }
            else if(loginResult == Consts.LOGGED_IN_ADMINISTRATOR_SUCCESSFULLY) {
                username = message.getUserName();
                isAdmin = true;
                isLoggedIn = true;
                return new AckMessage(message.getOpcode(),"");
            }
            else{
                return new ErrorMessage(message.getOpcode());
            }
        });
        instructions.put(Consts.LOGOUT,(database, msg)->{
            if (!isLoggedIn) return new ErrorMessage(msg.getOpcode());
            LogoutMyCoursesMessages message = (LogoutMyCoursesMessages)msg;
            if (database.logout(username) == Consts.LOGGED_OUT_SUCCESSFULLY) {
                isLoggedIn = false;
                shouldTerminate = true;
                return new AckMessage(message.getOpcode(),"");
            }
            else{
                return new ErrorMessage(message.getOpcode());
            }
        });
        instructions.put(Consts.COURSEREG,(database, msg)->{
            if (!isLoggedIn || isAdmin) return new ErrorMessage(msg.getOpcode());

            CourseNumberMessage message = (CourseNumberMessage)msg;
            int ans = database.registerCourse(username,message.getCourseNumber());
            if (ans == Consts.REGISTERED_COURSE_SUCCESSFULLY){
                return new AckMessage(message.getOpcode(),"");
            }
            else{
                return new ErrorMessage(msg.getOpcode());
            }
        });
        instructions.put(Consts.KDAMCHECK,(database, msg)->{
            if (!isLoggedIn || isAdmin) return new ErrorMessage(msg.getOpcode());
            CourseNumberMessage message = (CourseNumberMessage)msg;
            int [] kdamCourses = database.kdamCheck(message.getCourseNumber());
            return new AckMessage(message.getOpcode(), Arrays.toString(kdamCourses));
        });
        instructions.put(Consts.COURSESTAT,(database, msg)->{
            if (!isLoggedIn || !isAdmin) return new ErrorMessage(msg.getOpcode());
            CourseNumberMessage message = (CourseNumberMessage)msg;
            int courseNum = database.getCourseNum(message.getCourseNumber());
            String courseName = database.getCourseName(message.getCourseNumber());
            int [] kdamList = database.kdamCheck(message.getCourseNumber());
            int numOfMaxStudents = database.getNumOfMaxStudents(message.getCourseNumber());
            int registeredStudentsSize = database.getRegisteredStudentsSize(message.getCourseNumber());
            String output = "Course: (" + Integer.toString(courseNum) + ") " + courseName + "\nSeats Available: " +
                    Integer.toString(registeredStudentsSize) +"/"+Integer.toString(numOfMaxStudents) + "\n" +
                    "Students Registered: " + Arrays.toString(kdamList);
            return new AckMessage(message.getOpcode(),output);
        });
        instructions.put(Consts.STUDENTSTAT,(database, msg)->{
            if (!isLoggedIn || !isAdmin) return new ErrorMessage(msg.getOpcode());
            StudentStatMessage message = (StudentStatMessage)msg;
            ArrayList<Integer> studentCourses = database.getStudentCourses(username);
            String output = "Student: " + username + "\n" + "Courses: " + studentCourses.toString();
            return new AckMessage(message.getOpcode(),output);
        });
        instructions.put(Consts.ISREGISTERED,(database, msg)->{
            if (!isLoggedIn || isAdmin) return new ErrorMessage(msg.getOpcode());
            CourseNumberMessage message = (CourseNumberMessage)msg;
            int isRegistered = database.isRegisteredToCourse(username, message.getCourseNumber());
            if (isRegistered == Consts.IS_REGISTERED_TO_COURSE){
                return new AckMessage(message.getOpcode(),"REGISTERED");
            }
            else if (isRegistered == Consts.NOT_REGISTERED_TO_COURSE){
                return new AckMessage(message.getOpcode(),"NOT REGISTERED");
            }
            else return new ErrorMessage(msg.getOpcode());
        });
        instructions.put(Consts.UNREGISTER,(database, msg)->{
            if (!isLoggedIn || isAdmin) return new ErrorMessage(msg.getOpcode());
            CourseNumberMessage message = (CourseNumberMessage)msg;
            if (database.unregisterFromCourse(username, message.getCourseNumber()) == Consts.UNREGISTERED_FROM_COURSE_SUCCESSFULLY){
                return new AckMessage(message.getOpcode(),"");
            }
            else return new ErrorMessage(msg.getOpcode());
        });
        instructions.put(Consts.MYCOURSES,(database, msg)->{
            if (!isLoggedIn || isAdmin) return new ErrorMessage(msg.getOpcode());
            LogoutMyCoursesMessages message = (LogoutMyCoursesMessages)msg;
            return new AckMessage(message.getOpcode(),database.getStudentCourses(username).toString());
        });
        instructions.put(Consts.MYCOURSES,(database, msg)->{
            if (!isLoggedIn || isAdmin) return new ErrorMessage(msg.getOpcode());
            LogoutMyCoursesMessages message = (LogoutMyCoursesMessages)msg;
            return new AckMessage(message.getOpcode(),database.getStudentCourses(username).toString());
        });
    }

    @Override
    public OpMessage process(OpMessage msg){
        return instructions.get(msg.getOpcode()).process(database,msg);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
