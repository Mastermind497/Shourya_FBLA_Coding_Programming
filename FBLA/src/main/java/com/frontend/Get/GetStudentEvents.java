package com.frontend.Get;

import com.backend.Event;
import com.backend.FileMethods;
import com.backend.MySQLMethods;
import com.backend.Student;
import com.frontend.Add.CreateStudent;
import com.frontend.Home;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

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
        H1 studentName = new H1(chosen.getFirstName() + " " + chosen.getLastName() + ", " +
                chosen.getStudentID());

        VerticalLayout layout = new VerticalLayout();

        ArrayList<Event> eventList = MySQLMethods.selectStudentEventsAsEvent(chosen);


        Grid<Event> grid = new Grid<>();
        grid.setItems(eventList);
        grid.addColumn(Event::getEventName).setHeader("Event Name");
        grid.addColumn(Event::getHours).setHeader("Hours of Event");
        grid.addColumn(Event::getDate).setHeader("Date of Event");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<Event> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        layout.add(studentName, grid);
        setContent(layout);
    }
}
