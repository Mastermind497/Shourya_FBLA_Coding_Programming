package com.backend;

import java.util.StringTokenizer;

/**
 * Creates a date class to store the date. This would allow easy
 * storage and manipulation of the date
 */
public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date() {

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

    public static String getMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;
        }
    }

    public void setDate(String date) {
        StringTokenizer st = new StringTokenizer(date, "-");
        int year = Integer.parseInt(st.nextToken());
        if (year > 1900) {
            this.setYear(year);
        } else {
            this.setYear(year + 1900);
        }
        this.setMonth(Integer.parseInt(st.nextToken()));
        this.setDay(Integer.parseInt(st.nextToken()));
    }

    public String getMonthString() {
        return getMonth(month);
    }

    @Override
    public String toString() {
        return day + " " + getMonthString() + ", " + year;
    }
}
