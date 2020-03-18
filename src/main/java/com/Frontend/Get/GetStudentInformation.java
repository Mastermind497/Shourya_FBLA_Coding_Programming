package com.Frontend.Get;

import com.Backend.MySQLMethods;
import com.Backend.StudentData;
import com.Frontend.Home;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.Route;
import org.github.legioth.buttonrenderer.ButtonRendererBuilder;

import java.util.ArrayList;

//TODO Make this class fully-functional
@Route("get-student-info")
public class GetStudentInformation extends AppLayout {

    public GetStudentInformation() throws Exception {
        addToNavbar(Home.makeHeader());

        //Shows data on a grid (Up to 100k pieces)
        ArrayList<StudentData> data = MySQLMethods.selectFullTracker();

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

        //Creates a Grid with Inline editing and Sorting
        GridPro<StudentData> grid = new GridPro<>();
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
                .text(StudentData::setCommunityServiceHours)
                .setHeader("CS Hours");
        grid.addEditColumn(StudentData::getCommunityServiceCategory, "category")
                .text(StudentData::setCommunityServiceCategory)
                .setHeader("CS Category");
        grid.addEditColumn(StudentData::getEmail)
                .text(StudentData::setEmail)
                .setHeader("Email");
        grid.addEditColumn(StudentData::getYearsDone, "years", "integer")
                .text(StudentData::setYearsDone)
                .setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited).setHeader("Last Edited");
        grid.addColumn(
                new ButtonRendererBuilder<StudentData>(item -> System.out.println(item.getDeleteIcon().name()))
                        .withIconPrefix(item -> item.getDeleteIcon().create())
                        .withStyle(item -> "--lumo-border-radius: " + 10 + "px")
                        .build())
                .setHeader("Delete");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

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

    private CrudEditor<StudentData> createStudentEditor() {
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        IntegerField studentIDField = new IntegerField("Student ID");
        IntegerField gradeField = new IntegerField("Grade");
        Select<String> communityServiceCategoryField = new Select<>();
        communityServiceCategoryField.setItems("CSA Community", "CSA Service", "CSA Achievement");
        NumberField hours = new NumberField("Hours");
        EmailField emailField = new EmailField("Email");

        //Stores all search elements
        FormLayout form = new FormLayout(firstNameField, lastNameField, studentIDField, gradeField,
                communityServiceCategoryField, hours, emailField);

        Binder<StudentData> binder = new Binder<>();

        binder.forField(firstNameField).bind(StudentData::getFirstName, StudentData::setFirstName);

        binder.forField(lastNameField).bind(StudentData::getLastName, StudentData::setLastName);

        binder.forField(studentIDField)
                .withValidator(new IntegerRangeValidator(
                        "Please enter the Student ID", 1, null))
                .bind(StudentData::getStudentID, StudentData::setStudentID);

        binder.forField(emailField).bind(StudentData::getEmail, StudentData::setEmail);

        binder.forField(gradeField)
                .withValidator(new IntegerRangeValidator(
                        "Please enter the Current Grade", 6, 12))
                .bind(StudentData::getGradeInt, StudentData::setGrade);

        binder.forField(communityServiceCategoryField).bind(StudentData::getCommunityServiceCategory, StudentData::setCommunityServiceCategory);

        return new BinderCrudEditor<>(binder, form);
    }

    /**
     * Runs if user wants to Export Data
     */
    //TODO find a way to export it (PDF + Spreadsheet)
    public void exportData() {

    }
}