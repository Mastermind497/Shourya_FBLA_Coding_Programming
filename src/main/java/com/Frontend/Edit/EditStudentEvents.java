package com.Frontend.Edit;

import com.Backend.Event;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Frontend.Add.CreateStudent;
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
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route("edit-student-events")
public class EditStudentEvents extends AppLayout {
    public static Student selected = null;
    public static Event oldEvent = null;

    public EditStudentEvents() {
//        addToNavbar(Home.makeHeader());

        if (selected == null) {
            chooseStudent();
        }
        else if (oldEvent == null) {
            eventSelector(selected);
        }
        else {
            onButtonClick(oldEvent);
        }
    }

    public VerticalLayout chooseStudent() {
        //Grouping in a Vertical Column
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to Edit
        //Make Labels for Different Input Fields
        List<Student> students = MySQLMethods.getStudents();

        //Adds Create New Student Option
        students.add(new Student(true));
        ComboBox<Student> studentChoices = new ComboBox<>();
        studentChoices.setItems(students);
        studentChoices.addValueChangeListener(e -> {
            if (studentChoices.getValue().getCreateNewStudent()) {
                UI.getCurrent().navigate(CreateStudent.class);
            }
            selected = studentChoices.getValue();
        });
        studentChoices.setRequiredIndicatorVisible(true);

        Button edit = new Button("Edit This Student");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        selector.add(studentChoices, edit);
        selector.setAlignItems(FlexComponent.Alignment.CENTER);
        selector.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(selector);

        //If Button is Clicked
        edit.addClickListener(event -> {
            try {
                eventSelector(selected);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        return selector;
    }

    public VerticalLayout eventSelector(Student selectedStudent) {
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to Edit
        //Make Labels for Different Input Fields
        List<Event> events = new ArrayList<>();
        try {
            events = MySQLMethods.selectStudentEventsAsEvent(selectedStudent);
        }
        catch (Exception e) {
            Notification.show(e.getMessage());
            e.printStackTrace();
        }
        //Adds Create New Student Option
        ComboBox<Event> eventChoices = new ComboBox<>();
        eventChoices.setItems(events);
        eventChoices.addValueChangeListener(e ->
            oldEvent = eventChoices.getValue()
        );
        eventChoices.setRequiredIndicatorVisible(true);

        Button edit = new Button("Edit This Event");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        selector.add(eventChoices, edit);
        selector.setAlignItems(FlexComponent.Alignment.CENTER);
        selector.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(selector);

        //If Button is Clicked
        edit.addClickListener(Event -> {
            try {
                onButtonClick(oldEvent);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        return selector;
    }

    public VerticalLayout onButtonClick(Event selectedEvent) {
        //The new Event Data
        Event newEvent = new Event();

        //Creates a Horizontal Layout to decrease maximum width
        HorizontalLayout full = new HorizontalLayout();

        //A Structure that changes size and shape depending on screen
        FormLayout editEventForm = new FormLayout();

        //A Data Type to store all information on a student
        Binder<Event> binder = new Binder<>();

        //Configures the Form
        editEventForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("20em", 1),
                new FormLayout.ResponsiveStep("30em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        //Makes Labels for Different Input Fields
        TextField eventName = new TextField("Event Name");
        eventName.setValue(oldEvent.getEventName());
        eventName.setValueChangeMode(ValueChangeMode.EAGER);

        NumberField eventHours = new NumberField("Length of Event");
        eventHours.setValue(oldEvent.getHours());
        eventHours.setErrorMessage("That is not a Number, Please Enter a Number");
        eventHours.setValueChangeMode(ValueChangeMode.EAGER);

        DatePicker dateOfEvent = new DatePicker("Date");
        dateOfEvent.setClearButtonVisible(true);
        dateOfEvent.setMax(LocalDate.now());
        //Sets the displayed date to the previous event date
        dateOfEvent.setValue(LocalDate.of(oldEvent.getYear(), oldEvent.getMonth(), oldEvent.getDay()));

        //Makes all components required
        eventName.setRequiredIndicatorVisible(true);
        eventHours.setRequiredIndicatorVisible(true);
        dateOfEvent.setRequiredIndicatorVisible(true);

        //Adds Components with their desired widths
        editEventForm.add(eventName, 2);
        editEventForm.add(eventHours, 1);
        editEventForm.add(dateOfEvent, 1);

        full.add(editEventForm);
        full.setSpacing(true);
        full.setMargin(true);
        full.setAlignItems(FlexComponent.Alignment.CENTER);

        //Button Bar
        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Update");
        Button reset = new Button("Reset");
        actions.add(save, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.setAlignSelf(FlexComponent.Alignment.CENTER);

        binder.forField(eventName)
                .withValidator(new StringLengthValidator(
                        "Please enter the first name", 1, null))
                .bind(Event::getEventName, Event::setEventName);

        binder.forField(eventHours)
                .withValidator(new DoubleRangeValidator(
                        "Please enter a valid number", 0.0, null))
                .bind(Event::getHours, Event::setHours);

        binder.forField(dateOfEvent)
                .withValidator(new DateRangeValidator(
                        "Please enter a valid Date", LocalDate.MIN, LocalDate.now()))
                .bind(Event::getLocalDate, Event::setDate);

        //add listeners for the buttons
        save.addClickListener(e -> {
            Notification.show("Your data is being processed");
            if (binder.writeBeanIfValid(newEvent)) {
                try {
                    MySQLMethods.updateEvent(selected, oldEvent, newEvent);
                } catch (Exception ex) {
                    Notification.show("Error Occurred, Please Try Again \n" + ex.getMessage());
                    ex.printStackTrace();
                }

                binder.readBean(null);
                Notification.show("Your data has been processed!");
                selected = null;
                oldEvent = null;
                setContent(chooseStudent());
            } else {
                Notification.show("Your Data couldn't be added");
            }
        });
        //ENTER key also activates save
        save.addClickShortcut(Key.ENTER);

        reset.addClickListener(e -> {
            // clear fields by setting null
            binder.readBean(null);
            selected = null;
            setContent(chooseStudent());
        });

        VerticalLayout container = new VerticalLayout(full, actions);
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        setContent(container);

        return container;
    }
}
