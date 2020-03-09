package com.Frontend.Reports;

import com.Backend.Date;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.Home;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

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


    public void report(Student student, Date beginDate) {
        //Dropdown menu for all data
        Accordion dataViewer = new Accordion();

        StudentData dataOfStudent = student.getStudentData();

        VerticalLayout basicInformation = new VerticalLayout();
            basicInformation.add(

            );
    }

}
