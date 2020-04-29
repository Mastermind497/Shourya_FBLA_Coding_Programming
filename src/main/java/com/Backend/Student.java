package com.Backend;

import com.vaadin.flow.component.notification.Notification;

/**
 * The General Student Class to Store Basic Student Information
 * <p>
 * The Parent of StudentData and Event
 */
public class Student {
    /**
     * The First Name of the Student
     */
    private String firstName;
    /**
     * The Last Name of the Student
     */
    private String lastName;
    /**
     * The Student ID of the Student
     */
    private int studentID;

    /**
     * A boolean used for the option in View Student info
     */
    private boolean createNewStudent = false;

    /**
     * Creates an empty student to allow using the other methods to manipulate it
     */
    public Student() {
    }

    /**
     * Creates a student with the given variables
     *
     * @param firstName The First Name
     * @param lastName  The Last Name
     * @param studentID The Student ID
     */
    public Student(String firstName, String lastName, int studentID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
    }

    /**
     * Creates a new student with the createNewStudent variable, used to link a student
     * with the Add Student feature in Select, ComboBox, and others
     *
     * @param createNewStudent The value of createNewStudent, typically when this method is used;
     */
    public Student(boolean createNewStudent) {
        this.createNewStudent = createNewStudent;
    }

    /**
     * Gets the First
     *
     * @return the First Name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Changes the first name
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Changes the Last name
     *
     * @param lastName the new Last Name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the full name in a specific format
     * <p>
     * Uses the format firstName_lastName_studentID
     *
     * @return The Name in said format
     */
    public String getFullName() {
        return MySQLMethods.makeName(this);
    }

    /**
     * Gets the student ID
     *
     * @return the Student ID
     */
    public int getStudentID() {
        return studentID;
    }

    /**
     * Changes the StudentID
     *
     * @param studentID the new Student ID
     */
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    /**
     * Returns itself, mainly for the child methods
     *
     * @return itself, a Student
     */
    public Student getStudent() {
        return this;
    }

    /**
     * Changes all values of a student to be equal to a different student datatype
     *
     * @param newStudent The Values of the New Student
     */
    public void setStudent(Student newStudent) {
        this.firstName = newStudent.getFirstName();
        this.lastName = newStudent.getLastName();
        this.studentID = newStudent.getStudentID();
    }

    /**
     * Returns the boolean telling whether the object is meant to symbolize creating a new student.
     *
     * @return Whether the student symbolizes creating a new Student
     */
    public boolean getCreateNewStudent() {
        return this.createNewStudent;
    }

    /**
     * Uses the MySQLMethods to get the {@link StudentData} of the given Student
     *
     * @return The full StudentData of the Student
     */
    public StudentData getStudentData() {
        try {
            return MySQLMethods.selectTrackerAsStudent(this);
        } catch (Exception e) {
            Notification.show(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes this student from the backend database
     */
    public void delete() {
        MySQLMethods.delete(this);
    }

    /**
     * Converts The Student to a String
     *
     * @return A String representing the Student
     */
    @Override
    public String toString() {
        if (!createNewStudent) {
            return firstName + " " + lastName + ", " + studentID;
        } else {
            return "Create New Student";
        }
    }
}
