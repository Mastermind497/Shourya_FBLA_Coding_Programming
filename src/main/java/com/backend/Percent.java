package com.backend;

import java.math.BigDecimal;

/**
 * A Class to Use for Percent Data Types
 */
public class Percent extends Number implements Cloneable {

    /**
     * The Percentage in BigDecimal Form
     */
    private final BigDecimal percent;

    /**
     * A Constructor that takes in integers and generates a percentage based on them
     * <p>
     * It calculates percentage in the form current / total
     *
     * @param current The Current Value
     * @param max     The Total Value
     */
    public Percent(int current, int max) {
        double percent = (double) current / max;
        this.percent = BigDecimal.valueOf(round(percent * 100));
    }

    /**
     * A Constructor that takes in doubles and generates a percentage based on them
     * <p>
     * It calculates percentage in the form current / total
     *
     * @param current The Current Value
     * @param max     The Total Value
     */
    public Percent(double current, double max) {
        double percentage = current / max;
        this.percent = BigDecimal.valueOf(round(percentage * 100));
    }

    /**
     * Rounds to the nearest integer, as none of the used percentages require much precision
     *
     * @param toBeRounded the double before rounding
     * @return The rounded double
     */
    public static double round(double toBeRounded) {
        return (int) (toBeRounded + 0.5);
    }

    /**
     * Returns the Percent as a Double
     *
     * @return the Percent as a Double
     */
    public double getPercent() {
        return percent.doubleValue();
    }

    /**
     * Allows Printing as a String
     *
     * @return A String with the correct formatting
     */
    @Override
    public String toString() {
        return percent.toString() + "%";
    }

    /**
     * Gets the integer value of the percentage
     *
     * @return the integer value of the percentage
     */
    @Override
    public int intValue() {
        return percent.toBigInteger().intValue();
    }

    /**
     * Gets the long value of the percentage
     *
     * @return the long value of the percentage
     */
    @Override
    public long longValue() {
        return percent.toBigIntegerExact().intValueExact();
    }

    /**
     * Gets the float value of the percentage
     *
     * @return the float value of the percentage
     */
    @Override
    public float floatValue() {
        return (float) percent.doubleValue();
    }

    /**
     * Just returns the double value of the percentage
     *
     * @return the double value of the percentage
     */
    @Override
    public double doubleValue() {
        return percent.doubleValue();
    }

    @Override
    protected Percent clone() throws CloneNotSupportedException {
        return (Percent) super.clone();
    }
}
