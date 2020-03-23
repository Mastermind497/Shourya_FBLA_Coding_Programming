package com.Backend;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * Creates a date class to store the date. This would allow easy
 * storage and manipulation of the date
 */
public class Date implements Comparator<Date>, Comparable<Date> {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date() {
        this.year = 0;
        this.month = 1;
        this.day = 1;
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

    public static int getMonth(String month) {
        month = month.toLowerCase();
        System.out.print("Month: " + month);
        switch (month) {
            case "january":
            case "jan":
                System.out.println(1);
                return 1;
            case "february":
            case "feb":
            case "f":
                System.out.println(2);
                return 2;
            case "march":
            case "mar":
                System.out.println(3);
                return 3;
            case "april":
            case "ap":
            case "apr":
                System.out.println(4);
                return 4;
            case "may":
                System.out.println(5);
                return 5;
            case "june":
            case "jun":
                System.out.println(6);
                return 6;
            case "july":
            case "jul":
                System.out.println(7);
                return 7;
            case "august":
            case "aug":
                System.out.println(8);
                return 8;
            case "september":
            case "sept":
                System.out.println(9);
                return 9;
            case "october":
            case "oct":
                System.out.println(10);
                return 10;
            case "november":
            case "nov":
                System.out.println(11);
                return 11;
            case "december":
            case "dec":
                System.out.println(12);
                return 12;
            default:
                System.out.println(" None");
                return -1;
        }
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
        System.out.println(year);
        if (year > 1900) {
            this.setYear(year);
        } else {
            this.setYear(year + 1900);
        }
        this.setMonth(Integer.parseInt(st.nextToken()));
        this.setDay(Integer.parseInt(st.nextToken()));
    }

    public void setDate(LocalDate date) {
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.day = date.getDayOfMonth();
    }

    public String getMonthString() {
        return getMonth(month);
    }

    @Override
    public String toString() {
        return day + " " + getMonthString() + ", " + year;
    }

    public String toStringRegular() {
        String year = "" + this.year;

        String month = "";
        if (this.month < 10) {
            month += "0";
        }
        month += this.month;

        String day = "";
        if (this.day < 10) {
            day += "0";
        }
        day += this.day;

        return year + "-" + month + "-" + day;
    }

    @Override
    public int compareTo(Date o) {
        if (this.year != o.getYear()) {
            return this.year - o.getYear();
        }
        else {
            if (this.month != o.getMonth()) {
                return this.month - o.getMonth();
            }
            else {
                return this.day - o.getDay();
            }
        }
    }

    @Override
    public int compare(Date o1, Date o2) {
        if (o1.getYear() != o2.getYear()) {
            return o1.getYear() - o2.getYear();
        }
        else {
            if (o1.getMonth() != o2.getMonth()) {
                return o1.getMonth() - o2.getMonth();
            }
            else {
                return o1.getMonth() - o2.getDay();
            }
        }
    }
}
