package com.Backend;

import java.math.BigDecimal;

public class Percent {
    private BigDecimal percent;

    public Percent(int current, int max) {
        double percent = (double) current / max;
        this.percent = BigDecimal.valueOf(round(percent));
    }

    public Percent(double current, double max) {
        double percent = current / max;
        this.percent = BigDecimal.valueOf(round(percent));
    }

    public static double round(double toBeRounded) {
        toBeRounded *= 100;
        int inProgress = (int) toBeRounded;
        return inProgress / 100.0;
    }

    public double getPercent() {
        return percent.doubleValue();
    }
}
