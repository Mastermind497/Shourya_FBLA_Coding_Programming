package com.Frontend.Reports;

import com.Backend.*;
import com.Frontend.Charts;
import com.Frontend.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

import static com.Backend.MySQLMethods.round;

@Route(value = "report", layout = MainView.class)
@PageTitle("FBLA Genie | Generate Reports")
public class Reports extends VerticalLayout {
    final Button groupReport = new Button("Generate Group Reports", buttonClickEvent -> generateGroupReport());
    final Button individualReport = new Button("Generate Individual Reports", buttonClickEvent -> generateIndividualReport());

    public Reports() {
        removeAll();

        HorizontalLayout form = new HorizontalLayout();

        groupReport.setText("Generate Group Reports");
        groupReport.addThemeVariants(ButtonVariant.LUMO_LARGE);
        individualReport.setText("Generate Individual Reports");
        individualReport.addThemeVariants(ButtonVariant.LUMO_LARGE);

        form.add(groupReport, individualReport);

        form.setAlignItems(FlexComponent.Alignment.CENTER);
        form.setAlignSelf(FlexComponent.Alignment.CENTER);

        setAlignItems(Alignment.CENTER);
        add(form);
    }

    /**
     * This method returns a the pre-formatted data of a student. This simplifies the job significantly when
     * writing multiple pieces of data
     *
     * @param header The heading text of the piece of data
     * @param text   The piece of data
     * @return A Div containing the formatted Student Data
     */
    public static Div setText(String header, String text) {
        Div div = new Div();
        div.add(new H6(header + ":"));
        div.add(text);

        return div;
    }

    public void generateIndividualReport() {
        removeAll();
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        groupReport.setText("Generate Group Reports Instead");

        //Uses Size 1 Arrays To Allow Implementation in Lambda methods because they are effectively final
        Student[] selectedStudent = new Student[1];
        Date[] startingDate = new Date[1];
        String[] startingDateOption = new String[1];

        //Gets Student Data values and adds to a dropdown list
        List<Student> studentNames = MySQLMethods.getStudents();
        ComboBox<Student> studentSelect = new ComboBox<>();
        studentSelect.setLabel("Student");
        studentSelect.setItems(studentNames);
        studentSelect.addValueChangeListener(e -> selectedStudent[0] = studentSelect.getValue());

        VerticalLayout datePicker = new VerticalLayout();
        datePicker.setPadding(false);
        datePicker.setSpacing(false);

        ComboBox<String> dateOption = new ComboBox<>("Type of Date Selection", "Specific Start Date", "General Date Range");

        //Dynamically Changes the Date Selector Depending on Selected Date Option Tool
        dateOption.addValueChangeListener(onChange -> {
            try {
                form.remove(datePicker);
            } catch (Exception e) {
                //Do Nothing because there never was datePicker
            }
            datePicker.removeAll();
            if (onChange.getValue().contains("Specific")) {
                startingDateOption[0] = null;
                DatePicker startDate = new DatePicker("Get Events Starting From:");
                startDate.setClearButtonVisible(true);
                startDate.addValueChangeListener(e -> {
                    startingDate[0] = new Date();
                    startingDate[0].setDate(startDate.getValue());
                });
                startDate.setMax(LocalDate.now());
                startDate.setWidth("20em");
                datePicker.add(startDate);
            } else if (onChange.getValue().contains("General")) {
                startingDate[0] = null;
                ComboBox<String> rangeSelector = new ComboBox<>("Range of Data",
                        Charts.WEEK_CHART, Charts.MONTH_CHART, Charts.YEAR_CHART, Charts.ALL_TIME_CHART);
                rangeSelector.addValueChangeListener(rangeChange -> startingDateOption[0] = rangeChange.getValue());
                rangeSelector.setValue(Charts.ALL_TIME_CHART);
                rangeSelector.setRequired(true);
                rangeSelector.setWidth("20em");
                datePicker.add(rangeSelector);
            }
            datePicker.setAlignItems(Alignment.START);
            form.add(datePicker, 2);
        });

        form.add(groupReport, 1);
        form.add(studentSelect, 2);
        form.add(dateOption, 1);

        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        actions.add(save, reset);
        actions.setWidthFull();
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.setAlignSelf(FlexComponent.Alignment.CENTER);

        save.addClickListener(e -> {
            try {
                individualReport(selectedStudent[0], startingDate[0]);
            } catch (NullPointerException npe) {
                individualReport(selectedStudent[0], Date.optionToDate(startingDateOption[0]));
            }
        });

        reset.addClickListener(e -> {
            studentSelect.setValue(null);
            form.remove(datePicker);
        });

        actions.setJustifyContentMode(JustifyContentMode.CENTER);
        add(form, actions);
    }

    public static void nextSection(Board dataBoard) {
        //Adding two lines to signify the end of a section
        dataBoard.addRow(new Html("<hr>"));
        dataBoard.addRow(new Html("<hr>"));
    }

    public static Div makeBulletList(String label, List<StudentData> list) {
        Html header = new Html("<h4 style = \"color:#800517\">" + label + "</h4>");
        StringBuilder html = new StringBuilder("<ul style = \"color:#c11b17\">");
        for (StudentData s : list) {
            html.append("<li>").append(s).append("</li>");
        }
        html.append("</ul>");
        Html bullet = new Html(String.valueOf(html));
        Div div = new Div(header);
        div.add(bullet);
        return div;
    }

    /**
     * Calculates the hours remaining for a student to reach his or her community service goal
     *
     * @param student The Student who is being analyzed
     * @return A String containing formatted hours to go
     */
    private String getHoursRemaining(StudentData student) {
        String category = student.getCommunityServiceCategory();
        double hours = student.getCommunityServiceHours();
        if (category.contains("50")) {
            if (hours >= 50) return "Goal Reached! Next Goal: CSA Service (200 Hours)";
            else return (50 - hours) + " Hours To Go";
        } else if (category.contains("200")) {
            if (hours >= 200) return "Goal Reached! Next Goal: CSA Achievement (500 Hours)";
            else return (200 - hours) + " Hours To Go";
        } else {
            assert category.contains("500");
            if (hours >= 500) return "Completed the Highest Goal! Congratulations!";
            else return (500 - hours) + "To Go";
        }
    }

    public void generateGroupReport() {
        removeAll();
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        individualReport.setText("Generate Individual Reports Instead");

        Button selector = new Button("Generate Group Reports");
        selector.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        ComboBox<String> rangeSelector = new ComboBox<>("Range of Data", Charts.WEEK_CHART, Charts.MONTH_CHART, Charts.YEAR_CHART, Charts.ALL_TIME_CHART);
        rangeSelector.setValue(Charts.ALL_TIME_CHART);
        add(individualReport, rangeSelector, selector);
        selector.addClickListener(onClick -> {
            remove(selector);
            groupReport(rangeSelector.getValue());
        });
        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);
    }

    public void individualReport(Student student, Date startingDate) {
        removeAll();
        StudentData dataOfStudent = student.getStudentData();

        //Sees if any date was selected. If not, it returns the full Data Analysis
        List<Event> eventsList;
        if (startingDate.fakeDate()) {
            eventsList = MySQLMethods.selectStudentEventsAsEvent(student);
        } else {
            eventsList = MySQLMethods.selectStudentEventsInRange(student, startingDate);
        }
        double hoursInRange = Event.getTotalHours(eventsList);
        double totalHours = round(dataOfStudent.getCommunityServiceHours());

        //Adds space between edge of screen and content
        setPadding(true);
        setMargin(true);

        //Dropdown menu for all data
        Board dataBoard = new Board();

        H1 overviewHeading = new H1("Overview of " + student);
        //Heading to describe section
        dataBoard.addRow(overviewHeading);

        Div firstName = setText("First Name", dataOfStudent.getFirstName());
        Div lastName = setText("Last Name", dataOfStudent.getLastName());
        Div studentID = setText("Student ID", Integer.toString(dataOfStudent.getStudentID()));
        Div email = setText("Email", dataOfStudent.getEmail());
        Div grade = setText("Grade", Short.toString(dataOfStudent.getGrade()));
        Div lastEdited = setText("Last Edited", dataOfStudent.getLastEdited().toString());

        //First Row: Basic Overview
        dataBoard.addRow(firstName, lastName, studentID, grade);

        //Second Row: Supporting Information
        dataBoard.addRow(email, lastEdited);

        //Add a space between the second and third rows
        dataBoard.addRow(new Html("<hr>"));
        if (hoursInRange != totalHours) {
            Div communityServiceHoursInRange = setText("Community Service Hours in Selected Range",
                    Double.toString(hoursInRange));
            Div percentOfTotal = setText("Percentage of Total Hours", new Percent(hoursInRange, totalHours).toString());

            dataBoard.addRow(communityServiceHoursInRange, percentOfTotal);
        }

        Div communityServiceCategoryGoal = setText("Community Service Category Goal",
                dataOfStudent.getCommunityServiceCategory());
        Div communityServiceCategoryCurrent = setText("Current Community Service Category",
                dataOfStudent.getCurrentCommunityServiceCategory());
        Div communityServiceHours = setText("Total Community Service Hours",
                Double.toString(dataOfStudent.getCommunityServiceHours()));
        Div hoursToGoTillGoal = setText("Hours Left for Goal", getHoursRemaining(dataOfStudent));

        //Third Row: Gives Brief Overview of hours
        dataBoard.addRow(communityServiceHours, hoursToGoTillGoal, communityServiceCategoryCurrent, communityServiceCategoryGoal);

        //Adding two lines to signify the end of a section
        nextSection(dataBoard);

        H1 hourAnalysis = new H1("Progress Gauge");
        //Creates a Solid Gauge for each of the Different Awards
        VerticalLayout communityChartLayout = new VerticalLayout();
        Chart communityChart = Charts.solidGauge(dataOfStudent.getCommunityServiceHours(), 50, 2);
        H5 communityTitle = new H5("CSA Community");
        communityChartLayout.add(communityTitle, communityChart);
        communityChartLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout serviceChartLayout = new VerticalLayout();
        Chart serviceChart = Charts.solidGauge(dataOfStudent.getCommunityServiceHours(), 200, 3);
        H5 serviceTitle = new H5("CSA Service");
        serviceChartLayout.add(serviceTitle, serviceChart);
        serviceChartLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout achievementChartLayout = new VerticalLayout();
        Chart achievementChart = Charts.solidGauge(dataOfStudent.getCommunityServiceHours(), 500, 4);
        H5 achievementTitle = new H5("CSA Achievement");
        achievementChartLayout.add(achievementTitle, achievementChart);
        achievementChartLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //Row 1: Heading for Hour Analysis
        dataBoard.addRow(hourAnalysis);
        //Row 2: Charts depicting process through hours
        dataBoard.addRow(communityChartLayout, serviceChartLayout, achievementChartLayout);

        //Adding double lines to indicate the end of a section
        nextSection(dataBoard);

        H1 eventDetails = new H1("Event Details");
        dataBoard.addRow(eventDetails);
        Grid<Event> eventGrid = new Grid<>();
        eventGrid.setItems(eventsList);
        eventGrid.addColumn(Event::getEventName, "Name", "EventName").setHeader("Event Name");
        eventGrid.addColumn(Event::getHours, "double", "hours").setHeader("Event Hours");
        eventGrid.addColumn(Event::getDate, "Date").setHeader("Event Date");

        //Adjusts Column Sizes Automatically based on data inside
        for (Grid.Column<Event> al : eventGrid.getColumns()) {
            al.setAutoWidth(true);
        }

        eventGrid.setHeightByRows(true);
        eventGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        eventGrid.setMultiSort(true);

        dataBoard.addRow(eventGrid);
        if (Event.monthsInRange(eventsList.get(0).getDate(), eventsList.get(eventsList.size() - 1).getDate()) > 1) {
            Button viewChart = new Button("Show Monthly Hour Graph");
            Row questionRow = dataBoard.addRow(viewChart);
            viewChart.addClickListener(onClick -> {
                Chart hourLineGraph = Charts.monthLineGraph("Hours per Month", eventsList);
                dataBoard.add(new Html("<hr>"));
                dataBoard.add(hourLineGraph);
                dataBoard.removeRow(questionRow);
            });
        }

        add(dataBoard);
    }

    public void groupReport(String option) {
        removeAll();
        Board dataBoard = new Board();
        //This list contains all of the students in the chapter for easy reference for the rest of the report
        List<StudentData> studentDataList = MySQLMethods.getStudentData();
        List<StudentData> studentDataListInRange = MySQLMethods.getStudentData(option);
        List<StudentData> inactiveStudentList = StudentData.removeActive(studentDataListInRange);
        int activeMembers = 0;
        double totalHours = 0;
        for (StudentData s : studentDataListInRange) {
            if (s.getCommunityServiceHours() > 0) {
                activeMembers++;
                totalHours += s.getCommunityServiceHours();
            }
        }

        /* Section 1: General Information
         * This section is mainly for a brief overview. It will give a few numbers
         * and overarching data which is important for most when looking for basic information
         *
         * This section will include, but is not limited to:
         *   Number of Students
         *   Average Student Hours
         *   Average Student Hours Discounting all Zeros
         *   Number of Students without hours
         *   Average Community Service Category goal
         *   Average Achieved Community Service Category
         */
        H1 generalInformation = new H1("General Information");
        dataBoard.addRow(generalInformation);

        H2 chapterDetails = new H2("Chapter Details");
        Div numMembers = setText("Number of Chapter Members", Integer.toString(studentDataListInRange.size()));
        Div numActiveMembers = setText("Number of Active Members", Integer.toString(activeMembers)); //Note: An Active Member is someone who has more than 0 hours
        Div percentActiveMembers = setText("Percent of Members Active", new Percent(activeMembers, studentDataListInRange.size()).toString());
        Div numHours = setText("Total Hours In Chapter", Double.toString(totalHours));

        //Heading for Section
        dataBoard.addRow(chapterDetails);

        //Add A Few Details
        dataBoard.addRow(numMembers, numActiveMembers, percentActiveMembers, numHours);

        nextSection(dataBoard);

        /*
         * A Section for the Specific Member Details, such as Average Hours and such
         */
        H2 memberDetails = new H2("Member Details");
        Div averageHours = setText("Average Hours Per Student", Double.toString(round(totalHours / studentDataListInRange.size())));
        Div averageCategory = setText("Average Achieved Community Service Category", StudentData.getAverageCategory(studentDataListInRange));
        Div averageGoal = setText("Average Community Service Goal", StudentData.getAverageGoal(studentDataListInRange));

        Div averageActiveHours = setText("Average Hours Per Active Student", "0 (0 Active Students)");
        Div averageActiveCategory = setText("Average Achieved Community Service Category by Active Students", "None (No Active Students)");
        Div averageActiveGoal = setText("Average Community Service Goal of Active Students", "None (No Active Students)");

        if (activeMembers > 0) {
            averageActiveHours = setText("Average Hours Per Active Student", Double.toString(round(totalHours / activeMembers)));
            averageActiveCategory = setText("Average Achieved Community Service Category by Active Students", StudentData.getActiveCategory(studentDataListInRange));
            averageActiveGoal = setText("Average Community Service Goal of Active Students", StudentData.getActiveGoal(studentDataListInRange));
        }

        //Heading for Section
        dataBoard.addRow(memberDetails);

        //A Few general, all-student details
        dataBoard.addRow(averageHours, averageCategory, averageGoal);

        //More Specific Details with only activeStudents
        dataBoard.addRow(averageActiveHours, averageActiveCategory, averageActiveGoal);

        nextSection(dataBoard);

        //This next section is to help visualize the data
        Chart chart = Charts.contributionTreemapChart(option);
        dataBoard.addRow(chart);
        if (inactiveStudentList.size() > 0) {
            Div inactiveStudentsHTML = makeBulletList("Inactive Students", inactiveStudentList);
            dataBoard.addRow(inactiveStudentsHTML);
        }

        /*
         *  Section 2: Community Service Award Analysis
         *
         * This section is for an overview of the Community Service Award Categories and their achievements. This will
         * help in analyzing the goals and achievements of each student, differentiating between the active and inactive students
         *
         * This Section will include, but is not limited to:
         *    A Bar Graph for goals and achievements
         *    A Pie Chart for how many people achieved their goals
         */
        H1 csaAnalysis = new H1("Community Service Award Analysis");
        dataBoard.addRow(csaAnalysis);
        H2 evolutionChart = new H2("Community Service Division Bar/Pie Graph");

        Chart goalChart = Charts.barGraphCommunityServiceCategoryGoals(studentDataList);
        Chart achievedChart = Charts.barGraphCommunityServiceCategoryAchieved(studentDataList);

        dataBoard.addRow(evolutionChart);
        dataBoard.addRow(goalChart, achievedChart);
        dataBoard.addRow(new Html("<hr>"));
        dataBoard.addRow(Charts.goalDivisionChart(studentDataList, "Student Goal Division"),
                Charts.currentDivisionChart(studentDataList, "Student Current Division"));

        nextSection(dataBoard);

        dataBoard.addRow(new H2("Community Service Goal Achievement Analysis"));
        dataBoard.addRow(Charts.achievedGoalPieChart(studentDataList, "Students Who Achieved vs Didn't Achieve Their Goals"));


        add(dataBoard);
    }
}