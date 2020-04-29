package com.frontend.GetStudentInformation;

import com.backend.Event;
import com.backend.MySQLMethods;
import com.backend.Student;
import com.backend.StudentData;
import com.frontend.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Gets All Student's General Information and Allows Editing. Also Allows viewing more specific events.
 */
@Route(value = "get-student-info", layout = MainView.class)
@PageTitle("View and Edit Information | FBLA Genie")
public class GetStudentInformation extends VerticalLayout {

    /** The Main Grid of Student Data */
    GridPro<StudentData> grid;

    /**
     * The Constructor, just runs the Main Table Method
     */
    public GetStudentInformation() {
        mainTable();
    }

    /**
     * Creates the main table, which shows student data and allows editing of it
     */
    public void mainTable() {
        removeAll();
        H1 header = new H1("View And Edit Student Information");
        add(header);
        //Shows data on a grid (Up to 100k pieces)
        List<StudentData> data = MySQLMethods.getStudentData();

        //Creates a Grid with Inline editing and Sorting
        grid = setUpStudentGrid(data);
        grid.addComponentColumn(this::expandButton).setHeader("Expand");
        grid.addComponentColumn(this::deleteButton).setHeader("Delete");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.setMultiSort(true);

        add(grid);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setAlignSelf(FlexComponent.Alignment.CENTER);
    }

    /**
     * Creates the delete button, which deletes a student if clicked
     *
     * @param student The Student to create a delete button for
     * @return The Delete Button
     */
    public Button deleteButton(Student student) {
        ConfirmDialog dialog = new ConfirmDialog("Confirm Delete",
                String.format("Are you sure you want to delete %s? This action cannot be undone", student.getFirstName() + " " + student.getLastName()),
                "Delete", onDelete -> {
            student.delete();
            grid.setItems(MySQLMethods.getStudentData());
        },
                "Cancel", this::onClose);
        dialog.setConfirmButtonTheme("error primary");
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.TRASH.create(), buttonClickEvent ->
                dialog.open()
        );
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }

    /**
     * Closes the Dialog Box and Resets the table with the updated values of the Student
     *
     * @param cancelEvent The Event that the dialog box was cancelled
     */
    private void onClose(ConfirmDialog.CancelEvent cancelEvent) {
        cancelEvent.getSource().close();
        grid.setItems(MySQLMethods.getStudentData());
    }

    /**
     * Creates the expand button, which reveals more specific student information, namely the events
     * <p>
     * This method creates a dialog box (as a notification) which has all of a student's events and data, allowing
     * for a more in-depth review of a student
     *
     * @param student The Student to Expand
     * @return The Expand Button
     */
    public Button expandButton(StudentData student) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Expand", buttonClickEvent -> {
            System.out.println("Notification");
            Notification fullData = new Notification();
            Button close = new Button("Close");
            VerticalLayout layout = new VerticalLayout(close);
            List<Component> arr = viewEvents(student.getStudent());
            Grid<StudentData> studentGrid = (Grid<StudentData>) arr.get(2);
            Grid<Event> events = (Grid<Event>) arr.get(0);
            events.addComponentColumn(this::deleteButton).setHeader("Delete");
            events.setMultiSort(true);
            events.setMaxHeight("20em");
            VerticalLayout studentInfo = (VerticalLayout) arr.get(1);
            studentInfo.add(studentGrid, events);
            studentInfo.setMaxHeight("25em");

            //spacer for close button
            layout.add(new H6(" "));
            layout.add(studentInfo);
            layout.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.setWidth("73em");
            layout.setHeight("30em");
            fullData.add(layout);
            fullData.setPosition(Notification.Position.MIDDLE);
            fullData.open();
            close.addClickListener(onClick -> {
                fullData.close();
                grid.setItems(MySQLMethods.getStudentData());
            });
            System.out.println("End Notification");
        });
        button.addThemeVariants();
        button.setIcon(VaadinIcon.EXPAND_FULL.create());
        return button;
    }

    /**
     * Views events and hours done by the chosen student.
     * <p>
     * This method is used by the expandButton to add all of the events to the dialog box
     *
     * @param chosen The chosen student
     */
    private List<Component> viewEvents(Student chosen) {
        List<StudentData> studentData = new ArrayList<>();
        studentData.add(chosen.getStudentData());

        //Creates a Grid with Inline editing and Sorting
        GridPro<StudentData> grid = setUpStudentGrid(studentData);

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

        //Creates a Grid for all of the Events a student has
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

    /**
     * Sets up the student grid for easy viewing and manipulation
     *
     * @param studentData A List of all the students to be added to the grid
     * @return The Grid Containing all of the student information
     */
    private GridPro<StudentData> setUpStudentGrid(List<StudentData> studentData) {
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

        return grid;
    }
}