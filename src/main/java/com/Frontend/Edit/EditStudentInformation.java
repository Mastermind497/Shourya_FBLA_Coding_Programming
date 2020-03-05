package com.Frontend.Edit;

import com.Backend.FileMethods;
import com.Backend.MySQLMethods;
import com.Backend.Student;
import com.Backend.StudentData;
import com.Frontend.Add.CreateStudent;
import com.Frontend.Home;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.Backend.MySQLMethods.selectTrackerDouble;
import static com.Backend.MySQLMethods.updateTracker;

@Route("edit-student-info")
public class EditStudentInformation extends AppLayout {
    public static Student selected;

    public EditStudentInformation() throws IOException {
        addToNavbar(Home.makeHeader());

        if (selected == null) {
            chooseStudent();
        }
        if (selected != null) {
            onButtonClick(selected);
        }
    }

    public VerticalLayout onButtonClick(Student studentSelected) {

        //Creates a Horizontal Layout to decrease maximum width
        HorizontalLayout full = new HorizontalLayout();

        //A Structure that changes size and shape depending on screen
        FormLayout addStudentForm = new FormLayout();

        //A Data Type to store all information on a student
        Binder<StudentData> binder = new Binder<>();
        //Creates a StudentData object based on current query
        StudentData student = studentSelected.getStudentData();

        //Configures the Form
        addStudentForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("20em", 1),
                new FormLayout.ResponsiveStep("30em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        //Makes Labels for Different Input Fields
        TextField firstNameField = new TextField("First Name");
        firstNameField.setValue(student.getFirstName());
        firstNameField.setValueChangeMode(ValueChangeMode.EAGER);

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setValue(student.getLastName());
        lastNameField.setValueChangeMode(ValueChangeMode.EAGER);

        IntegerField studentIDField = new IntegerField("Student ID");
        studentIDField.setValue(student.getStudentID());
        studentIDField.setValueChangeMode(ValueChangeMode.EAGER);

        IntegerField gradeField = new IntegerField("Grade");
        gradeField.setValue(student.getGradeInt());
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);

        //Dropdown Menu
        Select<String> communityServiceCategoryField = new Select<>();
        communityServiceCategoryField.setItems("CSA Community", "CSA Service", "CSA Achievement");
        communityServiceCategoryField.setValue(student.getCommunityServiceCategory());

        NumberField communityServiceHours = new NumberField("Community Service Hours");
        communityServiceHours.setValue(student.getCommunityServiceHours());
        communityServiceHours.setValueChangeMode(ValueChangeMode.EAGER);

        EmailField emailField = new EmailField("Email");
        emailField.setValue(student.getEmail());
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        emailField.setValueChangeMode(ValueChangeMode.EAGER);

        //Will state number of years
        IntegerField numberOfYears = new IntegerField();
        numberOfYears.setLabel("Number of Years Done:");
        numberOfYears.setValue((int) student.getYearsDone());

        //Makes all components required
        firstNameField.setRequiredIndicatorVisible(true);
        lastNameField.setRequiredIndicatorVisible(true);
        studentIDField.setRequiredIndicatorVisible(true);
        emailField.setRequiredIndicatorVisible(true);
        gradeField.setRequiredIndicatorVisible(true);
        communityServiceCategoryField.setRequiredIndicatorVisible(true);
        communityServiceHours.setRequiredIndicatorVisible(true);

        //Adds Components with their desired widths
        addStudentForm.add(firstNameField, 1);
        addStudentForm.add(lastNameField, 1);
        addStudentForm.add(studentIDField, 1);
        addStudentForm.add(emailField, 2);
        addStudentForm.add(gradeField, 1);
        addStudentForm.add(communityServiceCategoryField, 1);
        addStudentForm.add(communityServiceHours, 1);
        addStudentForm.add(numberOfYears, 1);

        full.add(addStudentForm);
        full.setSpacing(true);
        full.setMargin(true);
        full.setAlignItems(FlexComponent.Alignment.CENTER);

        //Button Bar
        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Update");
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
                        "Please enter the Current Grade", 6, 12))
                .bind(StudentData::getGradeInt, StudentData::setGrade);

        binder.forField(communityServiceCategoryField).bind(StudentData::getCommunityServiceCategory, StudentData::setCommunityServiceCategory);

        binder.forField(communityServiceHours)
                .withValidator(new DoubleRangeValidator(
                        "Please Enter a Value Number of Hours", 0.0, null))
                .bind(StudentData::getCommunityServiceHours, StudentData::setCommunityServiceHours);

        binder.forField(numberOfYears)
                .withValidator(new IntegerRangeValidator(
                        "Please Enter a Valid Number of Years", 1, 7))
                .bind(StudentData::getYearsDoneInt, StudentData::setYearsDone);

        //add listeners for the buttons
        save.addClickListener(e -> {
            Notification.show("Your data is being processed");
            if (binder.writeBeanIfValid(student)) {
                try {
                    double currentHours = selectTrackerDouble(
                            selected.getFirstName(), selected.getLastName(), selected.getStudentID(), "communityServiceHours");

                    double hourChange = student.getCommunityServiceHours() - currentHours;

                    //Gets today's date
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    MySQLMethods.addStudentHours(selected, "MANUAL ADJUSTMENT", hourChange, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DATE));
                } catch (Exception ex) {
                    Notification.show("Caught An Exception");
                }
                try {
                    updateTracker(studentSelected, student);
                } catch (Exception ex) {
                    Notification.show("Error Occurred, Please Try Again \n" + ex.getMessage());
                    ex.printStackTrace();
                }
                try {
                    FileMethods.removeStudent(studentSelected);
                    FileMethods.addToStudent(student.getStudent());
                } catch (IOException IOExceptionE) {
                    Notification.show("Student couldn't be added to File");
                }
                binder.readBean(null);
                Notification.show("Your data has been processed!");
                selected = null;
            } else {
                Notification.show("Your Data couldn't be added");
            }
        });
        //ENTER key also activates save
        save.addClickShortcut(Key.ENTER);

        reset.addClickListener(e -> {
            // clear fields by setting null
            binder.readBean(null);
            selected = null;
            try {
                setContent(chooseStudent());
            } catch (IOException ioe) {
                Notification.show(ioe.getMessage());
                ioe.printStackTrace();
            }
        });

        VerticalLayout container = new VerticalLayout(full, actions);
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        setContent(container);

        return container;
    }

    public VerticalLayout chooseStudent() throws IOException {
        //Grouping in a Vertical Column
        VerticalLayout selector = new VerticalLayout();

        //Choosing Student to Edit
        //Make Labels for Different Input Fields
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
        edit.addClickListener(event -> onButtonClick(selected));

        return selector;
    }
}
