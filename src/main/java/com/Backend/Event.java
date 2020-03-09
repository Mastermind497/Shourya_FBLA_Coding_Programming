package com.Backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;

import static com.Backend.MySQLMethods.addStudentHours;

public class Event extends Student implements Comparable<Event>, Comparator<Event> {
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

    public java.sql.Date getDateSQL() {
        String string_date = this.date.toStringRegular();

        SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd");
        long milliseconds = 0;
        try {
            java.util.Date d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(milliseconds);
    }

    public void setDate(String date) {
        this.date.setDate(date);
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public void setDate(LocalDate localDate) {
        date.setYear(localDate.getYear());
        date.setMonth(localDate.getMonthValue());
        date.setDay(localDate.getDayOfMonth());
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

    @Override
    public int compareTo(Event o) {
        return this.date.compareTo(o.getDate());
    }

    @Override
    public int compare(Event o1, Event o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
