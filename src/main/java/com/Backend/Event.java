package com.Backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

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

    public Event(Student student, String eventName, double hours, Date date) {
        super.setStudent(student);
        this.eventName = eventName;
        this.hours = hours;
        this.date = date;
    }

    public Event() {
        date = new Date();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        boolean needToUpdateSQL = this.eventName != null;
        this.eventName = eventName;
        if (needToUpdateSQL) updateEvent(oldEvent, this);
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        boolean needToUpdateSQL = this.hours != 0;
        this.hours = hours;
        if (needToUpdateSQL) updateEvent(oldEvent, this);
    }

    public void setHours(String hours) {
        setHours(Double.parseDouble(hours));
    }

    public int getYear() {
        return date.getYear();
    }

    public void setYear(int year) {
        setDate(date.getDay(), date.getMonth(), year);
    }

    public int getMonth() {
        return date.getMonth();
    }

    public void setMonth(int month) {
        setDate(date.getDay(), month, date.getYear());
    }

    public int getDay() {
        return date.getDay();
    }

    public void setDay(int day) {
        setDate(day, date.getMonth(), date.getYear());
    }

    public void setDate(int year, int month, int day) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        boolean needToUpdateSQL = (date != null);
        date = new Date(year, month, day);
        if (needToUpdateSQL) updateEvent(oldEvent, this);
    }

    public Date getDate() {
        return date;
    }

    public java.sql.Date getDateSQL() {
        String string_date = this.date.toStringRegular();

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
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
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours,
                new Date(this.date.getYear(), this.date.getMonth(), this.date.getDay()));
        if (date.contains("-")) {
            this.date.setDate(date);
        } else if (date.contains(" ")) {
            StringTokenizer st = new StringTokenizer(date);
            int day = Integer.parseInt(st.nextToken());
            String monthString = st.nextToken();
            monthString = monthString.substring(0, monthString.length() - 1);
            int month = Date.getMonth(monthString);
            int year = Integer.parseInt(st.nextToken());
            this.date.setDate(LocalDate.of(year, month, day));
            this.date.setDay(day);
            this.date.setMonth(month);
            this.date.setYear(year);
            updateEvent(oldEvent, this);
        } else System.out.println("No Condition Met, Date Change Failed");
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public void setDate(LocalDate localDate) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        boolean needToUpdateSQL = this.date != null;
        date.setYear(localDate.getYear());
        date.setMonth(localDate.getMonthValue());
        date.setDay(localDate.getDayOfMonth());
        if (needToUpdateSQL) updateEvent(oldEvent, this);
    }

    public void setDate(java.sql.Date date) {
        setDate(date.toString());
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
                    date.getYear(), date.getMonth() - 1 //The date feature in Vaadin returns one month too large
                    , date.getDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        MySQLMethods.delete(this);
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

    public void updateEvent(Event oldEvent, Event newEvent) {
        MySQLMethods.updateEvent(super.getStudent(), oldEvent, newEvent);
    }

    public static double getTotalHours(List<Event> eventList) {
        double hours = 0;
        for (Event event : eventList) {
            hours += event.getHours();
        }

        return MySQLMethods.round(hours);
    }

    public List<String> getMonths(int start, int end) {
        return new ArrayList<>();
    }
}
