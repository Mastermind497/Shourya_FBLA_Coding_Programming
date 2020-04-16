package com.Frontend.Get.GetStudentEvents;

import com.Backend.Event;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Route(layout = MainView.class)
public class GetStudentEvents extends VerticalLayout {
    public static Student selected;

    /**
     * Views events and hours done by the chosen student. Now, this method is linked to the
     *
     * @param chosen The chosen student
     * @link GetStudentInformation.java class and can be accessed by single-clicking any student.
     */
    public static List<Component> viewEvents(Student chosen) {
        List<StudentData> studentData = new ArrayList<>();
        studentData.add(chosen.getStudentData());

        //Creates a Grid with Inline editing and Sorting
        GridPro<StudentData> grid = new GridPro<>();
        grid.setItems(studentData);
        grid.addEditColumn(StudentData::getFirstName, "name")
                .text(StudentData::setFirstName)
                .setHeader("First Name");
        grid.addEditColumn(StudentData::getLastName, "name")
                .text(StudentData::setLastName)
                .setHeader("Last Name");
        grid.addEditColumn(StudentData::getStudentID, "idNumber")
                .text(StudentData::setStudentID)
                .setHeader("Student ID");
        grid.addEditColumn(StudentData::getGrade, "grade", "integer")
                .text(StudentData::setGrade)
                .setHeader("Grade");
        grid.addEditColumn(StudentData::getCommunityServiceHours, "hours", "double")
                .text(StudentData::setCommunityServiceHoursFromSelect)
                .setHeader("CS Hours");
        List<String> categoryOptions = new ArrayList<>(Arrays.asList("CSA Community (50 Hours)", "CSA Service (200 Hours)", "CSA Achievement (500 Hours)"));
        grid.addEditColumn(StudentData::getCommunityServiceCategory, "category")
                .select(StudentData::setCommunityServiceCategory, categoryOptions)
                .setHeader("CS Category");
        grid.addEditColumn(StudentData::getEmail)
                .text(StudentData::setEmail)
                .setHeader("Email");
        grid.addEditColumn(StudentData::getYearsDone, "years", "integer")
                .text(StudentData::setYearsDone)
                .setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited, "date", "lastedited").setHeader("Last Edited");

        grid.setHeightByRows(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        VerticalLayout layout = new VerticalLayout();

        List<Event> eventList = MySQLMethods.selectStudentEventsAsEvent(chosen);
        Collections.sort(eventList);

        GridPro<Event> events = new GridPro<>();
        events.setItems(eventList);
        events.addEditColumn(Event::getEventName, "Name", "String")
                .text(Event::setEventName)
                .setHeader("Event Name");
        events.addEditColumn(Event::getHours, "Hours", "Double")
                .text((event, hours) -> {
                    event.setHours(hours);
                    grid.setItems(chosen.getStudentData());
                })
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

        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        List<Component> newArr = new ArrayList<>();
        newArr.add(events);
        newArr.add(layout);
        newArr.add(grid);

        return newArr;
    }
}
