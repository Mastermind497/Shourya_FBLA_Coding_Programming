package com.backend;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This is a class which helps in running MySQL Methods and takes in all the values needed
 * for the main student
 *
 * @author Shourya Bansal
 * @see Student
 */
public class StudentData extends Student implements Cloneable {
    /**
     * The Date any of this student's data was last edited
     */
    private final Date lastEdited;
    /**
     * The Student's Grade
     */
    private short grade;
    /**
     * The Student's Community Service Hours
     */
    private double communityServiceHours;
    /**
     * The Student's Community Service Award Category
     */
    private String communityServiceCategory;
    /**
     * The Student's Email address, typically provided by the school
     */
    private String email;
    /**
     * The number of years this Student participated in FBLA
     */
    private short yearsDone;


    /**
     * A Constructor which allows the use of
     * @param fromSelect the fromSelect value, typically true if this method is used.
     */
    public StudentData(boolean fromSelect) {
        this();
    }

    /**
     * A Simple Constructor for the creation of a StudentData Object
     *
     * However, the StudentData Object can not be used unless the requisite setter methods are used
     *
     * @see #setGrade(int)
     * @see #setYearsDone(short)
     * @see #setStudentID(int)
     * @see #setStudent(Student)
     * @see #setCommunityServiceHours(double)
     * @see #setCommunityServiceCategory(String)
     */
    public StudentData() {
        this.yearsDone = 0;
        lastEdited = new Date();
    }

    /**
     * Calculates the average achieved category in a list of students
     *
     * @param studentDataList The List of Students
     * @return The Average Category
     */
    public static String getAverageCategory(List<StudentData> studentDataList) {
        int category = 0;
        //Assign each category a weighting: 1, 2, or 3
        for (StudentData s : studentDataList) {
            if (s.getCurrentCommunityServiceCategory().contains("Achievement")) category += 3;
            else if (s.getCurrentCommunityServiceCategory().contains("Service")) category += 2;
            else if (s.getCurrentCommunityServiceCategory().contains("Community")) category++;
        }

        //Gets the average of the weighting
        short averageCategory = (short) Math.round((double) category / studentDataList.size());

        //Converts the average integer back to the Category
        return category(averageCategory);
    }

    /**
     * Calculates the Average Category Goal of a List of Students
     *
     * @param studentDataList The List of Students
     * @return The Average Category Goal
     */
    public static String getAverageGoal(List<StudentData> studentDataList) {
        int category = 0;
        for (StudentData s : studentDataList) {
            if (s.getCommunityServiceCategory().contains("Achievement")) category += 3;
            else if (s.getCommunityServiceCategory().contains("Service")) category += 2;
            else if (s.getCommunityServiceCategory().contains("Community")) category++;
        }

        short averageCategory = (short) Math.round((double) category / studentDataList.size());

        return category(averageCategory);
    }

    /**
     * Takes a List of Students and returns the value of their average current category
     * discounting students with no hours
     *
     * @param studentDataListIn A List of Students which contains both active and inactive students
     * @return The Average Category of only the Active Students
     */
    public static String getActiveCategory(List<StudentData> studentDataListIn) {
        //Removes all inactive students without making any changes to the original List
        List<StudentData> studentDataList = removeInactive(studentDataListIn);

        //Using the new List of only Active Students, it returns the average category
        return getAverageCategory(studentDataList);
    }

    /**
     * Takes a List of Students and returns the value of their average Community Service Goal
     * discounting the students with no hours
     *
     * @param studentDataListIn A List of Students which contains both active and inactive students
     * @return The Average Category Goal of only the Active Students
     */
    public static String getActiveGoal(List<StudentData> studentDataListIn) {
        //Removes all inactive students without making any changes to the original List
        List<StudentData> studentDataList = removeInactive(studentDataListIn);

        //Using the new list of only active Students, it returns the average goal
        return getAverageGoal(studentDataList);
    }

    /**
     * Takes a list of Students and removes all of the students with no hours. No
     * changes are made to the original List to ensure that both the old and new list are still
     * accessible by the User
     *
     * @param studentDataListIn A List of Students with both active and inactive students
     * @return A new list with only the active students
     */
    public static List<StudentData> removeInactive(List<StudentData> studentDataListIn) {
        //Clones the list so that changes are not made in the original list
        List<StudentData> studentDataList = new ArrayList<>(studentDataListIn);

        //Removes all non-active students
        studentDataList.removeIf(s -> !s.isActive());

        return studentDataList;
    }

    /**
     * Returns a new list with only the inactive students
     *
     * @param studentDataListIn A List containing both active and inactive students
     * @return A new list with only inactive students
     */
    public static List<StudentData> removeActive(List<StudentData> studentDataListIn) {
        //Clones the List so changes are not made in the original list
        List<StudentData> studentDataList = new ArrayList<>(studentDataListIn);

        //Removes all Active Students
        studentDataList.removeIf(StudentData::isActive);

        return studentDataList;
    }

    /**
     * Uses an Average Number generated in other methods to return the
     * average Community Service Goal
     *
     * @param averageCategory A Calculated Number of the Average Community Service Category
     * @return A String of the Average Category
     */
    private static String category(short averageCategory) {
        if (averageCategory == 3) return "CSA Achievement (500 Hours)";
        else if (averageCategory == 2) return "CSA Service (200 Hours)";
        else if (averageCategory == 1) return "CSA Community (50 Hours)";
        else return "None";
    }

    /**
     * Gets the student's current grade level
     *
     * @return the current grade level
     */
    public short getGrade() {
        return grade;
    }

    /**
     * Sets the Grade Level of the Student
     *
     * @param grade The New Grade level
     */
    public void setGrade(int grade) {
        this.grade = (short) grade;
    }

    /**
     * Sets the Grade Level of the Student, changing the backend database if necessary
     *
     * @param grade The New Grade level
     */
    public void setGrade(String grade) {
        setGrade(Integer.parseInt(grade));
    }

    /**
     * Updates the backend while changing the student's grade
     *
     * @param grade The New Grade
     */
    public void updateGrade(int grade) {
        short finalGrade;
        if (grade == 0) {
            finalGrade = MySQLMethods.selectTrackerShort(getFirstName(), getLastName(), getStudentID(), "grade");
        } else finalGrade = (short) grade;
        assert finalGrade != 0;
        updateQuery("grade", Short.toString(finalGrade));
        this.grade = finalGrade;
    }

    /**
     * Updates the backend while changing the student's grade
     *
     * @param grade The New Grade
     */
    public void updateGrade(String grade) {
        updateGrade(Integer.parseInt(grade));
    }

    /**
     * Gets the student's current grade level
     *
     * @return the current grade level
     */
    public int getGradeInt() {
        return grade;
    }

    /**
     * Gets the Amount of Community Service Hours the Student has participated in
     *
     * @return The Number of hours
     */
    public double getCommunityServiceHours() {
        return communityServiceHours;
    }

    /**
     * Changes the number of Community Service Hours a student participated in
     *
     * @param communityServiceHours The new amount of community Service Hours
     */
    public void setCommunityServiceHours(double communityServiceHours) {
        this.communityServiceHours = MySQLMethods.round(communityServiceHours);
    }

    /**
     * Updates the backend with a new number of Community Service Hours
     *
     * @param communityServiceHours The new amount of community Service Hours
     */
    public void updateCommunityServiceHours(double communityServiceHours) {
        updateQuery("communityServiceHours", Double.toString(MySQLMethods.round(communityServiceHours)));
        this.communityServiceHours = MySQLMethods.round(communityServiceHours);
    }

    /**
     * Updates the backend with a new number of Community Service Hours
     *
     * @param communityServiceHours The new amount of community Service Hours
     */
    public void updateCommunityServiceHours(String communityServiceHours) {
        updateCommunityServiceHours(Double.parseDouble(communityServiceHours));
    }

    /**
     * Gets the Community Service Award Category Goal of the Student
     *
     * @return The Community Service Award Category Goal
     */
    public String getCommunityServiceCategory() {
        return communityServiceCategory;
    }

    /**
     * Changes the Community Service Award Category
     *
     * @param communityServiceCategoryIn The new Community Service Award Category
     */
    public void setCommunityServiceCategory(String communityServiceCategoryIn) {
        this.communityServiceCategory = communityServiceCategoryIn;
    }

    /**
     * Updates the Community Service Award Category in the backend
     *
     * @param communityServiceCategoryIn The New Community Service Award Category Goal
     */
    public void updateCommunityServiceCategory(String communityServiceCategoryIn) {
        String communityServiceCategoryUpper = communityServiceCategoryIn.toUpperCase();
        String communityServiceCategory;
        if (communityServiceCategoryUpper.contains("ACHIEVEMENT")) {
            communityServiceCategory = "CSA Achievement (500 Hours)";
        } else if (communityServiceCategoryUpper.contains("SERVICE")) {
            communityServiceCategory = "CSA Service (200 Hours)";
        } else {
            communityServiceCategory = "CSA Community (50 Hours)";
        }
        updateQuery("communityServiceCategory", communityServiceCategory);
        this.communityServiceCategory = communityServiceCategory;
    }

    /**
     * Gets each Community Service Categories respective integer value
     *
     * @return the respective integer value of the Student's Category goal
     */
    public int getCommunityServiceCategoryInt() {
        if (communityServiceCategory.toUpperCase().contains("ACHIEVEMENT")) {
            return 3;
        } else if (communityServiceCategory.toUpperCase().contains("SERVICE")) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Gets the Student's current Community Service Award Category depending on the hours the student
     * has done
     *
     * @return The Category
     */
    public String getCurrentCommunityServiceCategory() {
        if (communityServiceHours >= 500) return "CSA Achievement (500 Hours)";
        else if (communityServiceHours >= 200) return "CSA Service (200 Hours)";
        else if (communityServiceHours >= 50) return "CSA Community (50 Hours)";
        else return "None";
    }

    /**
     * Gets the student's current achieved category and returns its respective integer
     *
     * @return An integer representing the student's current category
     * @see #getCurrentCommunityServiceCategory()
     */
    public int getCurrentCommunityServiceCategoryInt() {
        String communityServiceCategory = getCurrentCommunityServiceCategory();
        if (communityServiceCategory.toUpperCase().contains("ACHIEVEMENT")) {
            return 3;
        } else if (communityServiceCategory.toUpperCase().contains("SERVICE")) {
            return 2;
        } else if (communityServiceCategory.toUpperCase().contains("COMMUNITY")) {
            return 1;
        } else return 0;
    }

    /**
     * Gets the student's email address
     *
     * @return The Student's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the student's email address
     *
     * @param email The New email address
     */
    public void setEmail(@NotNull final String email) {
        this.email = email;
    }

    /**
     * Updates the student's email address in the backend
     *
     * @param email The new email address
     */
    public void updateEmail(String email) {
        String finalEmail;
        if (email == null) {
            finalEmail = "None";
        } else finalEmail = email;
        updateQuery("email", finalEmail);
        this.email = finalEmail;
    }

    /**
     * Gets the number of years a student has participated in FBLA
     *
     * @return The Number of Years a Student Has Participated in FBLA
     */
    public short getYearsDone() {
        return yearsDone;
    }

    /**
     * Changes the value of the number of years a student has participated in FBLA
     *
     * @param yearsDone The new number of years done
     */
    public void setYearsDone(@NotNull final short yearsDone) {
        this.yearsDone = yearsDone;
    }

    /**
     * Changes the value of the number of years a student has participated in FBLA
     *
     * @param yearsDone The new number of years done
     */
    public void setYearsDone(String yearsDone) {
        setYearsDone(Short.parseShort(yearsDone));
    }

    /**
     * Updates the number of years a student has participated in the FBLA chapter in the backend database
     *
     * @param yearsDone The new number of years done
     */
    public void updateYearsDone(short yearsDone) {
        short finalYears;
        if (email == null) {
            finalYears = MySQLMethods.selectTrackerShort(getFirstName(), getLastName(), getStudentID(), "yearsDone");
        } else finalYears = yearsDone;
        updateQuery("yearsDone", Short.toString(finalYears));
        this.yearsDone = finalYears;
    }

    /**
     * Updates the number of years a student has participated in the FBLA chapter in the backend database
     *
     * @param yearsDone The new number of years done
     */
    public void updateYearsDone(String yearsDone) {
        updateYearsDone(Short.parseShort(yearsDone));
    }

    /**
     * Updates the number of years a student has participated in the FBLA chapter in the backend database
     *
     * @param yearsDone The new number of years done
     */
    public void updateYearsDone(int yearsDone) {
        updateYearsDone((short) yearsDone);
    }

    /**
     * Gets the date any of a Student's information was last edited
     *
     * @return A {@link Date} containing the last date a value was changed
     */
    public Date getLastEdited() {
        return lastEdited;
    }

    /**
     * Changes the last-edited date of a Student's information
     *
     * @param lastEdited The new lastEdited Date
     */
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

    /**
     * Gets whether or not a student achieved their Community Service Award Category Goal
     *
     * @return a boolean of whether or not a student achieved his or her goal
     */
    public boolean achievedGoal() {
        return getCommunityServiceCategory().toUpperCase().equals(getCurrentCommunityServiceCategory().toUpperCase());
    }

    /**
     * Creates this student and adds it to database
     */
    public void createStudent() {
        MySQLMethods.createStudent(super.getFirstName(), super.getLastName(), super.getStudentID(),
                grade, communityServiceCategory, email, yearsDone);
    }

    /**
     * Temporarily sets the community Service hours of a student, typically used when looking at the hours in a range
     *
     * @param communityServiceHours the temporary amount of hours of a student
     */
    public void setCommunityServiceHoursTemp(double communityServiceHours) {
        this.communityServiceHours = MySQLMethods.round(communityServiceHours);
    }

    /**
     * Returns whether or not the student has any hours
     * <p>
     * If a Student has no hours, the student is considered inActive, so some tables will ignore them when showing data
     *
     * @return whether or not a student is active
     */
    public boolean isActive() {
        return communityServiceHours > 0;
    }

    /**
     * Updates the first name in the backend database
     *
     * @param firstName The new First Name
     */
    public void updateFirstName(String firstName) {
        String finalFirstName;
        if (firstName == null) {
            finalFirstName = "";
        } else finalFirstName = firstName;
        MySQLMethods.updateFirstName(getFirstName(), getLastName(), getStudentID(), finalFirstName);
        setFirstName(firstName);
    }

    /**
     * Updates the Last Name in the backend database
     *
     * @param lastName The new Last Name
     */
    public void updateLastName(String lastName) {
        String finalLastName;
        if (lastName == null) {
            finalLastName = "";
        } else finalLastName = lastName;
        MySQLMethods.updateLastName(getFirstName(), getLastName(), getStudentID(), lastName);
        setLastName(finalLastName);
    }

    /**
     * Updates the Student ID in the backend database
     *
     * @param studentID The new Student ID
     */
    public void updateStudentID(int studentID) {
        int finalStudentID;
        if (studentID == 0) {
            finalStudentID = MySQLMethods.selectTrackerInt(getFirstName(), getLastName(), getStudentID(), "studentID");
        } else finalStudentID = studentID;
        MySQLMethods.updateStudentID(getFirstName(), getLastName(), getStudentID(), finalStudentID);
    }

    /**
     * Updates the Student ID in the backend database
     *
     * @param studentID the new Student ID
     */
    public void updateStudentID(String studentID) {
        updateStudentID(Integer.parseInt(studentID));
    }

    /**
     * Updates the current student in the MySQL Database, using the dataType as a reference
     *
     * @param dataType the Data Type being changed
     * @param newData  The New Data of the Student
     */
    private void updateQuery(String dataType, String newData) {
        try {
            MySQLMethods.updateTracker(super.getFirstName(), super.getLastName(), super.getStudentID()
                    , dataType, newData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String toFullString() {
        return String.format("FirstName=%s,LastName=%s,StudentID=%d,Grade=%d,CommunityServiceAwardHours=%e,communityServiceAwardCategory=%s,Email=%s,YearsDone=%d",
                             getFirstName(), getLastName(), getStudentID(), grade, communityServiceHours, communityServiceCategory, email, yearsDone);
    }
    
    public StudentData clone() {
        try {
            return (StudentData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
