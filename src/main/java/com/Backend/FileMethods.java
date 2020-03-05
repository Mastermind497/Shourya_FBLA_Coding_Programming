package com.Backend;

import java.io.*;
import java.util.ArrayList;

public class FileMethods {
    //The Working Directory to make it easier to access
    public static String workingDirectory = getWorkingDirectory();

    //The path to the student name file
    public static String studentPath = workingDirectory + "\\data\\studentList.user";

//    public static void main(String[] args) throws IOException{
//        System.out.println(workingDirectory);
//        clearStudentList();
//    }

    /**
     * Adds the name of a student to a custom file with extension .user (a text based file) to help keep track of the students quickly
     *
     * @deprecated now use firstName and lastName {@link #addToStudent(String, String, int)}
     */
    @Deprecated
    public static void addToStudent(String studentName) throws IOException {
        //making the second term true allows us to append the text file without removing what was previously there.
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(studentPath, true)));
        //Adds student Name to file
        out.println(studentName);
        out.close();
    }

    /**
     * Adds the name of a student to a custom file with extension .user (a text based file) to help keep track of the students quickly
     *
     * @param firstName First Name
     * @param lastName  Last Name
     * @param studentID the Student ID
     * @throws IOException in case file doesn't exist
     */
    public static void addToStudent(String firstName, String lastName, int studentID) throws IOException {
        //making the second term true allows us to append the text file without removing what was previously there.
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(studentPath, true)));
        //Adds student Name to file
        out.println(firstName.toUpperCase() + ", " + lastName.toUpperCase() + ", " + studentID);
        out.close();
    }

    public static void addToStudent(Student student) throws IOException {
        //making the second term true allows us to append the text file without removing what was previously there.
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(studentPath, true)));
        //Adds student Name to file
        out.println(student.getFirstName().toUpperCase() + ", " + student.getLastName().toUpperCase() + ", " + student.getStudentID());
        out.close();
    }

    public static void clearStudentList() throws IOException {
        new PrintWriter(new BufferedWriter(new FileWriter(studentPath)));
    }

    //Returns the save location of all of the files which are created
    public static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    //Gets number of Students
    public static int getNumberOfStudents() throws IOException {
        //Quickly reads the file of student names
        BufferedReader read = new BufferedReader(new FileReader(studentPath));
        int lines = 0;
        //sees if there is a line; if so, it adds 1 to the variable lines
        while (read.readLine() != null) lines++;
        read.close();
        return lines;
    }

    /**
     * Gets all current Students
     *
     * @return an Array Containing all of the Students
     */
    public static Student[] getStudents() throws IOException {
        Student[] students = new Student[getNumberOfStudents()];
        BufferedReader read = new BufferedReader(new FileReader(studentPath));
        for (int i = 0; i < students.length; i++) {
            Student temp = new Student();
            temp.makeStudent(read.readLine());
            students[i] = temp;
        }
        return students;
    }

    /**
     * This method returns an ArrayList of all Students on whom we have data stored. The
     * file is an alternate way to store data as it is easier to use and faster to access.
     * However, the main data is all stored in the MySQL Database.
     *
     * @return An ArrayList containing all the students on whom we have data
     */
    public static ArrayList<Student> getStudentsAsList() {
        ArrayList<Student> students = new ArrayList<>();
        try {
            BufferedReader read = new BufferedReader(new FileReader(studentPath));
            for (int i = 0; i < getNumberOfStudents(); i++) {
                Student temp = new Student();
                temp.makeStudent(read.readLine());
                students.add(temp);
            }
        } catch (IOException e) {
            //Do Nothing
        }
        return students;
    }

    public static ArrayList<StudentData> getStudentData() {
        ArrayList<Student> students = getStudentsAsList();
        ArrayList<StudentData> studentData = new ArrayList<>();

        for (Student student : students) {
            studentData.add(student.getStudentData());
        }

        return studentData;
    }

    /**
     * Removes a student from the list of students
     *
     * @param student The student that will be removed
     * @return the student removed
     * @throws IOException in case the file is not found
     */
    public static Student removeStudent(Student student) throws IOException {
        //Gets Current Students
        Student[] currentStudents = getStudents();

        //Clears Text File of Students
        clearStudentList();

        //Adds the students back (excluding the student to remove)
        for (Student currentStudent : currentStudents) {
            if (!currentStudent.getFullName().equals(student.getFullName())) {
                addToStudent(currentStudent);
            }
        }

        return student;
    }
}
