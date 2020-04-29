package com.Frontend.Get.GetStudentInformation;

import com.Backend.Event;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.Get.GetStudentEvents.GetStudentEvents;
import com.Frontend.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO Make this class fully-functional
@Route(value = "get-student-info", layout = MainView.class)
@PageTitle("View and Edit Information | FBLA Genie")
public class GetStudentInformation extends VerticalLayout {

    Button close = new Button("Close", buttonClickEvent -> UI.getCurrent().getPage().reload());
    GridPro<StudentData> grid;

    public GetStudentInformation() {
        mainTable();
    }

    public void mainTable() {
        removeAll();
        //Shows data on a grid (Up to 100k pieces)
        List<StudentData> data = MySQLMethods.getStudentData();

        //Creates a Grid with Inline editing and Sorting
        grid = new GridPro<>();
        grid.setItems(data);
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
        grid.addComponentColumn(this::expandButton).setHeader("Expand");
        grid.addComponentColumn(this::deleteButton).setHeader("Delete");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.setMultiSort(true);

        //Layouts to help in orienting
        HorizontalLayout choice = new HorizontalLayout();

        Button exportData = new Button("Export Data");
        exportData.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //Button is disabled until exporting feature is added
        exportData.setEnabled(false);

        choice.add(exportData);
        choice.setAlignItems(FlexComponent.Alignment.CENTER);
        choice.setAlignSelf(FlexComponent.Alignment.CENTER);

        add(grid, choice);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setAlignSelf(FlexComponent.Alignment.CENTER);
    }

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

    public Button deleteButton(Event event, Grid<Event> grid, Notification notification, Grid<StudentData> studentGrid) {
        ConfirmDialog dialog = new ConfirmDialog("Confirm Delete",
                String.format("Are you sure you want to delete %s, which happened on %s? This action cannot be undone",
                        event.getEventName(), event.getDate().toString()),
                "Delete", onDelete -> {
            event.delete();
            grid.setItems(MySQLMethods.selectStudentEventsAsEvent(event));
            studentGrid.setItems(MySQLMethods.selectTrackerAsStudent(event));
            notification.open();
        },
                "Cancel", onClose -> {
            onClose.getSource().close();
            notification.open();
        });
        dialog.setConfirmButtonTheme("error primary");
        @SuppressWarnings("unchecked")
        Button button = new Button(VaadinIcon.TRASH.create(), buttonClickEvent -> {
            notification.close();
            dialog.open();
        });
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }

    //Closes the DialogBox
    private void onClose(ConfirmDialog.CancelEvent cancelEvent) {
        cancelEvent.getSource().close();
        grid.setItems(MySQLMethods.getStudentData());
    }

    public Button expandButton(StudentData student) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Expand", buttonClickEvent -> {
//            ListDataProvider<StudentData> dataProvider = (ListDataProvider<StudentData>) grid
//                    .getDataProvider();
//            VerticalLayout fullData = new VerticalLayout(close, GetStudentEvents.viewEvents(student));
//            fullData.setAlignItems(FlexComponent.Alignment.CENTER);
//            setContent(fullData);
            System.out.println("Notification");
            Notification fullData = new Notification();
            Button close = new Button("Close");
            VerticalLayout layout = new VerticalLayout(close);
            List<Component> arr = GetStudentEvents.viewEvents(student.getStudent());
            Grid<StudentData> studentGrid = (Grid<StudentData>) arr.get(2);
            Grid<Event> events = (Grid<Event>) arr.get(0);
            events.addComponentColumn(event -> deleteButton(event, events, fullData, studentGrid)).setHeader("Delete");
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
     * Runs if user wants to Export Data
     */
    //TODO find a way to export it (PDF + Spreadsheet)
    public void exportData() {

    }
}