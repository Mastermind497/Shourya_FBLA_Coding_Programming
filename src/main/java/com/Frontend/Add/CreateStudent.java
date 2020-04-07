package com.Frontend.Add;

import com.Backend.StudentData;
import com.Frontend.Home;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.Backend.MySQLMethods.selectTrackerString;

@Route("create-student")
@PageTitle("Create a Student | FBLA Genie")
public class CreateStudent extends AppLayout {
    private static int count = 0;

    public CreateStudent() {
        //Creates the MenuBar again in this page
        addToNavbar(Home.makeHeader(Home.ADD_STUDENT_TAB));

        //Creates a Horizontal Layout to decrease maximum width
        HorizontalLayout full = new HorizontalLayout();

        //A Structure that changes size and shape depending on screen
        FormLayout addStudentForm = new FormLayout();

        //A Data Type to store all information on a student
        Binder<StudentData> binder = new Binder<>();
        StudentData student = new StudentData();

        //Configures the Form
        addStudentForm.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));


        //Makes Labels for Different Input Fields
        TextField firstNameField = new TextField("First Name");
        firstNameField.setPlaceholder("John");
        firstNameField.setValueChangeMode(ValueChangeMode.EAGER);

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setPlaceholder("Doe");
        lastNameField.setValueChangeMode(ValueChangeMode.EAGER);

        IntegerField studentIDField = new IntegerField("Student ID");
        studentIDField.setPlaceholder("123456");
        studentIDField.setValueChangeMode(ValueChangeMode.EAGER);

        IntegerField gradeField = new IntegerField("Grade");
        gradeField.setPlaceholder("10");
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);

        //Button Group

        Select<String> communityServiceCategoryField = new Select<>();
        communityServiceCategoryField.setItems("CSA Community (50 Hours)", "CSA Service (200 Hours)",
                "CSA Achievement (500 Hours)");
        communityServiceCategoryField.setPlaceholder("Community Service Category");
        communityServiceCategoryField.setLabel("Community Service Category");

        EmailField emailField = new EmailField("Email");
        emailField.setPlaceholder("john.doe@highschoolstudent.org");
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        emailField.setValueChangeMode(ValueChangeMode.EAGER);

        //Will state number of years
        Div numberOfYears = new Div();
        numberOfYears.setText("Current Number of Years: " + count);

        //Vertically Aligned Checkboxes
        VerticalLayout checkBoxes = new VerticalLayout();
        Checkbox freshman = new Checkbox("9th Grade");
        freshman.addValueChangeListener(event -> {
            if (freshman.getValue())
                addToCount();
            else if (!freshman.getValue()) {
                removeFromCount();
            }
            numberOfYears.setText("Currently Selected Number of Years Done: " + count);
        });
        Checkbox sophomore = new Checkbox("10th Grade");
        sophomore.addValueChangeListener(event -> {
            if (sophomore.getValue())
                addToCount();
            else if (!sophomore.getValue()) {
                removeFromCount();
            }
            numberOfYears.setText("Currently Selected Number of Years Done: " + count);
        });
        Checkbox junior = new Checkbox("11th Grade");
        junior.addValueChangeListener(event -> {
            if (junior.getValue())
                addToCount();
            else if (!junior.getValue()) {
                removeFromCount();
            }
            numberOfYears.setText("Currently Selected Number of Years Done: " + count);
        });
        Checkbox senior = new Checkbox("12th Grade");
        senior.addValueChangeListener(event -> {
            if (senior.getValue())
                addToCount();
            else if (!senior.getValue()) {
                removeFromCount();
            }
            numberOfYears.setText("Currently Selected Number of Years Done: " + count);
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
                        "Please enter the Current Grade", 6, 12))
                .bind(StudentData::getGradeInt, StudentData::setGrade);

        binder.forField(communityServiceCategoryField).bind(StudentData::getCommunityServiceCategory, StudentData::setCommunityServiceCategory);

        binder.forField(freshman).bind(StudentData::isFreshman, StudentData::setFreshman);
        binder.forField(sophomore).bind(StudentData::isSophomore, StudentData::setSophomore);
        binder.forField(junior).bind(StudentData::isJunior, StudentData::setJunior);
        binder.forField(senior).bind(StudentData::isSenior, StudentData::setSenior);

        //add listeners for the buttons
        save.addClickListener(event -> {
            if (count < 1) {
                Notification.show("Years Done Can not be Less Than 1");
            } else if (binder.writeBeanIfValid(student)) {
                if (selectTrackerString(student.getFirstName(), student.getLastName(), student.getStudentID(), "firstName") == null) {
                    student.setYearsDone((short) count);
                    student.createStudent();
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
        //ENTER Key also activated SAVE
        save.addClickShortcut(Key.ENTER);

        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
        });

        VerticalLayout container = new VerticalLayout(full, actions);
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        setContent(container);
    }

    public static void addToCount() {
        count++;
    }

    public static void removeFromCount() {
        count--;
    }

    public static int getCount() {
        return count;
    }
}
