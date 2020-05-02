package com.frontend.Add.CreateStudent;

import com.backend.StudentData;
import com.frontend.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import static com.backend.MySQLMethods.selectTrackerString;

/**
 * The Class Used to Allow a User to Add a New Student to their List of Students
 */
@Route(value = "create-student", layout = MainView.class)
@PageTitle("Create a Student | FBLA Genie")
@PreserveOnRefresh
public class CreateStudent extends VerticalLayout {
    private static int count = 0;

    /**
     * The Overall Path to create a new Student
     */
    public CreateStudent() {
        removeAll();
        H1 header = new H1("Add a Student");
        add(header);
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


        /* Make Different Input Fields */
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

        EmailField emailField = new EmailField("Email");
        emailField.setPlaceholder("john.doe@highschoolstudent.org");
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        emailField.setValueChangeMode(ValueChangeMode.EAGER);

        /* Creates a Dropdown to Choose the Community Service Award Category Goal */
        Select<String> communityServiceCategoryField = new Select<>();
        communityServiceCategoryField.setItems("CSA Community (50 Hours)", "CSA Service (200 Hours)",
                "CSA Achievement (500 Hours)");
        communityServiceCategoryField.setPlaceholder("CSA Community (50 Hours)");
        communityServiceCategoryField.setLabel("Community Service Category");

        //Will state number of years
        Div numberOfYears = new Div();
        numberOfYears.setText("Current Number of Years: " + count);

        //Vertically Aligned Checkboxes for years done
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

        //Adds the Form to the layout, keeping padding and spacing to keep things looking clean and cozy
        full.add(addStudentForm);
        full.setSpacing(true);
        full.setMargin(true);
        full.setAlignItems(Alignment.CENTER);

        //Button Bar
        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        actions.add(save, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(Alignment.CENTER);
        actions.setAlignSelf(Alignment.CENTER);
        save.addClickShortcut(Key.ENTER);

        /* Binds Data Entered to Fields */
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
                        "Please enter the email address", 5, null))
                .bind(StudentData::getEmail, StudentData::setEmail);
        binder.forField(emailField)
                .withValidator(s -> s.contains("@"), "Not a Valid Email Address") //Makes sure an email address is valid
                .bind(StudentData::getEmail, StudentData::setEmail);
        binder.forField(emailField)
                .withValidator(s -> s.contains("."), "Not a Valid Email Address") //Makes sure an email address is valid
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
                Notification invalid = new Notification();
                invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Label failed = new Label("Years Done Can Not Be Less Than 1");
                invalid.add(failed);
                invalid.setDuration(3000);
                invalid.open();
            } else if (!(emailField.getValue().contains("@") && emailField.getValue().contains("."))) {
                Notification invalid = new Notification();
                invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Label failed = new Label("Not A Valid Email");
                invalid.add(failed);
                invalid.setDuration(3000);
                invalid.open();
                emailField.setErrorMessage("Not A Valid Email Address");
            } else if (binder.writeBeanIfValid(student)) {
                if (selectTrackerString(student.getFirstName(), student.getLastName(), student.getStudentID(), "firstName") == null) {
                    student.setYearsDone((short) count);
                    student.createStudent();
                    Notification.show("Your data is being processed...");
                    binder.readBean(null);
                    Notification invalid = new Notification();
                    invalid.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    Label failed = new Label("The Student Was Successfully Added!");
                    invalid.add(failed);
                    invalid.setDuration(3000);
                    invalid.open();
                    communityServiceCategoryField.setInvalid(false);
                } else {
                    Notification invalid = new Notification();
                    invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    Label failed = new Label("A Student With Those Details Already Exists");
                    invalid.add(failed);
                    invalid.setDuration(3000);
                    invalid.open();
                }
            } else {
                Notification invalid = new Notification();
                invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Label failed = new Label("An Unidentifiable Error Occurred. Please Try Again.");
                invalid.add(failed);
                invalid.setDuration(3000);
                invalid.open();
            }
        });

        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
        });

        add(full, actions);
        setAlignItems(Alignment.CENTER);
    }

    private static void addToCount() {
        count++;
    }

    private static void removeFromCount() {
        count--;
    }
}
