package com.Frontend.Add;

import com.Backend.Date;
import com.Backend.Event;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Frontend.Home;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;

@Route("add-hours")
@PageTitle("Add Student Hours | FBLA Genie")
public class AddHours extends AppLayout {
    static Date eventDate = new Date();

    public AddHours() {
        //Adds Navigation
        addToNavbar(Home.makeHeader(Home.ADD_HOURS_TAB));

        //The main form to fill out data
        //Creates a Horizontal Layout to decrease maximum width
        HorizontalLayout full = new HorizontalLayout();

        //A Structure that changes size and shape depending on screen
        FormLayout addEventHours = new FormLayout();

        //A Data Type to store all information on a event
        Binder<Event> binder = new Binder<>();
        Event event = new Event();

        //Configures the Form
        addEventHours.setResponsiveSteps(
                new FormLayout.ResponsiveStep("20em", 1),
                new FormLayout.ResponsiveStep("30em", 2),
                new FormLayout.ResponsiveStep("40em", 3));


        //Makes Labels for Different Input Fields
        ArrayList<Student> students = MySQLMethods.getStudents();
        //Adds Create New Student Option
        students.add(new Student(true));
        ComboBox<Student> studentChoices = new ComboBox<>("Student Name");
        studentChoices.setItems(students);
        studentChoices.addValueChangeListener(e -> {
            if (studentChoices.getValue() != null && studentChoices.getValue().getCreateNewStudent()) {
                UI.getCurrent().navigate(CreateStudent.class);
            }
        });

        TextField eventName = new TextField("Event Name");
        eventName.setPlaceholder("Volunteering at Central Park");
        eventName.setValueChangeMode(ValueChangeMode.EAGER);

        NumberField eventHours = new NumberField("Length of Event (Hours)");
        eventHours.setPlaceholder("2.5");
        eventHours.setErrorMessage("That is not a Number, Please Enter a Number");
        eventHours.setValueChangeMode(ValueChangeMode.EAGER);

        DatePicker dateOfEvent = new DatePicker("Date");
        dateOfEvent.setClearButtonVisible(true);
        dateOfEvent.addValueChangeListener(e ->
            eventDate.setDate(dateOfEvent.getValue())
        );
        dateOfEvent.setMax(LocalDate.now());

        //Makes all components required
        studentChoices.setRequiredIndicatorVisible(true);
        eventName.setRequiredIndicatorVisible(true);
        eventHours.setRequiredIndicatorVisible(true);

        //Adds Components with their desired widths
        addEventHours.add(studentChoices, 3);
        addEventHours.add(eventName, 2);
        addEventHours.add(eventHours, 1);
        addEventHours.add(dateOfEvent, 1);

        full.add(addEventHours);
        full.setSpacing(true);
        full.setMargin(true);
        full.setAlignItems(FlexComponent.Alignment.CENTER);

        //Button Bar
        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        actions.add(save, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.setAlignSelf(FlexComponent.Alignment.CENTER);

        binder.forField(studentChoices).bind(Event::getStudent, Event::setStudent);

        binder.forField(eventName)
                .withValidator(new StringLengthValidator(
                        "Please enter a Valid Event Name", 1, null))
                .bind(Event::getEventName, Event::setEventName);

        binder.forField(eventHours)
                .withValidator(new DoubleRangeValidator(
                        "Please Enter a Valid Integer", 0.0, null))
                .bind(Event::getHours, Event::setHours);

        //add listeners for the buttons
        save.addClickListener(e -> {
            if (binder.writeBeanIfValid(event)) {
                event.setDate(eventDate);
                event.addEvent();
                Notification.show("Your data is being processed");
                binder.readBean(null);
                Notification.show("Your data has been processed!");
            } else Notification.show("There was an error. Please Try Again");
        });
        //ENTER key also clicks save
        save.addClickShortcut(Key.ENTER);

        reset.addClickListener(e -> {
            // clear fields by setting null
            binder.readBean(null);
        });

        VerticalLayout container = new VerticalLayout(full, actions);
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        setContent(container);
    }
}