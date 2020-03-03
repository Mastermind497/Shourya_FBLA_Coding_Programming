package com.Backend;

import com.vaadin.flow.component.notification.Notification;

import java.util.StringTokenizer;

public class Student {
    private String firstName;
    private String lastName;
    private int studentID;

    //This is used for the option in View Student info
    private boolean createNewStudent = false;

    public Student(String firstName, String lastName, int studentID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
    }

    public Student() {

    }

    public Student(boolean createNewStudent) {
        this.createNewStudent = createNewStudent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return MySQLMethods.makeName(this);
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void makeStudent(String dataLine) {
        StringTokenizer st = new StringTokenizer(dataLine, ", ");
        this.firstName = st.nextToken();
        this.lastName = st.nextToken();
        this.studentID = Integer.parseInt(st.nextToken());
    }

    public Student getStudent() {
        return this;
    }

    public void setStudent(Student newStudent) {
        this.firstName = newStudent.getFirstName();
        this.lastName = newStudent.getLastName();
        this.studentID = newStudent.getStudentID();
    }

    public boolean getCreateNewStudent() {
        return this.createNewStudent;
    }

    public void setCreateNewStudent(boolean createNewStudent) {
        this.createNewStudent = createNewStudent;
    }

    public StudentData getStudentData() {
        try {
            return MySQLMethods.selectTrackerAsStudent(this);
        } catch (Exception e) {
            Notification.show(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        if (!createNewStudent) {
            return firstName + " " + lastName + ", " + studentID;
        } else {
            return "CREATE NEW STUDENT";
        }
    }
}
