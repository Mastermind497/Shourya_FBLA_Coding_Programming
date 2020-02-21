package com.frontend;


import com.backend.FileMethods;
import com.backend.Student;
import com.backend.StudentData;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;

@Route("get-student-info/individual")
public class GetStudentInformationIndividual extends AppLayout {
    //The Selected Student to view (Static to bypass lambda limitations
    static Student selected;
    public GetStudentInformationIndividual() throws Exception {
        addToNavbar(Home.makeHeader());



        VerticalLayout view = new VerticalLayout();

        //Makes Labels for Different Input Fields
        ArrayList<Student> students = new ArrayList<>(Arrays.asList(FileMethods.getStudents()));
        //Adds Create New Student Option
        students.add(new Student(true));
        ComboBox<Student> studentChoices = new ComboBox<>();
        studentChoices.setItems(students);
        studentChoices.addValueChangeListener(e ->
            selected = studentChoices.getValue()
        );

        HorizontalLayout choice = new HorizontalLayout();
        Button viewData = new Button("View Data");
        viewData.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        viewData.addClickListener(event ->
            viewData()
        );

        Button exportData = new Button("Export Data");
        exportData.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        viewData.addClickListener(event ->
            exportData()
        );

        setContent(view);
    }

    /**
     * Runs if user wants to View Data
     */
    public static void viewData() {
        //Data is shown on a grid (Even though there is only one person)
        //This Grid does not support Lazy Loading because it is only one person
        ArrayList<StudentData> data = new ArrayList<>();

    }

    /**
     * Runs if user wants to Export Data
     */
    //TODO find a way to export it (PDF + Spreadsheet)
    public static void exportData() {

    }
}
