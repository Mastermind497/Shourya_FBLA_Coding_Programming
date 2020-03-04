package com.Frontend.Edit;

import com.Backend.Event;
import com.Backend.FileMethods;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Frontend.Add.CreateStudent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditStudentEvents extends AppLayout {
    public static Student selected = null;

    public VerticalLayout chooseStudent() {
        //Grouping in a Vertical Column
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to Edit
        //Make Labels for Different Input Fields
        ArrayList<Student> students = new ArrayList<>();
        try {
            students = new ArrayList<>(Arrays.asList(FileMethods.getStudents()));
        }
        catch (IOException ioe) {
            Notification.show(ioe.getMessage());
            ioe.printStackTrace();
        }
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
                onButtonClick(selected);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        return selector;
    }

    public VerticalLayout onButtonClick(Student selectedStudent) throws Exception {
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to Edit
        //Make Labels for Different Input Fields
        ArrayList<Event> events = new ArrayList<>();
        try {
            events = MySQLMethods.selectStudentEventsAsEvent(selectedStudent);
        }
        catch (IOException ioe) {
            Notification.show(ioe.getMessage());
            ioe.printStackTrace();
        }
        //Adds Create New Student Option
        ComboBox<Event> eventChoices = new ComboBox<>();
        eventChoices.setItems(events);
        eventChoices.addValueChangeListener(e -> {
            if (eventChoices.getValue().getCreateNewStudent()) {
                UI.getCurrent().navigate(CreateStudent.class);
            }
            selected = eventChoices.getValue();
        });
        eventChoices.setRequiredIndicatorVisible(true);

        Button edit = new Button("Edit This Student");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        selector.add(eventChoices, edit);
        selector.setAlignItems(FlexComponent.Alignment.CENTER);
        selector.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(selector);

        //If Button is Clicked
        edit.addClickListener(event -> {
            try {
                onButtonClick(selected);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });



    }

    public VerticalLayout eventEditor(Event selectedEvent) {

    }
}
