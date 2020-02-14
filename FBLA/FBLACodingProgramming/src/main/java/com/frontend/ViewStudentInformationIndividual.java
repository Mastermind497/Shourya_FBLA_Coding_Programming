package com.frontend;


import com.backend.FileMethods;
import com.backend.Student;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;

@Route("view-student-info/individual")
public class ViewStudentInformationIndividual extends AppLayout {
    //The Selected Student to view (Static to bypass lambda limitations
    static Student selected;
    public ViewStudentInformationIndividual() throws Exception {
        addToNavbar(Home.makeHeader());

        //Makes Labels for Different Input Fields
        ArrayList<Student> students = new ArrayList<>(Arrays.asList(FileMethods.getStudents()));
        //Adds Create New Student Option
        students.add(new Student());
        ComboBox<Student> studentChoices = new ComboBox<>();
        studentChoices.setItems(students);
        studentChoices.addValueChangeListener(e ->
            selected = studentChoices.getValue()
        );

        setContent(studentChoices);

        if (ViewStudentInformationQuestion.viewData) {

        }
        else {

        }
    }
}
