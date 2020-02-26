package fbla.backend;

import static fbla.backend.MySQLMethods.addStudentHours;

public class Event {
    private String firstName;
    private String lastName;
    private int studentID;
    private String eventName;
    private double hours;
    private int year;
    private int month;
    private int day;

    public Event(String firstName, String lastName, int studentID, String eventName, double hours,
                 int year, int month, int day) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
        this.eventName = eventName;
        this.hours = hours;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Event() {
        //Just Creates an Event Object
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDate(Date date) {
        this.year = date.getYear();
        this.month = date.getMonth();
        this.day = date.getDay();
    }

    public Student getStudent() {
        return new Student(firstName, lastName, studentID);
    }

    public void setStudent(Student student) {
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.studentID = student.getStudentID();
    }

    public void addEvent() {
        try {
            addStudentHours(firstName, lastName, studentID, eventName, hours,
                    year, month, day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
