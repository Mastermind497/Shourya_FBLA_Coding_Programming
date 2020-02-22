package com.frontend;

import com.backend.FileMethods;
import com.backend.Student;
import com.backend.StudentData;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.backend.MySQLMethods.selectTrackerString;
import static com.frontend.CreateStudent.*;

@Route("edit-student-info")
public class EditStudentInformation extends AppLayout {
    Student selected;
    public EditStudentInformation() throws IOException {
        addToNavbar(Home.makeHeader());

        //Grouping in a Vertical Column
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to Edit
        //Makes Labels for Different Input Fields
        ArrayList<Student> students = new ArrayList<>(Arrays.asList(FileMethods.getStudents()));
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

        Button edit = new Button("Edit This Student");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        selector.add(studentChoices, edit);
        selector.setAlignItems(FlexComponent.Alignment.CENTER);
        selector.setAlignSelf(FlexComponent.Alignment.CENTER);

        setContent(selector);

        //If Button is Clicked
        edit.addClickListener(event ->{
            //Creates a Horizontal Layout to decrease maximum width
            HorizontalLayout full = new HorizontalLayout();

            //A Structure that changes size and shape depending on screen
            FormLayout addStudentForm = new FormLayout();

            //A Data Type to store all information on a student
            Binder<StudentData> binder = new Binder<>();
            StudentData student = new StudentData();

            //Configures the Form
            addStudentForm.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("20em", 1),
                    new FormLayout.ResponsiveStep("30em", 2),
                    new FormLayout.ResponsiveStep("40em", 3));


            //Makes Labels for Different Input Fields
            TextField firstNameField = new TextField("First Name");
            firstNameField.setValue(selected.getFirstName());
            firstNameField.setValueChangeMode(ValueChangeMode.EAGER);

            TextField lastNameField = new TextField("Last Name");
            lastNameField.setValue(selected.getLastName());
            lastNameField.setValueChangeMode(ValueChangeMode.EAGER);

            IntegerField studentIDField = new IntegerField("Student ID");
            studentIDField.setValue(selected.getStudentID());
            studentIDField.setValueChangeMode(ValueChangeMode.EAGER);

            IntegerField gradeField = new IntegerField("Grade");
            gradeField.setPlaceholder("10");
            gradeField.setValueChangeMode(ValueChangeMode.EAGER);

            //Dropdown Menu
            Select<String> communityServiceCategoryField = new Select<>();
            communityServiceCategoryField.setItems("CSA Community", "CSA Service", "CSA Achievement");
            communityServiceCategoryField.setPlaceholder("Community Service Category");

            EmailField emailField = new EmailField("Email");
            emailField.setPlaceholder("john.doe@highschoolstudent.org");
            emailField.setClearButtonVisible(true);
            emailField.setErrorMessage("Please enter a valid email address");
            emailField.setValueChangeMode(ValueChangeMode.EAGER);

            //Will state number of years
            Div numberOfYears = new Div();
            numberOfYears.setText("Current Number of Years: " + getCount());

            //Vertically Aligned Checkboxes
            VerticalLayout checkBoxes = new VerticalLayout();
            Checkbox freshman = new Checkbox("9th Grade");
            freshman.addValueChangeListener(e -> {
                if (freshman.getValue())
                    addToCount();
                else if (!freshman.getValue()) {
                    removeFromCount();
                }
                numberOfYears.setText("Currently Selected Number of Years Done: " + getCount());
            });
            Checkbox sophomore = new Checkbox("10th Grade");
            sophomore.addValueChangeListener(e -> {
                if (sophomore.getValue())
                    addToCount();
                else if (!sophomore.getValue()) {
                    removeFromCount();
                }
                numberOfYears.setText("Currently Selected Number of Years Done: " + getCount());
            });
            Checkbox junior = new Checkbox("11th Grade");
            junior.addValueChangeListener(e -> {
                if (junior.getValue())
                    addToCount();
                else if (!junior.getValue()) {
                    removeFromCount();
                }
                numberOfYears.setText("Currently Selected Number of Years Done: " + getCount());
            });
            Checkbox senior = new Checkbox("12th Grade");
            senior.addValueChangeListener(e -> {
                if (senior.getValue())
                    addToCount();
                else if (!senior.getValue()) {
                    removeFromCount();
                }
                numberOfYears.setText("Currently Selected Number of Years Done: " + getCount());
            });
            checkBoxes.add("Grade Levels Participated in FBLA (Including This Year)");
            checkBoxes.add(freshman, sophomore, junior, senior);
            checkBoxes.setPadding(false);
            checkBoxes.setSpacing(false);
            checkBoxes.setMargin(false);

            //Makes all components required
            firstNameField.setRequiredIndicatorVisible(true);
            lastNameField.setRequiredIndicatorVisible(true);
            studentIDField.setRequiredIndicatorVisible(true);
            emailField.setRequiredIndicatorVisible(true);
            gradeField.setRequiredIndicatorVisible(true);
            communityServiceCategoryField.setRequiredIndicatorVisible(true);

            //Adds Components with their desired widths
            addStudentForm.add(firstNameField, 1);
            addStudentForm.add(lastNameField, 1);
            addStudentForm.add(studentIDField, 1);
            addStudentForm.add(emailField, 2);
            addStudentForm.add(gradeField, 1);
            addStudentForm.add(communityServiceCategoryField, 1);
            addStudentForm.add(checkBoxes, 1);
            addStudentForm.add(numberOfYears, 1);

            full.add(addStudentForm);
            full.setSpacing(true);
            full.setMargin(true);
            full.setAlignItems(FlexComponent.Alignment.CENTER);

            //Button Bar
            HorizontalLayout actions = new HorizontalLayout();
            Button save = new Button("Save");
            Button reset = new Button("Reset");
            actions.add(save, reset);
            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
            actions.setAlignItems(FlexComponent.Alignment.CENTER);
            actions.setAlignSelf(FlexComponent.Alignment.CENTER);

            binder.forField(firstNameField)
                    .withValidator(new StringLengthValidator(
                            "Please enter the first name", 1, null))
                    .bind(StudentData::getFirstName, StudentData::setFirstName);

            binder.forField(lastNameField)
                    .withValidator(new StringLengthValidator(
                            "Please enter the last name", 1, null))
                    .bind(StudentData::getLastName, StudentData::setLastName);

            binder.forField(studentIDField)
                    .withValidator(new IntegerRangeValidator(
                            "Please enter the Student ID", 1, null))
                    .bind(StudentData::getStudentID, StudentData::setStudentID);

            binder.forField(emailField)
                    .withValidator(new StringLengthValidator(
                            "Please enter the email address", 1, null))
                    .bind(StudentData::getEmail, StudentData::setEmail);

            binder.forField(gradeField)
                    .withValidator(new IntegerRangeValidator(
                            "Please enter the Current Grade", 9, 12))
                    .bind(StudentData::getGradeInt, StudentData::setGrade);

            binder.forField(communityServiceCategoryField).bind(StudentData::getCommunityServiceCategory, StudentData::setCommunityServiceCategory);

            binder.forField(freshman).bind(StudentData::isFreshman, StudentData::setFreshman);
            binder.forField(sophomore).bind(StudentData::isSophomore, StudentData::setSophomore);
            binder.forField(junior).bind(StudentData::isJunior, StudentData::setJunior);
            binder.forField(senior).bind(StudentData::isSenior, StudentData::setSenior);

            //add listeners for the buttons
            save.addClickListener(e -> {
                if (binder.writeBeanIfValid(student)) {
                    if (selectTrackerString(student.getFirstName(), student.getLastName(), student.getStudentID(), "firstName") == null) {
                        student.setYearsDone((short) getCount());
                        student.createStudent();
                        try {
                            FileMethods.addToStudent(student.getFirstName(), student.getLastName(), student.getStudentID());
                        } catch (IOException IOExceptionE) {
                            Notification.show("Student couldn't be added to File");
                        }
                        Notification.show("Your data is being processed");
                        binder.readBean(null);
                        Notification.show("Your data has been processed!");
                    } else {
                        Notification.show("A Student with those details already exists");
                    }
                } else {
                    Notification.show("There was an error. Please Try Again");
                }
            });

            reset.addClickListener(e -> {
                // clear fields by setting null
                binder.readBean(null);
            });

            VerticalLayout container = new VerticalLayout(full, actions);
            container.setAlignItems(FlexComponent.Alignment.CENTER);
            setContent(container);
        });
    }
}
