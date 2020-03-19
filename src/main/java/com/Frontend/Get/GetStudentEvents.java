package com.Frontend.Get;

import com.Backend.Event;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.Add.CreateStudent;
import com.Frontend.Home;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetStudentEvents extends AppLayout {
    public static Student selected;

    /**
     * This used to be the original method for editing data before inline editing. After
     * that was discovered, this method has been deprecated and not used anymore. it is just here
     * to show progress made.
     *
     * @deprecated
     */
    @Deprecated
    public GetStudentEvents() {
        addToNavbar(Home.makeHeader());

        //allows redirection from View Student Grid
        if (selected == null) {
            selectStudent();
        }
        if (selected != null) {
            viewEvents(selected);
        }
    }

    public VerticalLayout selectStudent() {
        //Grouping in a Vertical Column
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to view Events
        //Make Labels for Different Input Fields
        ArrayList<Student> students = MySQLMethods.getStudents();
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

        Button choose = new Button("View This Student's Event History");
        choose.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        selector.add(studentChoices, choose);
        selector.setAlignItems(FlexComponent.Alignment.CENTER);
        selector.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(selector);

        //If Button is Clicked
        choose.addClickListener(event -> {
            try {
                viewEvents(selected);
            } catch (Exception e) {
                Notification.show(e.getMessage());
                e.printStackTrace();
            }
        });

        return selector;
    }

    /**
     * Views events and hours done by the chosen student. Now, this method is linked to the
     *
     * @param chosen The chosen student
     * @link GetStudentInformation.java class and can be accessed by single-clicking any student.
     */
    public static VerticalLayout viewEvents(Student chosen) {
        List<StudentData> studentData = new ArrayList<>();
        studentData.add(MySQLMethods.selectTrackerAsStudent(chosen));

        GridPro<StudentData> grid = new GridPro<>();
        grid.setItems(studentData);
        grid.addEditColumn(StudentData::getFirstName, "First Name", "String")
                .text(StudentData::setFirstName)
                .setHeader("First Name");
        grid.addEditColumn(StudentData::getLastName, "Last Name", "String")
                .text(StudentData::setLastName)
                .setHeader("Last Name");
        grid.addEditColumn(StudentData::getStudentID, "Student ID", "Integer")
                .text(StudentData::setStudentID)
                .setHeader("Student ID");
        grid.addEditColumn(StudentData::getGrade, "Grade", "Short")
                .text(StudentData::setGrade)
                .setHeader("Grade");
        grid.addEditColumn(StudentData::getCommunityServiceHours, "Hours", "Double")
                .text(StudentData::setCommunityServiceHours)
                .setHeader("CS Hours");
        grid.addEditColumn(StudentData::getCommunityServiceCategory, "Category", "String")
                .text(StudentData::setCommunityServiceCategory)
                .setHeader("CS Category");
        grid.addEditColumn(StudentData::getEmail, "Email", "String")
                .text(StudentData::setEmail)
                .setHeader("Email");
        grid.addEditColumn(StudentData::getYearsDone, "Years Done", "Short")
                .text(StudentData::setYearsDone)
                .setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited, "Last Edited", "Date")
                .setHeader("Last Edited");

        grid.setHeightByRows(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        VerticalLayout layout = new VerticalLayout();

        ArrayList<Event> eventList = MySQLMethods.selectStudentEventsAsEvent(chosen);
        Collections.sort(eventList);

        GridPro<Event> events = new GridPro<>();
        events.setItems(eventList);
        events.addEditColumn(Event::getEventName, "Name", "String")
                .text(Event::setEventName)
                .setHeader("Event Name");
        events.addEditColumn(Event::getHours, "Hours", "Double")
                .text(Event::setHours)
                .setHeader("Hours of Event");
        events.addEditColumn(Event::getDate)
                .text(Event::setDate)
                .setHeader("Date of Event");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<Event> al : events.getColumns()) {
            al.setAutoWidth(true);
        }

        events.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        layout.add(grid, events);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        return layout;
    }
}
