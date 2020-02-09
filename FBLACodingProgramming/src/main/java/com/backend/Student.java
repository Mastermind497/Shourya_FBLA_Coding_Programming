/**
 * Created by shour on 2/8/2020 at 10:08 PM
 */
package com.backend;

import java.util.*;

public class Student {
    private String firstName;
    private String lastName;
    private int studentID;

    public Student(String firstName, String lastName, int studentID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
    }

    public Student() {
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

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + studentID;
    }
}
