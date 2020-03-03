package com.Backend;

import static com.Backend.MySQLMethods.addStudentHours;

public class Event extends Student {
    private String eventName;
    private double hours;
    private Date date;

    public Event(String firstName, String lastName, int studentID, String eventName, double hours,
                 int year, int month, int day) {
        super(firstName, lastName, studentID);
        this.eventName = eventName;
        this.hours = hours;
        date = new Date(year, month, day);
    }

    public Event() {
        date = new Date();
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
        return date.getYear();
    }

    public void setYear(int year) {
        date.setYear(year);
    }

    public int getMonth() {
        return date.getMonth();
    }

    public void setMonth(int month) {
        date.setMonth(month);
    }

    public int getDay() {
        return date.getDay();
    }

    public void setDay(int day) {
        date.setDay(day);
    }

    public void setDate(int year, int month, int day) {
        date = new Date(year, month, day);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.setDate(date);
    }

    public Student getStudent() {
        return super.getStudent();
    }

    public void setStudent(Student student) {
        super.setStudent(student);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addEvent() {
        try {
            addStudentHours(super.getFirstName(), super.getLastName(), super.getStudentID(), eventName, hours,
                    date.getYear(), date.getMonth(), date.getDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Hours: %s, Date: %s", eventName, hours, date);
    }
}
