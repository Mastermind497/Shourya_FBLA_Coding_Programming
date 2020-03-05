package com.Backend;

import java.util.StringTokenizer;

/**
 * This is a class which helps in running MySQL Methods and takes in all the values needed
 * for the main student
 *
 * @author Shourya Bansal
 */
public class StudentData extends Student {
    private short grade;
    private double communityServiceHours;
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
        super(firstName, lastName, studentID);
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
        super(firstName, lastName, studentID);
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
        lastEdited = new Date();
    }

    public String getFirstName() {
        return super.getFirstName();
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

    public double getCommunityServiceHours() {
        return communityServiceHours;
    }

    public void setCommunityServiceHours(double communityServiceHours) {
        this.communityServiceHours = MySQLMethods.round(communityServiceHours);
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

    public void setYearsDone(int yearsDone) {
        this.yearsDone = (short) yearsDone;
    }

    public int getYearsDoneInt() {
        return yearsDone;
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

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        StringTokenizer st = new StringTokenizer(lastEdited, "-");
        int year = Integer.parseInt(st.nextToken());
        if (year > 1900) {
            this.lastEdited.setYear(year);
        } else {
            this.lastEdited.setYear(year + 1900);
        }
        this.lastEdited.setMonth(Integer.parseInt(st.nextToken()));
        this.lastEdited.setDay(Integer.parseInt(st.nextToken()));
    }

    public void createStudent() {
        MySQLMethods.createStudent(super.getFirstName(), super.getLastName(), super.getStudentID(),
                grade, communityServiceCategory, email, yearsDone);
    }

    private void setStudentData(StudentData studentData) {
        super.setFirstName(studentData.getFirstName());
        super.setLastName(studentData.getLastName());
        super.setStudentID(studentData.getStudentID());
        this.grade = studentData.getGrade();
        this.communityServiceCategory = studentData.getCommunityServiceCategory();
        this.email = studentData.getEmail();
        this.yearsDone = studentData.getYearsDone();
    }
}
