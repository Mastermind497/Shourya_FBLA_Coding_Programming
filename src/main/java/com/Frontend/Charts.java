package com.Frontend;

import com.Backend.Event;
import com.Backend.MySQLMethods;
import com.Backend.Percent;
import com.Backend.StudentData;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Charts {

    public static final String WEEK_CHART = "Week";
    public static final String MONTH_CHART = "Month";
    public static final String YEAR_CHART = "Year";
    public static final String ALL_TIME_CHART = "All Time";

    public static Chart solidGauge(double current, double max, int colorIndex) {
        Percent percent = new Percent(current, max);
        Chart chart = new Chart(ChartType.SOLIDGAUGE);

        Configuration configuration = chart.getConfiguration();
        Pane pane = configuration.getPane();
        pane.setCenter(new String[]{"50%", "50%"});

        //Change depending on how the gauge should be (more open or closed)
        pane.setStartAngle(-90);
        pane.setEndAngle(90);

        //Creates an arced background for the Gauge
        Background paneBackground = new Background();
        paneBackground.setInnerRadius("60%");
        paneBackground.setOuterRadius("100%");
        paneBackground.setShape(BackgroundShape.ARC);
        pane.setBackground(paneBackground);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTickAmount(2);
        yAxis.setTitle("Percent Completion");
        //No small ticks
        yAxis.setMinorTickInterval("null");
        yAxis.getTitle().setY(-50);
        yAxis.getLabels().setY(16);
        yAxis.setExtremes(0, 100, true);
        yAxis.setMaxPadding(0);


        PlotOptionsSolidgauge plotOptionsSolidgauge = new PlotOptionsSolidgauge();

        DataLabels dataLabels = plotOptionsSolidgauge.getDataLabels();
        dataLabels.setY(5);
        dataLabels.setUseHTML(true);

        configuration.setPlotOptions(plotOptionsSolidgauge);

        DataSeries series = new DataSeries("Percent");

        DataSeriesItem item = new DataSeriesItem();
        item.setY(percent.getPercent());
        item.setColorIndex(colorIndex);
        item.setClassName("Percent");
        DataLabels dataLabelsSeries = new DataLabels();
        dataLabelsSeries.setFormat("<div style=\"text-align:center\"><span style=\"font-size:25px;"
                + "color:black' + '\">{y}</span><br/>"
                + "<span style=\"font-size:12px;color:silver\">" + "Percent" + "</span></div>");

        item.setDataLabels(dataLabelsSeries);

        series.add(item);

        configuration.addSeries(series);

        return chart;
    }

    public static Chart monthLineGraph(String title, List<Event> events) {
        Collections.sort(events);

        final Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();

        configuration.setTitle(title);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle("Hours");
        configuration.addyAxis(yAxis);

        String[] xAxisLabels = Event.getMonthsWithYear(events.get(0).getDate(), events.get(events.size() - 1).getDate());
        XAxis xAxis = configuration.getxAxis();
        xAxis.setTitle("Months");
        xAxis.setCategories(xAxisLabels);
        configuration.addxAxis(xAxis);

        //The Data for the Chart
        ListSeries series = new ListSeries("Months");
        List<Number> hours = new ArrayList<>(xAxisLabels.length);
        //Creates a list of hours with all of the hours allocated such that it matches with the month labels
        int currentMonth = events.get(0).getMonth();
        int currentYear = events.get(0).getYear();
        double sum = 0;
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            if (e.getMonth() == currentMonth && e.getYear() == currentYear) {
                sum += e.getHours();
            } else {
                hours.add(sum);
                sum = 0;
                currentMonth++;
                if (currentMonth > 12) {
                    currentMonth = 0;
                    currentYear++;
                }
                i--;
            }
        }
        //Last Sum because it doesn't get added in the loop
        hours.add(sum);
        series.setData(hours);
        configuration.addSeries(series);

        return chart;
    }

    public static Chart contributionTreemapChart(List<StudentData> studentDataList, String chartType) {
        Chart chart = new Chart(ChartType.TREEMAP);

        Configuration configuration = chart.getConfiguration();
        configuration.getTooltip().setEnabled(true);

        PlotOptionsTreemap plotOptions = new PlotOptionsTreemap();
        plotOptions.setLayoutAlgorithm(TreeMapLayoutAlgorithm.STRIPES); //test different values to see what works best
        plotOptions.setAlternateStartingDirection(false);

        Level level1 = new Level();
        level1.setLevel(1);
        level1.setLayoutAlgorithm(TreeMapLayoutAlgorithm.SLICEANDDICE);

        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setAlign(HorizontalAlign.LEFT);
        dataLabels.setVerticalAlign(VerticalAlign.TOP);

        level1.setDataLabels(dataLabels);
        plotOptions.setLevels(level1);

        TreeSeries series = createTreeSeries(studentDataList, chartType);
        series.setPlotOptions(plotOptions);

        chart.getConfiguration().addSeries(series);

        chart.getConfiguration().setTitle("Student Hours (Rounded to the nearest Integer)");

        return chart;
    }

    public static Chart contributionTreemapChart(String chartType) {
        return contributionTreemapChart(MySQLMethods.getStudentData(chartType), chartType);
    }

    private static TreeSeries createTreeSeries(List<StudentData> studentDataList, String chartType) {
        TreeSeries series = new TreeSeries();

        int low, med;

        switch (chartType) {
            case YEAR_CHART:
            case ALL_TIME_CHART:
                low = 25;
                med = 75;
                break;
            default:
                low = 5;
                med = 10;
                break;
        }

        TreeSeriesItem hoursLow = new TreeSeriesItem("HL", low + " Or Less Hours");
        //Red
        hoursLow.setColorIndex(5);

        TreeSeriesItem hoursMed = new TreeSeriesItem("HM", med + " Or Less Hours");
        //Orange
        hoursMed.setColorIndex(3);

        TreeSeriesItem hoursHigh = new TreeSeriesItem("HH", "More than " + med + "hours");
        //Green
        hoursHigh.setColorIndex(2);
        series.addAll(hoursLow, hoursMed, hoursHigh);

        for (StudentData student : studentDataList) {
            if (student.getCommunityServiceHours() <= low) {
                series.add(new TreeSeriesItem(student.toString(), hoursLow, round(student.getCommunityServiceHours())));
            } else if (student.getCommunityServiceHours() <= med) {
                series.add(new TreeSeriesItem(student.toString(), hoursMed, round(student.getCommunityServiceHours())));
            } else {
                series.add(new TreeSeriesItem(student.toString(), hoursHigh, round(student.getCommunityServiceHours())));
            }
        }

        return series;
    }

    public static Chart barGraphCommunityServiceCategoryGoals(List<StudentData> studentDataList) {
        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Community Service Category Analysis");
        configuration.setSubTitle("Community Service Goals");
        chart.getConfiguration().getChart().setType(ChartType.COLUMN);

        addGoalsBar(configuration, studentDataList);

        XAxis x = new XAxis();
        x.setCrosshair(new Crosshair());
        x.setCategories("CSA Community (50 Hours)", "CSA Service (200 Hours)", "CSA Achievement (500 Hours)");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Number of Students");
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        configuration.setTooltip(tooltip);

        return chart;
    }

    private static void addGoalsBar(Configuration configuration, List<StudentData> studentDataList) {
        List<Integer> activeStudents = Arrays.asList(0, 0, 0);
        List<Integer> inactivestudents = Arrays.asList(0, 0, 0);
        List<Integer> totalStudents = Arrays.asList(0, 0, 0);

        for (StudentData s : studentDataList) {
            int type = s.getCommunityServiceCategoryInt();
            totalStudents.set(type - 1, totalStudents.get(type - 1) + 1);
            if (s.isActive()) activeStudents.set(type - 1, activeStudents.get(type - 1) + 1);
            else inactivestudents.set(type - 1, inactivestudents.get(type - 1) + 1);
        }

        configuration.addSeries(new ListSeries("Active Students", (Number) activeStudents));
        configuration.addSeries(new ListSeries("Inactive Students", (Number) inactivestudents));
        configuration.addSeries(new ListSeries("Total Students", (Number) totalStudents));
    }

    private static int round(double toRound) {
        toRound += 0.5;
        return (int) toRound;
    }
}
