package com.backend;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import static com.backend.MySQLMethods.addStudentHours;

/**
 * A Class to Store Events of Students
 */
public class Event extends Student implements Comparable<Event>, Comparator<Event>, Cloneable {
    /**
     * The Name of the Event
     */
    private String eventName;

    /**
     * The Length of the Event
     */
    private double hours;

    /**
     * The Date of the Event
     */
    private Date date;

    /**
     * Event Constructor with Each Value Individually
     *
     * @param firstName The First Name of the Student
     * @param lastName  The Last Name of the Student
     * @param studentID The Student ID of the Student
     * @param eventName The Name of the Event
     * @param hours     The Length of the Event
     * @param year      The Year of the Event
     * @param month     The Month of the Event
     * @param day       The Day of the Event
     */
    public Event(String firstName, String lastName, int studentID, String eventName, double hours,
                 int year, int month, int day) {
        super(firstName, lastName, studentID);
        this.eventName = eventName;
        this.hours = hours;
        date = new Date(year, month, day);
    }

    /**
     * Event Constructor with Each Value Individually
     *
     * @param student   The Student doing the Event
     * @param eventName The Name of the Event
     * @param hours     The Length of the Event
     * @param date      The Date of the Event
     */
    public Event(Student student, String eventName, double hours, Date date) {
        super.setStudent(student);
        this.eventName = eventName;
        this.hours = hours;
        this.date = date;
    }

    /**
     * Creates an empty event, only initializing the date to avoid NullPointerExceptions
     */
    public Event() {
        date = new Date();
    }

    /**
     * Gets an Array of all the Months in the range between two months, including the start and the end.
     * <p>
     * This is used for labeling graphs, particularly the Line Graph in the Individual Student Reports.
     *
     * @param startDateIn The Start Date in the Range
     * @param endDateIn   The End Date in the Range
     * @return An Array of Strings containing all Labels of the dates in the range.
     */
    public static String[] getMonthsWithYear(Date startDateIn, Date endDateIn) {
        Date startDate = startDateIn.clone();
        Date endDate = endDateIn.clone();

        //Calculates the number of months in the period
        int numTimes = (endDate.getMonth() - startDate.getMonth()) + (endDate.getYear() - startDate.getYear()) + 1;
        if (numTimes < 0) {
            Date temp = endDate;
            endDate = startDate;
            startDate = temp;
            numTimes = (endDate.getMonth() - startDate.getMonth()) + (endDate.getYear() - startDate.getYear()) + 1;
        }

        String[] months = new String[numTimes];

        for (int i = 0; i < numTimes; i++) {
            months[i] = (Date.getMonth(startDate.getMonth()) + " " + startDate.getYear());
            startDate.incrementByMonth();
        }

        return months;
    }

    /**
     * Gets the number of months in a range between two months
     *
     * @param startDate The start date of the Range
     * @param endDate   The end date of the range
     * @return the range
     */
    public static int monthsInRange(Date startDate, Date endDate) {
        return (endDate.getMonth() - startDate.getMonth()) + (endDate.getYear() - startDate.getYear()) + 1;
    }

    /**
     * Gets the total hours in a list of Events, especially useful when calculating the hours
     * a student achieved in a certain range
     *
     * @param eventList The List of Events
     * @return the total time spent volunteering
     */
    public static double getTotalHours(List<Event> eventList) {
        double hours = 0;
        for (Event event : eventList) {
            hours += event.getHours();
        }

        return MySQLMethods.round(hours);
    }

    /**
     * Gets the name of the Event
     *
     * @return The Name of the Event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event
     *
     * @param eventName The new Name of the Event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Changes the backend with the new event name
     *
     * @param eventName The new Name of the Event
     */
    public void updateEventName(String eventName) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        this.eventName = eventName;
        updateEvent(oldEvent, this);
    }

    /**
     * Gets the length of the Event
     *
     * @return The Length of the Event
     */
    public double getHours() {
        return hours;
    }

    /**
     * Changes the length of the Event
     *
     * @param hours The new Length of the Event
     */
    public void setHours(double hours) {
        this.hours = hours;
    }

    /**
     * Updates the hours in the backend
     *
     * @param hours The new number of hours
     */
    public void updateHours(double hours) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        this.hours = hours;
        updateEvent(oldEvent, this);
    }

    /**
     * Updates the hours in the backend
     *
     * @param hours The new number of hours
     */
    public void updateHours(String hours) {
        updateHours(Double.parseDouble(hours));
    }

    /**
     * Gets the Year
     *
     * @return the current Year
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Changes the Year
     *
     * @param year The New Year
     */
    public void setYear(int year) {
        setDate(year, date.getMonth(), date.getDay());
    }

    /**
     * Updates the Year
     *
     * @param year The New Year
     */
    public void updateYear(int year) {
        updateDate(year, date.getMonth(), date.getDay());
    }

    /**
     * Gets the Month
     *
     * @return The current Month
     */
    public int getMonth() {
        return date.getMonth();
    }

    /**
     * Changes the Month
     *
     * @param month The new Month
     */
    public void setMonth(int month) {
        setDate(date.getYear(), month, date.getDay());
    }

    /**
     * Updates the Month
     *
     * @param month The new Month
     */
    public void updateMonth(int month) {
        updateDate(date.getYear(), month, date.getDay());
    }

    /**
     * Gets the Day
     *
     * @return The current Day
     */
    public int getDay() {
        return date.getDay();
    }

    /**
     * Changes the Day
     *
     * @param day The new Day
     */
    public void setDay(int day) {
        setDate(date.getYear(), date.getMonth(), day);
    }

    /**
     * Updates the Day
     *
     * @param day The new Day
     */
    public void updateDay(int day) {
        updateDate(date.getYear(), date.getMonth(), day);
    }

    /**
     * Sets the date
     *
     * @param year  The New Year
     * @param month The New Month
     * @param day   The New Day
     */
    public void setDate(int year, int month, int day) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        boolean needToUpdateSQL = (date != null);
        date = new Date(year, month, day);
        if (needToUpdateSQL) updateEvent(oldEvent, this);
    }

    /**
     * Sets the date
     *
     * @param year  The New Year
     * @param month The New Month
     * @param day   The New Day
     */
    public void updateDate(int year, int month, int day) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        date = new Date(year, month, day);
        updateEvent(oldEvent, this);
    }

    /**
     * Gets the Date
     *
     * @return The Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the Date using a String of the new Date, depending on the format of the String. Updates
     * Database if necessary
     *
     * @param date The new Date as a String
     */
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

    /**
     * Sets the Date of the Event based on the LocalDate
     *
     * @param localDate The New Date
     */
    public void setDate(LocalDate localDate) {
        this.date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    /**
     * Sets the Date of the Event based on the LocalDate, changing the Database if necessary
     *
     * @param localDate The New Date
     */
    public void updateDate(LocalDate localDate) {
        Event oldEvent = new Event(super.getStudent(), this.eventName, this.hours, this.date);
        this.date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        updateEvent(oldEvent, this);
    }

    /**
     * Sets the date given another Date
     *
     * @param date The new Date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the LocalDate (the new version of java.util.Date) of the current Date
     *
     * @return the LocalDate
     */
    public LocalDate getLocalDate() {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    /**
     * Adds the Current Event to the database, taking all changes made
     */
    public void addEvent() {
        try {
            addStudentHours(super.getStudent(), eventName, hours,
                    date.getYear(), date.getMonth()
                    , date.getDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This deletes the current event from the database, removing all traces
     */
    @Override
    public void delete() {
        MySQLMethods.delete(this);
    }

    /**
     * Formats the Event in an easy-to-read manner
     *
     * @return A String with the event Information
     */
    @Override
    public String toString() {
        return String.format("Name: %s, Hours: %s, Date: %s", eventName, hours, date);
    }

    /**
     * Updates the event with all the new values in the backend database.
     *
     * @param oldEvent the Old Event
     * @param newEvent the New Event
     */
    public void updateEvent(Event oldEvent, Event newEvent) {
        MySQLMethods.updateEvent(super.getStudent(), oldEvent, newEvent);
    }

    @Override
    public Student getStudent() {
        return this;
    }

    /**
     * Compares the dates of two events
     *
     * @param o The Second Event
     * @return An integer representing the difference
     */
    @Override
    public int compareTo(Event o) {
        return o.getDate().compareTo(this.getDate());
    }

    /**
     * Compares the dates of two events
     *
     * @param o1 The First Event
     * @param o2 The Second Event
     * @return An integer containing the difference
     */
    @Override
    public int compare(Event o1, Event o2) {
        return date.compare(o1.getDate(), o2.getDate());
    }
}
