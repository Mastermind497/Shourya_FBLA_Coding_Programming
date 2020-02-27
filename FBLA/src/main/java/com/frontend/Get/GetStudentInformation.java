package com.frontend.Get;

import com.backend.MySQLMethods;
import com.backend.StudentData;
import com.frontend.Edit.EditStudentInformation;
import com.frontend.Home;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

//TODO Make this class fully-functional
@Route("get-student-info")
public class GetStudentInformation extends AppLayout {
    public GetStudentInformation() {
        addToNavbar(Home.makeHeader());

        //Shows data on a grid (Up to 100k pieces)
        ArrayList<StudentData> data = new ArrayList<>();
        try {
            data = MySQLMethods.selectFullTracker();
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }

        //Creates Grid Data Holder
        Grid<StudentData> grid = new Grid<>();
        grid.setItems(data);
        grid.addColumn(StudentData::getFirstName).setHeader("First Name");
        grid.addColumn(StudentData::getLastName).setHeader("Last Name");
        grid.addColumn(StudentData::getStudentID).setHeader("Student ID");
        grid.addColumn(StudentData::getGrade).setHeader("Grade");
        grid.addColumn(StudentData::getCommunityServiceHours).setHeader("CS Hours");
        grid.addColumn(StudentData::getCommunityServiceCategory).setHeader("CS Category");
        grid.addColumn(StudentData::getEmail).setHeader("Email");
        grid.addColumn(StudentData::getYearsDone).setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited).setHeader("Last Edited");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addItemDoubleClickListener(event -> {
            EditStudentInformation.selected = event.getItem().getStudent();
            UI.getCurrent().navigate(EditStudentInformation.class);
        });

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
    }

    /**
     * Runs if user wants to View Data
     */
    public void viewData() {

    }

    /**
     * Runs if user wants to Export Data
     */
    //TODO find a way to export it (PDF + Spreadsheet)
    public void exportData() {

    }
}