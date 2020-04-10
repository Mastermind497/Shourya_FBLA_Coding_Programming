package com.Backend;

import java.math.BigDecimal;

public class Percent {
    private BigDecimal percent;

    public Percent(int current, int max) {
        double percent = (double) current / max;
        this.percent = BigDecimal.valueOf(round(percent));
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
}
