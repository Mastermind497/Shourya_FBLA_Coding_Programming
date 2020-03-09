package com.Frontend.Get;

import com.Backend.*;
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
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route("get-student-events")
public class GetStudentEvents extends AppLayout {
    public static Student selected;

    public GetStudentEvents() throws Exception {
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
        ArrayList<Student> students = FileMethods.getStudentsAsList();
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
     * Views events and hours done by the chosen student
     *
     * @param chosen The chosen student
     */
    public void viewEvents(Student chosen) throws Exception {
        List<StudentData> studentData = new ArrayList<>();
        studentData.add(MySQLMethods.selectTrackerAsStudent(chosen));

        GridPro<StudentData> grid = new GridPro<>();
        grid.setItems(studentData);
        grid.addColumn(StudentData::getFirstName).setHeader("First Name");
        grid.addColumn(StudentData::getLastName).setHeader("Last Name");
        grid.addColumn(StudentData::getStudentID).setHeader("Student ID");
        grid.addColumn(StudentData::getGrade).setHeader("Grade");
        grid.addColumn(StudentData::getCommunityServiceHours).setHeader("CS Hours");
        grid.addColumn(StudentData::getCommunityServiceCategory).setHeader("CS Category");
        grid.addColumn(StudentData::getEmail).setHeader("Email");
        grid.addColumn(StudentData::getYearsDone).setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited).setHeader("Last Edited");

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
        System.out.println(eventList);

        Grid<Event> events = new Grid<>();
        events.setItems(eventList);
        events.addColumn(Event::getEventName).setHeader("Event Name");
        events.addColumn(Event::getHours).setHeader("Hours of Event");
        events.addColumn(Event::getDate).setHeader("Date of Event");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<Event> al : events.getColumns()) {
            al.setAutoWidth(true);
        }

        events.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        layout.add(grid, events);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        setContent(layout);
    }
}
