package com.Frontend.Get;

import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.Home;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;

//TODO Make this class fully-functional
@Route("get-student-info")
@PageTitle("View and Edit Information | FBLA Genie")
public class GetStudentInformation extends AppLayout {

    Button close = new Button("Close", buttonClickEvent -> UI.getCurrent().getPage().reload());
    GridPro<StudentData> grid;

    public GetStudentInformation() {
        addToNavbar(Home.makeHeader());
//        //Creates Grid Data Holder
//        Crud<StudentData> crud = new Crud<>(StudentData.class, createStudentEditor());
//
//        //Data Provider to Search for Students
//        StudentDataProvider dataProvider = new StudentDataProvider();
//
//        crud.setDataProvider(dataProvider);
//        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
//        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));
//
//        crud.addThemeVariants(CrudVariant.NO_BORDER);

        mainTable();
    }

    public VerticalLayout mainTable() {
        //Shows data on a grid (Up to 100k pieces)
        ArrayList<StudentData> data = MySQLMethods.selectFullTracker();

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
        ArrayList<String> categoryOptions = new ArrayList<>(Arrays.asList("CSA Community (50 Hours)", "CSA Service (200 Hours)", "CSA Achievement (500 Hours)"));
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

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

//        grid.addItemDoubleClickListener(click -> {
//            Student selected = click.getItem().getStudent();
//            Notification fullData = new Notification();
//            Button close = new Button("Close");
//            VerticalLayout layout = new VerticalLayout(close, GetStudentEvents.viewEvents(selected));
//            layout.setAlignItems(FlexComponent.Alignment.CENTER);
//            layout.setWidth("73em");
//            fullData.add(layout);
//            fullData.setPosition(Notification.Position.MIDDLE);
//            fullData.open();
//            close.addClickListener(onClick -> fullData.close());
//        });

        //Layouts to help in orienting
        VerticalLayout aligner = new VerticalLayout();
        HorizontalLayout choice = new HorizontalLayout();

        Button exportData = new Button("Export Data");
        exportData.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        choice.add(exportData);
        choice.setAlignItems(FlexComponent.Alignment.CENTER);
        choice.setAlignSelf(FlexComponent.Alignment.CENTER);

        aligner.add(grid, choice);
        aligner.setAlignItems(FlexComponent.Alignment.CENTER);
        aligner.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(aligner);
        return aligner;
    }

    public Button deleteButton(Grid<StudentData> grid, Student student) {
        return new Button();
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
            VerticalLayout studentInfo = GetStudentEvents.viewEvents(student.getStudent());
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
                grid.setItems(MySQLMethods.selectFullTracker());
            });
            System.out.println("End Notification");
        });
        return button;
    }

    /**
     * Runs if user wants to Export Data
     */
    //TODO find a way to export it (PDF + Spreadsheet)
    public void exportData() {

    }
}