package com.Frontend.Get;

import com.Backend.MySQLMethods;
import com.Backend.Student;
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
import com.vaadin.flow.component.notification.Notification;
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

import java.util.ArrayList;
import java.util.Arrays;

//TODO Make this class fully-functional
@Route("get-student-info")
public class GetStudentInformation extends AppLayout {

    Button close = new Button("Close", buttonClickEvent -> setContent(mainTable()));

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
        ArrayList<String> categoryOptions = new ArrayList<>(Arrays.asList("CSA Community (50 Hours)", "CSA Service (200 Hours", "CSA Achievement (500 Hours)"));
        grid.addEditColumn(StudentData::getCommunityServiceCategory, "category")
                .select(StudentData::setCommunityServiceCategory, categoryOptions)
                .setHeader("CS Category");
        grid.addEditColumn(StudentData::getEmail)
                .text(StudentData::setEmail)
                .setHeader("Email");
        grid.addEditColumn(StudentData::getYearsDone, "years", "integer")
                .text(StudentData::setYearsDone)
                .setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited).setHeader("Last Edited");
        grid.addComponentColumn(item -> expandButton(grid, item)).setHeader("Expand");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addItemClickListener(click -> {
            Student selected = click.getItem().getStudent();
            Notification fullData = new Notification();
            Button close = new Button("Close");
            fullData.add(close, GetStudentEvents.viewEvents(selected));
            fullData.setPosition(Notification.Position.MIDDLE);
            fullData.open();
            close.addClickListener(onClick -> fullData.close());
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
        return aligner;
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

    public Button deleteButton(Grid<StudentData> grid, Student student) {
        return new Button();
    }

    public Button expandButton(Grid<StudentData> grid, StudentData student) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Expand", buttonClickEvent -> {
//            ListDataProvider<StudentData> dataProvider = (ListDataProvider<StudentData>) grid
//                    .getDataProvider();
            VerticalLayout fullData = new VerticalLayout(close, GetStudentEvents.viewEvents(student));
            fullData.setAlignItems(FlexComponent.Alignment.CENTER);
            setContent(fullData);
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