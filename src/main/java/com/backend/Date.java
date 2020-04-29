package com.backend;

import com.frontend.Charts;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * Creates a date class to store the date. This would allow easy
 * storage and manipulation of the date
 */
public class Date implements Comparator<Date>, Comparable<Date>, Cloneable {
    /**
     * The Year in the Date
     */
    private int year;

    /**
     * The Month in the Date
     */
    private int month;

    /**
     * The Year in the Date
     */
    private int day;

    /**
     * A Variable Used When Declaring a Date which might be null
     */
    private boolean noDate = false;

    /**
     * Constructs a date given all its information
     *
     * @param year  Year
     * @param month Month
     * @param day   Day
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Constructs a Date of the First Day of the Common Era, to use as a Minimum Date
     */
    public Date() {
        this(0, 1, 1);
    }

    /**
     * A Date which starts off as "Fake" because it hasn't been given any value
     *
     * @param noDate A boolean telling whether or not the date is Fake
     */
    public Date(boolean noDate) {
        this.noDate = noDate;
    }

    /**
     * Takes the options included in Charts.java and returns their related Date values
     * <p>
     * This is used for Dates which are based on the String Values, such as Week, Month, and Year. This typicaly is useful
     * in reports when the user is choosing a date range
     *
     * @param s The Option Selected
     * @return A Date with the constraints put on by the Option
     */
    public static Date optionToDate(String s) {
        switch (s) {
            case Charts.WEEK_CHART:
                Date dateWeek = new Date();
                dateWeek.setDate(LocalDate.now().minusDays(7));
                return dateWeek;
            case Charts.MONTH_CHART:
                Date dateMonth = new Date();
                dateMonth.setDate(LocalDate.now().minusMonths(1));
                return dateMonth;
            case Charts.YEAR_CHART:
                Date dateYear = new Date();
                dateYear.setDate(LocalDate.now().minusYears(1));
                return dateYear;
            default: //Includes All Time
                return new Date();
        }
    }

    /**
     * Converts a String of the month to its respective Integer values
     * <p>
     * This method allows the use of String-based months alongside their Integer-based
     * counterparts, allowing easy conversion in one direction.
     *
     * @param month The month as a string
     * @return The integer value of the month
     */
    public static int getMonth(String month) {
        month = month.toLowerCase();
        System.out.print("Month: " + month);
        switch (month) {
            case "january":
            case "jan":
                return 1;
            case "february":
            case "feb":
            case "f":
                return 2;
            case "march":
            case "mar":
                return 3;
            case "april":
            case "ap":
            case "apr":
                return 4;
            case "may":
                return 5;
            case "june":
            case "jun":
                return 6;
            case "july":
            case "jul":
                return 7;
            case "august":
            case "aug":
                return 8;
            case "september":
            case "sept":
                return 9;
            case "october":
            case "oct":
                return 10;
            case "november":
            case "nov":
                return 11;
            case "december":
            case "dec":
                return 12;
            default:
                return -1;
        }
    }

    /**
     * Converts an Integer of the month to its respective String-based version
     * <p>
     * This method allows the use of String-based months alongside their Integer-based
     * counterparts, allowing easy conversion in one direction.
     *
     * @param month The month as an Integer
     * @return The String value of the month
     */
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

    /**
     * Returns a copy of the Date without changing the original
     * <p>
     * This overrides the super method of clone to work for Date
     *
     * @return A copy of the Date
     */
    @Override
    protected Date clone() {
        try {
            return (Date) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Cloning Not Supported");
            return this;
        }
    }

    /**
     * Gets the value of the "Fake Date" variable
     *
     * @return The "Fake Date" value
     */
    public boolean fakeDate() {
        return noDate;
    }

    /**
     * Gets the Year
     *
     * @return the current Year
     */
    public int getYear() {
        return year;
    }

    /**
     * Changes the Year
     *
     * @param year The New Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the Month
     *
     * @return The current Month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Changes the Month
     *
     * @param month The new Month
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Gets the Day
     *
     * @return The current Day
     */
    public int getDay() {
        return day;
    }

    /**
     * Changes the Day
     *
     * @param day The new Day
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Takes a Date in the String format and converts it into a Date object.
     *
     * @param date the Date as a String
     * @apiNote For this method to work, the date has to be in the format of yyyy-mm-dd
     */
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

    /**
     * Returns the LocalDate of the Current Date
     *
     * @return The LocalDate of the Date
     */
    public LocalDate getLocalDate() {
        return LocalDate.of(year, month, day);
    }

    /**
     * Uses the modern version of Date in java, LocalDate, and turns it into the backend Date.
     *
     * @param date The LocalDate being converted.
     */
    public void setDate(LocalDate date) {
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.day = date.getDayOfMonth();
    }

    /**
     * Returns the month value as its respective string without the need to direct to static methods.
     *
     * @return The String value of the Month
     */
    public String getMonthString() {
        return getMonth(month);
    }

    /**
     * Prints out the Date as a String in dd, MM, yyyy format
     *
     * @return The Entire Date as a String
     */
    @Override
    public String toString() {
        return day + " " + getMonthString() + ", " + year;
    }

    /**
     * Uses the standard format of a Date, yyyy-mm-dd, and converts
     *
     * @return A String with the Date in the Standard Format
     */
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

    /**
     * Compares the current date with another to see which one comes first
     *
     * @param o The date compare with
     * @return An integer representing the difference in date values
     */
    @Override
    public int compareTo(Date o) {
        if (this.year != o.getYear()) {
            Integer year = o.getYear();
            return year.compareTo(this.getYear());
        } else {
            if (this.month != o.getMonth()) {
                Integer month = o.getMonth();
                return month.compareTo(this.getMonth());
            } else {
                Integer day = o.getDay();
                return day.compareTo(this.getDay());
            }
        }
    }

    /**
     * Compares two dates, o1 and o2, to find which one comes first
     *
     * @param o1 The First Date Being compared
     * @param o2 The Second Date Being Compared
     * @return An integer containing the comparison
     */
    @Override
    public int compare(Date o1, Date o2) {
        if (o2.getYear() != o1.getYear()) {
            return o2.getYear() - o1.getYear();
        } else {
            if (o2.getMonth() != o1.getMonth()) {
                return o2.getMonth() - o1.getMonth();
            } else {
                return o2.getDay() - o1.getDay();
            }
        }
    }

    /**
     * Compares to see if two dates are the same
     *
     * @param date the Date compared
     * @return Whether they are equal or not
     */
    public boolean equals(Date date) {
        return (this.day == date.getDay()) && (this.month == date.getMonth()) && (this.year == date.getYear());
    }

    /**
     * Compares to see if two dates are equal, ignoring the day
     *
     * @param date The date being compared
     * @return Whether they are equal
     */
    public boolean equalsNoDay(Date date) {
        return (this.month == date.getMonth()) && (this.year == date.getYear());
    }

    /**
     * Increments the Day by One, Changing the Month when necessary
     */
    public void incrementDay() {
        LocalDate date = LocalDate.of(year, month, day).plusDays(1);
        day = date.getDayOfMonth();
        month = date.getMonthValue();
        year = date.getYear();
    }

    /**
     * Increments the month by one, changing the year if it hits 13
     */
    public void incrementByMonth() {
        if (month < 12) month++;
        else {
            month = 0;
            year++;
        }
    }

    /**
     * Increments the Year by 1
     */
    public void incrementYear() {
        year++;
    }
}
