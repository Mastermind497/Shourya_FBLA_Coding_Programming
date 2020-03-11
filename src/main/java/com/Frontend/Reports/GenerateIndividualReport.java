package com.Frontend.Reports;

import com.Backend.Date;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.Charts;
import com.Frontend.Home;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;

@Route("individual-reports")
public class GenerateIndividualReport extends AppLayout {
    Student selectedStudent = new Student();

    //Creates a VerticalLayout to store options later in the code, but allow previous code to access the variable
    static VerticalLayout options = new VerticalLayout();
    static DatePicker startDate = new DatePicker();

    //The Starting Date of the Report
    Date startingDate = new Date();

    //Checkbox Values for the Generation Step
    boolean events = false;
    boolean csaCategory = false;
    boolean charts = false;

    public GenerateIndividualReport() {
        addToNavbar(Home.makeHeader());
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
        ArrayList<Student> studentNames = MySQLMethods.getStudents();
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
                chooseCSAVisibility.addValueChangeListener(e-> csaCategory = chooseCSAVisibility.getValue());

            //Includes Charts
            Checkbox chooseCharts = new Checkbox("Include Charts");
                chooseCharts.setValue(true);
                chooseCharts.addValueChangeListener(e -> charts = chooseCharts.getValue());

        options.add(selectAll, chooseEvents, chooseCSAVisibility,  chooseCharts);
        //The options can't be edited until a student is selected
        options.setEnabled(false);
        options.setPadding(false);
        options.setSpacing(false);
        options.setMargin(false);

        startDate = new DatePicker("Get Events Starting From:");
        startDate.setClearButtonVisible(true);
        startDate.addValueChangeListener(e -> startingDate.setDate(startDate.getValue()));
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
            report(selectedStudent, startingDate)
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
     * @param student The student whose report is being generated
     * @param beginDate The beginning date of the data generation
     */
    public void report(Student student, @OptionalParameter Date beginDate) {
        VerticalLayout main = new VerticalLayout();
        main.setPadding(true);
        main.setMargin(true);
        
        //Dropdown menu for all data
        Board dataBoard = new Board();

        StudentData dataOfStudent = student.getStudentData();

        H2 overviewHeading = new H2("Overview");
            Div firstName = setText("First Name", dataOfStudent.getFirstName());
            Div lastName = setText("Last Name" , dataOfStudent.getLastName());
            Div studentID = setText("Student ID", Integer.toString(dataOfStudent.getStudentID()));
            Div email = setText("Email", dataOfStudent.getEmail());
            Div grade = setText("Grade", Short.toString(dataOfStudent.getGrade()));

            Div communityServiceCategoryGoal = setText("Community Service Category Goal",
                    dataOfStudent.getCommunityServiceCategory());
            Div communityServiceCategoryCurrent = setText("Current Community Service Category",
                    dataOfStudent.getCurrentCommunityServiceCategory());
            Div communityServiceHours = setText("Community Service Hours",
                    Double.toString(dataOfStudent.getCommunityServiceHours()));

            Div lastEdited = setText("Last Edited", dataOfStudent.getLastEdited().toString());

            //Heading to describe section
            dataBoard.addRow(overviewHeading);

            //First Row: Basic Overview
            dataBoard.addRow(firstName, lastName, studentID, grade);

            //Second Row: Supporting Information
            Row supportingInfo = dataBoard.addRow(email, communityServiceHours);

            //Add a space between the second and third rows
            Div spacer = new Div();
            spacer.add(new H3(" "));
            dataBoard.addRow(spacer);

            //Third Row: Gives Brief Overview of hours
            dataBoard.addRow(communityServiceCategoryCurrent, communityServiceCategoryGoal, lastEdited);

        H2 hourAnalysis = new H2("Hours in Depth");
            //Creates a Solid Gauge for each of the Different Awards
            Chart communityChart = Charts.solidGauge(0, /*dataOfStudent.getCommunityServiceHours()*/ 50, 50, "Hours");

        //Row 1: Heading for Hour Analysis
        dataBoard.addRow(hourAnalysis);
        //Row 2: Charts depicting process through hours
        dataBoard.addRow(communityChart);

        main.add(dataBoard);

        setContent(main);
    }

    /**
     * This method returns a the pre-formatted data of a student. This simplifies the job significantly when
     * writing multiple pieces of data
     * @param header The heading text of the piece of data
     * @param text The piece of data
     * @return A Div containing the formatted Student Data
     */
    private Div setText(String header, String text) {
        Div div = new Div();
        div.add(new H6(header + ":"));
        div.add(text);

        return div;
    }

}