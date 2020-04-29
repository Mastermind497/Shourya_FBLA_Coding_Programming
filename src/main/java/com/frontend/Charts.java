package com.frontend;

import com.backend.Event;
import com.backend.MySQLMethods;
import com.backend.Percent;
import com.backend.StudentData;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A Class to help make making classes much easier
 */
public class Charts {

    /**
     * A String identifying the option range of "Week"
     */
    public static final String WEEK_CHART = "Week";
    /**
     * A String identifying the option range of "Month"
     */
    public static final String MONTH_CHART = "Month";
    /**
     * A String identifying the option range of "Year"
     */
    public static final String YEAR_CHART = "Year";
    /**
     * A String identifying the option range of "All Time"
     */
    public static final String ALL_TIME_CHART = "All Time";

    /**
     * Creates a solid gauge chart for the hours a student has compared to a certain category,
     *
     * @param current    The Current Number of Hours
     * @param max        The Max Possible Number of Hours, or hours to achieve an award
     * @param colorIndex The Color of the Chart
     * @return A Solid Gauge Chart
     */
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

    /**
     * Generates a Line Graph so that, for long-term reports, hours can be tracked over time to analyze
     * consistent growth
     *
     * @param title  The Chart Title
     * @param events The List of Events the chart should encompass
     * @return A Line Graph
     */
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

    /**
     * Generates a TreeMap chart to help divide which students contributed the most in a certain date range
     *
     * @param studentDataList The List of Students to be included in the chart
     * @param chartType       The type of chart, as in the date it covers
     * @return A TreeMap Char
     */
    public static Chart contributionTreeMapChart(List<StudentData> studentDataList, String chartType) {
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

    /**
     * Generates a TreeMap chart to help divide which students contributed the most in a certain date range
     * <p>
     * Automatically gets the Student Hour data based on the Chart Type Provided
     *
     * @param chartType The type of chart, as in the date it covers
     * @return A TreeMap Char
     */
    public static Chart contributionTreeMapChart(String chartType) {
        return contributionTreeMapChart(MySQLMethods.getStudentData(chartType), chartType);
    }

    /**
     * Generates Data from each of the students and places it in the respective categories int the TreeMap Chart
     *
     * @param studentDataList The List of Students and their hours in the range
     * @param chartType       The range of time for the chart to determine how many hours are good vs bad
     * @return Configured Data for the TreeMap Chart to use
     */
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

    /**
     * Generates a bar graph with student's Community Service Category Goals
     *
     * @param studentDataList The List of Students
     * @return A Bar Graph
     */
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

        return completeChart(chart, configuration);
    }

    /**
     * Adds data to the Goals Bar Graph
     *
     * @param configuration   The Configuration of the Bar Graph
     * @param studentDataList The List of Students
     */
    private static void addGoalsBar(Configuration configuration, List<StudentData> studentDataList) {
        List<Number> activeStudents = Arrays.asList(0, 0, 0);
        List<Number> inactiveStudents = Arrays.asList(0, 0, 0);
        List<Number> totalStudents = Arrays.asList(0, 0, 0);

        for (StudentData s : studentDataList) {
            int type = s.getCommunityServiceCategoryInt();
            totalStudents.set(type - 1, totalStudents.get(type - 1).intValue() + 1);
            if (s.isActive()) activeStudents.set(type - 1, activeStudents.get(type - 1).intValue() + 1);
            else inactiveStudents.set(type - 1, inactiveStudents.get(type - 1).intValue() + 1);
        }

        configuration.addSeries(new ListSeries("Active Students", activeStudents));
        configuration.addSeries(new ListSeries("Inactive Students", inactiveStudents));
        configuration.addSeries(new ListSeries("Total Students", totalStudents));
    }

    /**
     * Generates a Bar Graph for the number of Community Service Award's achieved by students.
     * <p>
     * In this graph, if a student achieved a higher award, they are ignored in the lower award.
     *
     * @param studentDataList The List of Students
     * @return A Bar Graph
     */
    public static Chart barGraphCommunityServiceCategoryAchieved(List<StudentData> studentDataList) {
        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Community Service Category Analysis");
        configuration.setSubTitle("Community Service Category Achieved");
        chart.getConfiguration().getChart().setType(ChartType.COLUMN);

        addCurrentBar(configuration, studentDataList);

        XAxis x = new XAxis();
        x.setCrosshair(new Crosshair());
        x.setCategories("None", "CSA Community (50 Hours)", "CSA Service (200 Hours)", "CSA Achievement (500 Hours)");
        configuration.addxAxis(x);
        return completeChart(chart, configuration);
    }

    /**
     * Generates data division for the Achievement Bar Graph
     *
     * @param configuration   The Bar Graphs configuration type
     * @param studentDataList The Student Data
     */
    private static void addCurrentBar(Configuration configuration, List<StudentData> studentDataList) {
        List<Number> activeStudents = Arrays.asList(0, 0, 0, 0);
        List<Number> inactiveStudents = Arrays.asList(0, 0, 0, 0);
        List<Number> totalStudents = Arrays.asList(0, 0, 0, 0);

        for (StudentData s : studentDataList) {
            int type = s.getCurrentCommunityServiceCategoryInt();
            totalStudents.set(type, totalStudents.get(type).intValue() + 1);
            if (s.isActive()) activeStudents.set(type, activeStudents.get(type).intValue() + 1);
            else inactiveStudents.set(type, inactiveStudents.get(type).intValue() + 1);
        }

        configuration.addSeries(new ListSeries("Active Students", activeStudents));
        configuration.addSeries(new ListSeries("Inactive Students", inactiveStudents));
        configuration.addSeries(new ListSeries("Total Students", totalStudents));
    }

    /**
     * This completes the y axis and tooltip for the Bar Graphs
     *
     * @param chart         The Chart
     * @param configuration The Chart's configuration
     * @return The Final Chart with Configuration
     */
    private static Chart completeChart(Chart chart, Configuration configuration) {
        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Number of Students");
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        configuration.setTooltip(tooltip);

        return chart;
    }

    /**
     * A Pie Chart to show the number of student's who achieved their goals vs the number of those who didn't
     *
     * @param list  All of the students
     * @param title The title of the chart
     * @return The Pie Chart of Divisions
     */
    public static Chart achievedGoalPieChart(List<StudentData> list, String title) {
        Chart chart = new Chart(ChartType.PIE);

        Configuration configuration = chart.getConfiguration();

        configuration.setTitle(title);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        configuration.setTooltip(tooltip);

        PlotOptionsPie plotOptionsPie = new PlotOptionsPie();
        plotOptionsPie.setAllowPointSelect(true);
        plotOptionsPie.setCursor(Cursor.POINTER);
        plotOptionsPie.setShowInLegend(true);
        configuration.setPlotOptions(plotOptionsPie);

        DataSeries series = new DataSeries();
        int achieved = 0;
        int not = 0;
        for (StudentData s : list) {
            if (s.achievedGoal()) achieved++;
            else not++;
        }
        series.add(new DataSeriesItem("Achieved Goal", achieved));
        series.add(new DataSeriesItem("Not Achieved Goal", not));
        configuration.setSeries(series);
        chart.setVisibilityTogglingDisabled(true);

        return chart;
    }

    /**
     * Shows the different goals each student has in a Pie Chart format
     *
     * @param list  The list of students
     * @param title The title of the chart
     * @return The Pie Chart
     */
    public static Chart goalDivisionChart(List<StudentData> list, String title) {
        Chart chart = new Chart(ChartType.PIE);

        Configuration configuration = chart.getConfiguration();

        configuration.setTitle(title);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        configuration.setTooltip(tooltip);

        PlotOptionsPie plotOptionsPie = new PlotOptionsPie();
        plotOptionsPie.setAllowPointSelect(true);
        plotOptionsPie.setCursor(Cursor.POINTER);
        plotOptionsPie.setShowInLegend(true);
        configuration.setPlotOptions(plotOptionsPie);

        configuration.setSeries(goalsPie(list));
        chart.setVisibilityTogglingDisabled(true);

        return chart;
    }

    /**
     * Adds Data to the Goal Pie Chart
     *
     * @param studentDataList The List of Students
     * @return The Data for the Pie Chart
     */
    private static DataSeries goalsPie(List<StudentData> studentDataList) {
        int community = 0, service = 0, achievement = 0;

        for (StudentData s : studentDataList) {
            if (s.getCommunityServiceCategory().toUpperCase().contains("COMMUNITY")) community++;
            else if (s.getCommunityServiceCategory().toUpperCase().contains("SERVICE")) service++;
            else achievement++;
        }

        DataSeries series = new DataSeries();

        series.add(new DataSeriesItem("CSA Community (50 Hours)", community));
        series.add(new DataSeriesItem("CSA Service (200 Hours)", service));
        series.add(new DataSeriesItem("CSA Achievement (500 Hours)", achievement));

        return series;
    }

    /**
     * A Pie Chart to show the division between the currently achieved goals of students
     *
     * @param list  The Students
     * @param title The Title of the Chart
     * @return The Pie Chart
     */
    public static Chart currentDivisionChart(List<StudentData> list, String title) {
        Chart chart = new Chart(ChartType.PIE);

        Configuration configuration = chart.getConfiguration();

        configuration.setTitle(title);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        configuration.setTooltip(tooltip);

        PlotOptionsPie plotOptionsPie = new PlotOptionsPie();
        plotOptionsPie.setAllowPointSelect(true);
        plotOptionsPie.setCursor(Cursor.POINTER);
        plotOptionsPie.setShowInLegend(true);
        configuration.setPlotOptions(plotOptionsPie);

        configuration.setSeries(pieCurrent(list));
        chart.setVisibilityTogglingDisabled(true);

        return chart;
    }

    /**
     * Formats the data from the student data list to work in a Pie Chart
     *
     * @param studentDataList The Student Data List
     * @return A DataSeries for the Pie Chart
     */
    private static DataSeries pieCurrent(List<StudentData> studentDataList) {
        int[] csaCategory = new int[4];

        for (StudentData s : studentDataList) {
            csaCategory[s.getCurrentCommunityServiceCategoryInt()]++;
        }

        DataSeries series = new DataSeries();

        series.add(new DataSeriesItem("None", csaCategory[0]));
        series.add(new DataSeriesItem("CSA Community (50 Hours)", csaCategory[1]));
        series.add(new DataSeriesItem("CSA Service (200 Hours)", csaCategory[2]));
        series.add(new DataSeriesItem("CSA Achievement (500 Hours)", csaCategory[3]));

        return series;
    }

    /**
     * A simple rounding method to round to the nearest integer.
     *
     * @param toRound The Double that needs to be rounded
     * @return The Rounded Integer
     */
    private static int round(double toRound) {
        toRound += 0.5;
        return (int) toRound;
    }
}
