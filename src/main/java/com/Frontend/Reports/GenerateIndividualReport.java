package com.Frontend.Reports;

import com.Backend.*;
import com.Frontend.Charts;
import com.Frontend.Home;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route("individual-reports")
@PageTitle("Generate Reports | FBLA Genie")
public class GenerateIndividualReport extends AppLayout {
    public static boolean checkboxAdded = false;

    Student selectedStudent = new Student();

    //Creates a VerticalLayout to store options later in the code, but allow previous code to access the variable
    static VerticalLayout options = new VerticalLayout();
    static DatePicker startDate = new DatePicker();

    //The Starting Date of the Report
    Date startingDate = new Date(true);

    //Checkbox Values for the Generation Step
    boolean events = false;
    boolean csaCategory = false;
    boolean charts = false;

    public GenerateIndividualReport() {
        addToNavbar(Home.makeHeader(Home.REPORT_TAB));
        settingsForm();
    }

    public void settingsForm() {
        //VerticalLayout to store form and buttons
        VerticalLayout mainLayout = new VerticalLayout();

        //Makes the form
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        //Gets Student Data values and adds to a dropdown list
        List<Student> studentNames = MySQLMethods.getStudents();
        Select<Student> studentSelect = new Select<>();
        studentSelect.setLabel("Student");
        studentSelect.setItems(studentNames);
        studentSelect.addValueChangeListener(e ->{
            selectedStudent = studentSelect.getValue();
            options.setEnabled(true);
            startDate.setEnabled(true);
        });

        //Creates Checkboxes
            Checkbox selectAll = new Checkbox("Select All");
                selectAll.setValue(true);
                selectAll.addValueChangeListener(e ->{
                    if (selectAll.getValue()){
                        //TODO set all buttons in the group true
                    }
                    else {
                        //TODO set all buttons in the group false
                    }
                });

            //Includes full list of events participated in
            Checkbox chooseEvents = new Checkbox("Get Events");
                chooseEvents.setValue(true);
                chooseEvents.addValueChangeListener(e -> events = chooseEvents.getValue());

        //Shows CSA Analysis, including progress, checkpoints, etc
        Checkbox chooseCSAVisibility = new Checkbox("See CSA Data");
        chooseCSAVisibility.setValue(true);
        chooseCSAVisibility.addValueChangeListener(e -> csaCategory = chooseCSAVisibility.getValue());

        //Includes Charts
        Checkbox chooseCharts = new Checkbox("Include Charts");
        chooseCharts.setValue(true);
        chooseCharts.addValueChangeListener(e -> charts = chooseCharts.getValue());
        if (!checkboxAdded) {
            options.add(selectAll, chooseEvents, chooseCSAVisibility, chooseCharts);
            checkboxAdded = true;
        }
        //The options can't be edited until a student is selected
        options.setEnabled(false);
        options.setPadding(false);
        options.setSpacing(false);
        options.setMargin(false);

        startDate = new DatePicker("Get Events Starting From:");
        startDate.setClearButtonVisible(true);
        startDate.addValueChangeListener(e -> {
            startingDate = new Date();
            startingDate.setDate(startDate.getValue());
        });
        startDate.setEnabled(false);
        startDate.setMax(LocalDate.now());

        //Adds components to form
        form.add(studentSelect, 3);
        form.add(options, 1);
        form.add(startDate, 2);

        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        actions.add(save, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.setAlignSelf(FlexComponent.Alignment.CENTER);

        save.addClickListener(e ->
                report(selectedStudent)
        );

        reset.addClickListener(e -> {
            studentSelect.setValue(null);
            startDate.setValue(null);
        });

        mainLayout.add(form, actions);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(mainLayout);
    }

    /**
     * Generates the report view after the form input in the beginning. It uses the student chosen in the form
     * and the date picked (if any) to create a report.
     *
     * @param student The student whose report is being generated
     */
    public void report(Student student) {
        StudentData dataOfStudent = student.getStudentData();

        //Sees if any date was selected. If not, it returns the full Data Analysis
        List<Event> eventsList;
        if (startingDate.fakeDate()) {
            eventsList = MySQLMethods.selectStudentEventsAsEvent(student);
        } else {
            eventsList = MySQLMethods.selectStudentEventsInRange(student, startingDate);
        }
        double hoursInRange = Event.getTotalHours(eventsList);
        double totalHours = MySQLMethods.round(dataOfStudent.getCommunityServiceHours());

        VerticalLayout main = new VerticalLayout();
        main.setPadding(true);
        main.setMargin(true);

        //Dropdown menu for all data
        Board dataBoard = new Board();

        H2 overviewHeading = new H2("Overview of " + student);
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

        H2 hourAnalysis = new H2("Progress Gauge");
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

        H2 eventDetails = new H2("Event Details");
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

        Button viewChart = new Button("Show Monthly Hour Graph");
        Row questionRow = dataBoard.addRow(viewChart);
        viewChart.addClickListener(onClick -> {
            Chart hourLineGraph = Charts.monthLineGraph("Hours per Month", eventsList);
            dataBoard.add(hourLineGraph);
            dataBoard.removeRow(questionRow);
        });

        main.add(dataBoard);

        setContent(main);
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

    public void nextSection(Board dataBoard) {
        //Adding two lines to signify the end of a section
        dataBoard.addRow(new Html("<hr>"));
        dataBoard.addRow(new Html("<hr>"));
    }

    /**
     * This method returns a the pre-formatted data of a student. This simplifies the job significantly when
     * writing multiple pieces of data
     *
     * @param header The heading text of the piece of data
     * @param text   The piece of data
     * @return A Div containing the formatted Student Data
     */
    private Div setText(String header, String text) {
        Div div = new Div();
        div.add(new H6(header + ":"));
        div.add(text);

        return div;
    }

}