package com.frontend.Add.AddHours;

import com.backend.Event;
import com.backend.MySQLMethods;
import com.backend.Student;
import com.frontend.Add.CreateStudent.CreateStudent;
import com.frontend.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDate;
import java.util.List;

/**
 * The Class for Adding Hours into a Student
 */
@Route(value = "add-hours", layout = MainView.class)
//Uses the Layout from MainView.java without having to recreate the entire thing
@PageTitle("Add Student Hours | FBLA Genie")
@PreserveOnRefresh
@UIScope
public class AddHours extends VerticalLayout {

    /**
     * The Add Hours View
     */
    public AddHours() {
        removeAll();
        H1 heading = new H1("Add Student Hours");
        add(heading);
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


        /* Makes Labels for Different Input Fields */
        //Gets all Students as Options for Adding Hours
        List<Student> students = MySQLMethods.getStudents();
        //Adds Create New Student Option
        students.add(new Student(true));
        ComboBox<Student> studentChoices = new ComboBox<>("Student Name");
        studentChoices.setItems(students);
        studentChoices.addValueChangeListener(e -> {
            //Checks if the person selected the "Create New Student" Option
            if (studentChoices.getValue() != null && studentChoices.getValue().getCreateNewStudent()) {
                UI.getCurrent().navigate(CreateStudent.class);
            }
        });

        //A Text Input field for the Name of the Event
        TextField eventName = new TextField("Event Name");
        eventName.setPlaceholder("Volunteering at Central Park"); //Example Name
        eventName.setValueChangeMode(ValueChangeMode.EAGER);

        //A Number input field for the length of the event
        NumberField eventHours = new NumberField("Length of Event (Hours)");
        eventHours.setHasControls(true);
        eventHours.setStep(0.5d);
        eventHours.setPlaceholder("2.5");
        eventHours.setErrorMessage("That is not a Number, Please Enter a Number");
        eventHours.setValueChangeMode(ValueChangeMode.EAGER);

        //A Date Picker to choose the exact date of the event
        DatePicker dateOfEvent = new DatePicker("Date");
        dateOfEvent.setClearButtonVisible(true);
        dateOfEvent.addValueChangeListener(e -> {
            event.setDate(dateOfEvent.getValue());
            System.out.println(dateOfEvent.getValue());
        });
        dateOfEvent.setMax(LocalDate.now());

        //Makes all components required
        studentChoices.setRequired(true);
        studentChoices.setRequiredIndicatorVisible(true);
        eventName.setRequired(true);
        eventName.setRequiredIndicatorVisible(true);
        eventHours.setRequiredIndicatorVisible(true);
        dateOfEvent.setRequired(true);
        dateOfEvent.setRequiredIndicatorVisible(true);

        //Adds Components with their desired widths
        addEventHours.add(studentChoices, 3);
        addEventHours.add(eventName, 2);
        addEventHours.add(eventHours, 1);
        addEventHours.add(dateOfEvent, 1);

        //Adds the form to the layout
        full.add(addEventHours);
        full.setSpacing(true);
        full.setMargin(true);
        full.setAlignItems(Alignment.CENTER);

        //Button Bar
        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        actions.add(save, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(Alignment.CENTER);
        actions.setAlignSelf(Alignment.CENTER);

        /* Bind Each Selection to the corresponding backend methods to speed up changing */
        binder.forField(studentChoices).bind(Event::getStudent, Event::setStudent);

        binder.forField(eventName)
                .withValidator(new StringLengthValidator(
                        "Please enter a Valid Event Name", 1, null))
                .bind(Event::getEventName, Event::setEventName);

        binder.forField(eventHours)
                .withValidator(new DoubleRangeValidator(
                        "Please Enter a Valid Length", 0.1, null))
                .bind(Event::getHours, Event::setHours);

        //add listeners for the buttons
        save.addClickListener(e -> {
            //Creates the event if it is a valid selection
            Notification.show("Your data is being processed...");
            if (binder.writeBeanIfValid(event)) {
                event.addEvent();
                studentChoices.setValue(null);
                Notification success = new Notification();
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                Label succeeded = new Label(String.format("The Event Was Successfully Added to %s!", studentChoices.getValue()));
                success.add(succeeded);
                success.setDuration(6000);
                success.open();
            } else {
                Notification invalid = new Notification();
                invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Label failed = new Label("There Was An Error. Please Try Again.");
                invalid.add(failed);
                invalid.setDuration(6000);
                invalid.open();
            }
        });
        //ENTER key also clicks save
        save.addClickShortcut(Key.ENTER);

        //Clears all fields after "RESET" is clicked
        reset.addClickListener(e -> {
            // clear fields by setting null
            binder.readBean(null);
        });

        add(full, actions);
        setAlignItems(Alignment.CENTER);
    }
}