package com.Frontend.Reports.GenerateReport;

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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route(value = "report", layout = MainView.class)
public class GenerateReport extends VerticalLayout {
    final Button individualReport = new Button("Generate Individual Report", buttonClickEvent -> generateIndividualReport());
    final Button groupReport = new Button("Generate Group Report", buttonClickEvent -> generateGroupReport());

    public GenerateReport() {
        removeAll();

        HorizontalLayout form = new HorizontalLayout();

        groupReport.setText("Generate Group Report");
        groupReport.addThemeVariants(ButtonVariant.LUMO_LARGE);
        individualReport.setText("Generate Individual Report");
        individualReport.addThemeVariants(ButtonVariant.LUMO_LARGE);

        form.add(groupReport, individualReport);

        form.setAlignItems(FlexComponent.Alignment.CENTER);
        form.setAlignSelf(FlexComponent.Alignment.CENTER);

        setAlignItems(Alignment.CENTER);
        add(form);
    }

    public void generateGroupReport() {
        removeAll();
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        individualReport.setText("Generate Individual Report Instead");

    }

    public void generateIndividualReport() {
        removeAll();
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));

        groupReport.setText("Generate Group Report Instead");

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
        double totalHours = MySQLMethods.round(dataOfStudent.getCommunityServiceHours());

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

    public void groupReport() {

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

    public static void nextSection(Board dataBoard) {
        //Adding two lines to signify the end of a section
        dataBoard.addRow(new Html("<hr>"));
        dataBoard.addRow(new Html("<hr>"));
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
}
