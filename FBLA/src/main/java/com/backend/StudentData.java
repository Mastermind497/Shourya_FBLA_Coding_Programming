package com.backend;

import java.sql.Date;

/**
 * This is a class which helps in running MySQL Methods and takes in all the values needed
 * for the main student
 *
 * @author Shourya Bansal
 */
public class StudentData {
    private String firstName;
    private String lastName;
    private int studentID;
    private short grade;
    private int communityServiceHours;
    private String communityServiceCategory;
    private String email;
    private short yearsDone;
    private boolean freshman;
    private boolean sophomore;
    private boolean junior;
    private boolean senior;
    private Date lastEdited;

    /**
     * A Constructor for a Student if all the values are already known.
     *
     * @param firstName                First Name
     * @param lastName                 Last Name
     * @param studentID                Student ID
     * @param grade                    Grade
     * @param communityServiceCategory Category of Community Service Event
     * @param email                    Email Address
     * @param yearsDone                Number of years completed, including this year
     */
    public StudentData(String firstName, String lastName, int studentID, short grade,
                       String communityServiceCategory, String email, short yearsDone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
        this.grade = grade;
        this.communityServiceCategory = communityServiceCategory;
        this.email = email;
        this.yearsDone = yearsDone;
    }

    /**
     * A Constructor for a Student if all the values are already known.
     *
     * @param firstName                First Name
     * @param lastName                 Last Name
     * @param studentID                Student ID
     * @param grade                    Grade
     * @param communityServiceCategory Category of Community Service Event
     * @param email                    Email Address
     * @param yearsDone                Number of years completed, including this year
     */
    public StudentData(String firstName, String lastName, int studentID, int grade,
                       String communityServiceCategory, String email, int yearsDone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
        this.grade = (short) grade;
        this.communityServiceCategory = communityServiceCategory;
        this.email = email;
        this.yearsDone = (short) yearsDone;
    }

    /**
     * Simple Student Creation, needs to use setter methods before anything can be done though
     */
    public StudentData() {
        this.yearsDone = 0;
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

    public short getGrade() {
        return grade;
    }

    public void setGrade(short grade) {
        this.grade = grade;
    }

    public void setGrade(int grade) {
        this.grade = (short) grade;
    }

    public int getGradeInt() {
        return grade;
    }

    public int getCommunityServiceHours() {
        return communityServiceHours;
    }

    public void setCommunityServiceHours(int communityServiceHours) {
        this.communityServiceHours = communityServiceHours;
    }

    public String getCommunityServiceCategory() {
        return communityServiceCategory;
    }

    public void setCommunityServiceCategory(String communityServiceCategory) {
        this.communityServiceCategory = communityServiceCategory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public short getYearsDone() {
        return yearsDone;
    }

    public void setYearsDone(short yearsDone) {
        this.yearsDone = yearsDone;
    }

    public boolean isFreshman() {
        return freshman;
    }

    public void setFreshman(boolean freshman) {
        this.freshman = freshman;
        if (this.freshman)
            yearsDone++;
        else yearsDone--;
    }

    public boolean isSophomore() {
        return sophomore;
    }

    public void setSophomore(boolean sophomore) {
        this.sophomore = sophomore;
        if (this.sophomore)
            yearsDone++;
        else yearsDone--;
    }

    public boolean isJunior() {
        return junior;
    }

    public void setJunior(boolean junior) {
        this.junior = junior;
        if (this.junior)
            yearsDone++;
        else yearsDone--;
    }

    public boolean isSenior() {
        return senior;
    }

    public void setSenior(boolean senior) {
        this.senior = senior;
        if (this.senior) {
            yearsDone++;
        } else yearsDone--;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEditedNow() {
        MySQLMethods.updateToCurrentDate(firstName, lastName, studentID);
    }

    public void createStudent() {
        MySQLMethods.createStudent(firstName, lastName, studentID, grade, communityServiceCategory,
                email, yearsDone);
    }
}
