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
        int inProgress = (int) (toBeRounded * 100);
        return inProgress / 100.0;
    }

    public double getPercent() {
        return percent.doubleValue();
    }
}
