package bgu.spl.net;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
    private HashMap<String,Integer> passwords;
    private HashMap<String,String> studentCourses;
    private Set<Course> courses;
    private Set<String> loggedIn;

    private static class DatabaseHolder{
        private static Database instance = new Database();
    }
    /**
     * Constructs the only Database instance of this class.
     */
    //to prevent user from creating new Database
    private Database() {
        passwords = new HashMap();
        studentCourses= new HashMap();
        courses = new HashSet();
        loggedIn = new HashSet();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return DatabaseHolder.instance;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    boolean initialize(String coursesFilePath) {
        Scanner input = new Scanner(new File("Stock.txt"));
        input.useDelimiter("-|\n");

        Product[] products = new Product[0];
        while(input.hasNext()) {
            int id = input.nextInt();
            String department = input.next();
            String name = input.next();
            double price = Double.valueOf(input.next().substring(1));
            int stock = input.nextInt();

            Product newProduct = new Product(name, price, department, id, stock);
            products = addProduct(products, newProduct);
        }



    }

    public boolean registerStudent(){
        return false;
    }

    public boolean registerAdmin(){
        return false;
    }

    public boolean isRegistered(){
        return false;
    }

    public boolean isLoggedIn(){
        return false;
    }
}