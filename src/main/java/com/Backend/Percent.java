package com.Backend;

import java.math.BigDecimal;

public class Percent extends Number {
    private final BigDecimal percent;

    public Percent(int current, int max) {
        double percent = (double) current / max;
        this.percent = BigDecimal.valueOf(round(percent * 100));
    }

    public Percent(double current, double max) {
        double percentage = current / max;
        this.percent = BigDecimal.valueOf(round(percentage * 100));
    }

    public static double round(double toBeRounded) {
        return (int) (toBeRounded + 0.5);
    }

    public double getPercent() {
        return percent.doubleValue();
    }

    @Override
    public String toString() {
        return percent.toString() + "%";
    }

    @Override
    public int intValue() {
        return percent.toBigInteger().intValue();
    }

    @Override
    public long longValue() {
        return percent.toBigIntegerExact().intValueExact();
    }

    @Override
    public float floatValue() {
        return (float) percent.doubleValue();
    }

    @Override
    public double doubleValue() {
        return percent.doubleValue();
    }
}
