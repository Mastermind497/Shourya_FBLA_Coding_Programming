package com.Backend;

import javax.validation.constraints.NotNull;
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
        if (this.grade == 0) {
            this.grade = grade;
        } else {
            short finalGrade;
            if (grade == 0) {
                finalGrade = MySQLMethods.selectTrackerShort(getFirstName(), getLastName(), getStudentID(), "grade");
            } else finalGrade = grade;
            assert finalGrade != 0;
            updateQuery("grade", Short.toString(finalGrade));
            this.grade = finalGrade;
        }
    }

    public void setGrade(int grade) {
        setGrade((short) grade);
    }

    public void setGrade(String grade) {
        setGrade(Integer.parseInt(grade));
    }

    public int getGradeInt() {
        return grade;
    }

    public double getCommunityServiceHours() {
        return communityServiceHours;
    }

    public void setCommunityServiceHours(double communityServiceHours) {
        updateQuery("communityServiceHours", Double.toString(communityServiceHours));
        this.communityServiceHours = MySQLMethods.round(communityServiceHours);
    }

    public void setCommunityServiceHours(String communityServiceHours) {
        setCommunityServiceHours(Double.parseDouble(communityServiceHours));
    }

    public String getCommunityServiceCategory() {
        return communityServiceCategory;
    }

    public String getCurrentCommunityServiceCategory() {
        if (communityServiceHours >= 500) return "CSA Achievement (500 Hours)";
        else if (communityServiceHours >= 200) return "CSA Service (200 Hours)";
        else if (communityServiceHours >= 50) return "CSA Community (50 Hours)";
        else return "None";
    }

    public void setCommunityServiceCategory(String communityServiceCategoryIn) {
        if (this.communityServiceCategory == null) {
            this.communityServiceCategory = communityServiceCategoryIn;
        } else {
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
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull final String email) {
        if (this.email == null) {
            this.email = email;
        } else {
            String finalEmail;
            if (email == null) {
                finalEmail = "";
            } else finalEmail = email;
            updateQuery("email", finalEmail);
            this.email = finalEmail;
        }
    }

    public short getYearsDone() {
        return yearsDone;
    }

    public void setYearsDone(@NotNull final short yearsDone) {
        if (this.yearsDone == 0) {
            this.yearsDone = yearsDone;
        } else {
            short finalYears;
            if (email == null) {
                finalYears = MySQLMethods.selectTrackerShort(getFirstName(), getLastName(), getStudentID(), "yearsDone");
            } else finalYears = yearsDone;
            updateQuery("yearsDone", Short.toString(finalYears));
            this.yearsDone = finalYears;
        }
    }

    public void setYearsDone(String yearsDone) {
        setYearsDone(Short.parseShort(yearsDone));
    }

    public void setYearsDone(int yearsDone) {
        setYearsDone((short) yearsDone);
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

    @Override
    public void setFirstName(@NotNull final String firstName) {
        if (getFirstName() == null) {
            super.setFirstName(firstName);
        } else {
            String finalFirstName;
            if (email == null) {
                finalFirstName = "";
            } else finalFirstName = firstName;
            assert finalFirstName != null;
            MySQLMethods.updateFirstName(getFirstName(), getLastName(), getStudentID(), finalFirstName);
            super.setFirstName(finalFirstName);
        }
    }

    @Override
    public void setLastName(@NotNull final String lastName) {
        if (getLastName() == null) {
            super.setLastName(lastName);
        } else {
            String finalLastName;
            if (email == null) {
                finalLastName = "";
            } else finalLastName = lastName;
            assert finalLastName != null;
            MySQLMethods.updateLastName(getFirstName(), getLastName(), getStudentID(), lastName);
            super.setLastName(finalLastName);
        }
    }

    @Override
    public void setStudentID(@NotNull final int studentID) {
        if (getStudentID() == 0) {
            super.setStudentID(studentID);
        } else {
            int finalStudentID;
            if (studentID == 0) {
                finalStudentID = MySQLMethods.selectTrackerInt(getFirstName(), getLastName(), getStudentID(), "studentID");
            } else finalStudentID = studentID;
            assert !(finalStudentID == 0);
            MySQLMethods.updateStudentID(getFirstName(), getLastName(), getStudentID(), studentID);
            super.setStudentID(studentID);
        }
    }

    public void setStudentID(String studentID) {
        setStudentID(Integer.parseInt(studentID));
    }

    private void updateQuery(String dataType, String newData) {
        try {
            MySQLMethods.updateTracker(super.getFirstName(), super.getLastName(), super.getStudentID()
                    , dataType, newData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
