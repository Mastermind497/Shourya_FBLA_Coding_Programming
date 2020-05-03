package com.frontend.GetStudentInformation;

import com.backend.Event;
import com.backend.MySQLMethods;
import com.backend.Student;
import com.backend.StudentData;
import com.frontend.Add.AddHours.AddHours;
import com.frontend.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.*;

/**
 * Gets All Student's General Information and Allows Editing. Also Allows viewing more specific events.
 */
@Route(value = "get-student-info", layout = MainView.class)
@PageTitle("View and Edit Information | FBLA Genie")
@PreserveOnRefresh
public class GetStudentInformation extends VerticalLayout {

    /** The Main Grid of Student Data */
    GridPro<StudentData> grid;

    /**
     * The Constructor, just runs the Main Table Method
     */
    public GetStudentInformation() {
        mainTable();
    }

    /**
     * Creates the main table, which shows student data and allows editing of it
     */
    public void mainTable() {
        removeAll();
        H1 header = new H1("View And Edit Student Information");
        add(header);
        //Shows data on a grid (Up to 100k pieces)
        List<StudentData> data = MySQLMethods.getStudentData();
        data.sort(Comparator.comparing(Student::getLastName));

        //Creates a Grid with Inline editing and Sorting
        grid = setUpStudentGrid(data);
        grid.addComponentColumn(this::expandButton).setHeader("Expand");
        grid.addComponentColumn(this::editStudent).setHeader("Edit");
        grid.addComponentColumn(this::deleteButton).setHeader("Delete");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.setMultiSort(true);

        add(grid);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setAlignSelf(FlexComponent.Alignment.CENTER);
    }

    /**
     * Creates the delete button, which deletes a student if clicked
     *
     * @param student The Student to create a delete button for
     * @return The Delete Button
     */
    public Button deleteButton(Student student) {
        ConfirmDialog dialog = new ConfirmDialog("Confirm Delete",
                String.format("Are you sure you want to delete %s? This action cannot be undone", student.getFirstName() + " " + student.getLastName()),
                "Delete", onDelete -> {
            student.delete();
            grid.setItems(MySQLMethods.getStudentData());
        },
                "Cancel", this::onClose);
        dialog.setConfirmButtonTheme("error primary");
        Button button = new Button(VaadinIcon.TRASH.create(), buttonClickEvent ->
                dialog.open()
        );
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }

    /**
     * Allows the deleting of events in the View
     *
     * @param event               The event being deleted
     * @param studentNotification The notification from where the event was selected
     * @param eventsGrid          The grid of events to update
     * @return A Button with the capability to delete an event
     */
    public Button deleteButton(Event event, Notification studentNotification, Grid<Event> eventsGrid) {
        ConfirmDialog dialog = new ConfirmDialog("Confirm Delete",
                String.format("Are you sure you want to delete %s, done by %s? This action cannot be undone", event.getEventName(), event.getFirstName() + " " + event.getLastName()),
                "Delete", onDelete -> {
            event.delete();
            grid.setItems(MySQLMethods.getStudentData());
            eventsGrid.setItems(MySQLMethods.selectStudentEventsAsEvent(event));
            studentNotification.open();
        },
                "Cancel", cancelEvent -> {
            onClose(cancelEvent);
            eventsGrid.setItems(MySQLMethods.selectStudentEventsAsEvent(event));
            studentNotification.open();

        });
        dialog.setConfirmButtonTheme("error primary");
        Button button = new Button(VaadinIcon.TRASH.create(), buttonClickEvent -> {
            dialog.open();
            studentNotification.close();
        });
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }

    /**
     * Closes the Dialog Box and Resets the table with the updated values of the Student
     *
     * @param cancelEvent The Event that the dialog box was cancelled
     */
    private void onClose(ConfirmDialog.CancelEvent cancelEvent) {
        cancelEvent.getSource().close();
        grid.setItems(MySQLMethods.getStudentData());
    }

    /**
     * Creates the expand button, which reveals more specific student information, namely the events
     * <p>
     * This method creates a dialog box (as a notification) which has all of a student's events and data, allowing
     * for a more in-depth review of a student
     *
     * @param student The Student to Expand
     * @return The Expand Button
     */
    public Button expandButton(StudentData student) {
        Button button = new Button("Expand", buttonClickEvent -> {
            System.out.println("Notification");
            Notification fullData = new Notification();
            Button close = new Button("Close");
            VerticalLayout layout = new VerticalLayout(close);

            List<Component> arr = viewEvents(student);

            @SuppressWarnings("unchecked")
            Grid<StudentData> studentGrid = (Grid<StudentData>) arr.get(2);
            @SuppressWarnings("unchecked")
            Grid<Event> events = (Grid<Event>) arr.get(0);
            events.addComponentColumn(event -> editEvent(event, fullData, events, studentGrid)).setHeader("Edit");
            events.addComponentColumn(event -> deleteButton(event, fullData, events)).setHeader("Delete");
            events.setMultiSort(true);
            events.setMaxHeight("20em");
            VerticalLayout studentInfo = (VerticalLayout) arr.get(1);
            studentInfo.add(studentGrid, events);
            studentInfo.setMaxHeight("25em");

            //spacer for close button
            layout.add(new H6(" "));
            layout.add(studentInfo);
            //Adds a Button to Add Events
            layout.add(new Button("Add a New Event", buttonClickEvent1 -> UI.getCurrent().navigate(AddHours.class)));
            layout.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.setWidth("73em");
            layout.setMaxHeight("30em");
            fullData.add(layout);
            fullData.setPosition(Notification.Position.MIDDLE);
            fullData.open();
            close.addClickListener(onClick -> {
                fullData.close();
                grid.setItems(MySQLMethods.getStudentData());
            });
            System.out.println("End Notification");
        });
        button.addThemeVariants();
        button.setIcon(VaadinIcon.EXPAND_FULL.create());
        return button;
    }

    /**
     * Creates a button which allows editing a student in a form layout
     *
     * @param student The Student to Edit
     * @return A Button with this functionality
     */
    public Button editStudent(Student student) {
        StudentData toEdit = student.getStudentData();

        Button editor = new Button("Edit", VaadinIcon.EDIT.create());

        Notification editStudent = new Notification();
        editStudent.setPosition(Notification.Position.MIDDLE);

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("73em");
        layout.setMaxHeight("30em");
        layout.setAlignItems(Alignment.CENTER);

        FormLayout editForm = new FormLayout();
        editForm.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));

        /* Make Different Input Fields */
        TextField firstNameField = new TextField("First Name");
        firstNameField.setValue(toEdit.getFirstName());
        firstNameField.setPlaceholder(toEdit.getFirstName());
        firstNameField.setValueChangeMode(ValueChangeMode.EAGER);

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setValue(toEdit.getLastName());
        lastNameField.setPlaceholder(toEdit.getLastName());
        lastNameField.setValueChangeMode(ValueChangeMode.EAGER);

        IntegerField studentIDField = new IntegerField("Student ID");
        studentIDField.setValue(toEdit.getStudentID());
        studentIDField.setPlaceholder(Integer.toString(toEdit.getStudentID()));
        studentIDField.setValueChangeMode(ValueChangeMode.EAGER);

        IntegerField gradeField = new IntegerField("Grade");
        gradeField.setValue(toEdit.getGradeInt());
        gradeField.setPlaceholder(Integer.toString(toEdit.getGradeInt()));
        gradeField.setHasControls(true);
        gradeField.setStep(1);
        gradeField.setMin(6);
        gradeField.setMax(12);
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);
        gradeField.setErrorMessage("That is not a valid grade");

        EmailField emailField = new EmailField("Email");
        emailField.setValue(toEdit.getEmail());
        emailField.setPlaceholder(toEdit.getEmail());
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        emailField.setValueChangeMode(ValueChangeMode.EAGER);

        RadioButtonGroup<String> communityServiceCategoryField = new RadioButtonGroup<>();
        communityServiceCategoryField.setLabel("Community Service Award Category");
        communityServiceCategoryField.setItems("CSA Community (50 Hours)", "CSA Service (200 Hours)",
                "CSA Achievement (500 Hours)");
        communityServiceCategoryField.setValue(toEdit.getCommunityServiceCategory());
        communityServiceCategoryField.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        NumberField communityServiceHoursField = new NumberField("Community Service Hours");
        communityServiceHoursField.setValue(toEdit.getCommunityServiceHours());
        communityServiceHoursField.setPlaceholder(Double.toString(toEdit.getCommunityServiceHours()));
        communityServiceHoursField.setMin(0);
        communityServiceHoursField.setStep(0.5d);
        communityServiceHoursField.setHasControls(true);

        IntegerField yearsDoneField = new IntegerField("Years Done");
        yearsDoneField.setValue((int) toEdit.getYearsDone());
        yearsDoneField.setPlaceholder(Short.toString(toEdit.getYearsDone()));
        yearsDoneField.setMin(1);
        yearsDoneField.setMax(4);
        yearsDoneField.setHasControls(true);
        yearsDoneField.setStep(1);

        editForm.add(firstNameField, 1);
        editForm.add(lastNameField, 1);
        editForm.add(studentIDField, 1);
        editForm.add(emailField, 2);
        editForm.add(gradeField, 1);
        editForm.add(communityServiceCategoryField, 1);
        editForm.add(communityServiceHoursField, 1);
        editForm.add(yearsDoneField, 1);

        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        Button cancel = new Button("Cancel");
        actions.add(save, cancel, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(Alignment.CENTER);
        actions.setAlignSelf(Alignment.CENTER);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(buttonClickEvent -> {
            try {
                toEdit.updateFirstName(firstNameField.getValue());
                toEdit.updateLastName(lastNameField.getValue());
                toEdit.updateStudentID(studentIDField.getValue());
                toEdit.updateGrade(gradeField.getValue());
                toEdit.updateEmail(emailField.getValue());
                toEdit.updateCommunityServiceCategory(communityServiceCategoryField.getValue());
                if (communityServiceHoursField.getValue() > 0)
                    toEdit.updateCommunityServiceHours(communityServiceHoursField.getValue());
                else toEdit.updateCommunityServiceHours(0);
                toEdit.updateYearsDone(yearsDoneField.getValue());

                Notification success = new Notification();
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                Label succeeded = new Label("The Student Was Successfully Updated!");
                success.add(succeeded);
                success.setDuration(3000);
                success.open();
            } catch (Exception e) {
                Notification invalid = new Notification();
                invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Label failed = new Label("There was an error when updating the student. Changes may be incomplete.");
                invalid.add(failed);
                invalid.setDuration(3000);
                invalid.open();
            }
            grid.setItems(MySQLMethods.getStudentData());
            editStudent.close();
        });

        cancel.addClickListener(buttonClickEvent -> editStudent.close());

        reset.addClickListener(buttonClickEvent -> {
            firstNameField.setValue(toEdit.getFirstName());
            lastNameField.setValue(toEdit.getLastName());
            studentIDField.setValue(toEdit.getStudentID());
            gradeField.setValue(toEdit.getGradeInt());
            emailField.setValue(toEdit.getEmail());
            communityServiceCategoryField.setValue(toEdit.getCommunityServiceCategory());
            communityServiceHoursField.setValue(toEdit.getCommunityServiceHours());
            yearsDoneField.setValue((int) toEdit.getYearsDone());

            Notification success = new Notification();
            success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Label succeeded = new Label("The Data was Successfully Reset");
            success.add(succeeded);
            success.setDuration(3000);
            success.open();
        });

        layout.add(editForm, actions);

        editStudent.add(layout);

        editor.addClickListener(buttonClickEvent -> editStudent.open());
        return editor;
    }

    /**
     * Creates a button which allows editing an event within the form layout
     *
     * @param event             The event to edit
     * @param eventNotification The notification of expanding
     * @param eventGrid         The grid of events
     * @param studentDataGrid   The grid of student data
     * @return A button which opens the editing dialogue
     */
    public Button editEvent(Event event, Notification eventNotification, Grid<Event> eventGrid, Grid<StudentData> studentDataGrid) {
        Button editor = new Button("Edit", VaadinIcon.EDIT.create());

        Notification editEvent = new Notification();
        editEvent.setPosition(Notification.Position.MIDDLE);

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("73em");
        layout.setMaxHeight("30em");
        layout.setAlignItems(Alignment.CENTER);

        FormLayout editForm = new FormLayout();
        editForm.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));

        /* Makes Labels for Different Input Fields */
        //Gets all Students as Options for Adding Hours
        List<Student> students = MySQLMethods.getStudents();
        ComboBox<Student> studentChoices = new ComboBox<>("Student Name");
        studentChoices.setItems(students);
        studentChoices.setValue(event.getStudent());
        studentChoices.setEnabled(false);

        //A Text Input field for the Name of the Event
        TextField eventName = new TextField("Event Name");
        eventName.setPlaceholder(event.getEventName()); //Example Name
        eventName.setValue(event.getEventName());
        eventName.setValueChangeMode(ValueChangeMode.EAGER);

        //A Number input field for the length of the event
        NumberField eventHours = new NumberField("Length of Event (Hours)");
        eventHours.setHasControls(true);
        eventHours.setStep(0.5d);
        eventHours.setMin(0);
        eventHours.setPlaceholder(Double.toString(event.getHours()));
        double length = event.getHours();
        eventHours.setValue(length);
        eventHours.setValueChangeMode(ValueChangeMode.EAGER);

        //A Date Picker to choose the exact date of the event
        DatePicker eventDate = new DatePicker("Date");
        eventDate.setClearButtonVisible(true);
        eventDate.setPlaceholder(event.getDate().toString());
        eventDate.setValue(event.getDate().getLocalDate());
        eventDate.setMax(LocalDate.now());

        //Adds Components with their desired widths
        editForm.add(studentChoices, 3);
        editForm.add(eventName, 2);
        editForm.add(eventHours, 1);
        editForm.add(eventDate, 1);

        HorizontalLayout actions = new HorizontalLayout();
        Button save = new Button("Save");
        Button reset = new Button("Reset");
        Button cancel = new Button("Cancel");
        actions.add(save, cancel, reset);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        actions.setAlignItems(Alignment.CENTER);
        actions.setAlignSelf(Alignment.CENTER);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        layout.add(editForm, actions);

        editEvent.add(layout);

        save.addClickListener(buttonClickEvent -> {
            try {
                event.updateEventName(eventName.getValue());
                event.updateHours(eventHours.getValue());
                event.updateDate(eventDate.getValue());

                Notification success = new Notification();
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                Label succeeded = new Label("The Event Was Successfully Updated!");
                success.add(succeeded);
                success.setDuration(3000);
                success.open();
            } catch (Exception e) {
                Notification invalid = new Notification();
                invalid.addThemeVariants(NotificationVariant.LUMO_ERROR);
                Label failed = new Label("There was an error when updating the event. Changes may be incomplete.");
                invalid.add(failed);
                invalid.setDuration(3000);
                invalid.open();
            }
            grid.setItems(MySQLMethods.getStudentData());
            studentDataGrid.setItems(event.getStudentData());
            eventGrid.setItems(MySQLMethods.selectStudentEventsAsEvent(event));
            editEvent.close();
            eventNotification.open();
        });

        cancel.addClickListener(buttonClickEvent -> {
            editEvent.close();
            eventNotification.open();
        });

        reset.addClickListener(buttonClickEvent -> {
            studentChoices.setValue(event);
            eventHours.setValue(event.getHours());
            eventName.setValue(event.getEventName());
            eventDate.setValue(event.getDate().getLocalDate());

            Notification success = new Notification();
            success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Label succeeded = new Label("The Data was Successfully Reset");
            success.add(succeeded);
            success.setDuration(3000);
            success.open();
        });

        layout.add(editForm, actions);

        editEvent.add(layout);

        editor.addClickListener(buttonClickEvent -> {
            eventNotification.close();
            editEvent.open();
        });
        return editor;
    }

    /**
     * Views events and hours done by the chosen student.
     * <p>
     * This method is used by the expandButton to add all of the events to the dialog box
     *
     * @param chosen The chosen student
     */
    private List<Component> viewEvents(Student chosen) {
        List<StudentData> studentData = new ArrayList<>();
        studentData.add(chosen.getStudentData());

        //Creates a Grid with Inline editing and Sorting
        GridPro<StudentData> grid = setUpStudentGrid(studentData);

        grid.setHeightByRows(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<StudentData> al : grid.getColumns()) {
            al.setAutoWidth(true);
        }

        VerticalLayout layout = new VerticalLayout();

        List<Event> eventList = MySQLMethods.selectStudentEventsAsEvent(chosen);
        Collections.sort(eventList);

        //Creates a Grid for all of the Events a student has
        GridPro<Event> events = new GridPro<>();
        events.setItems(eventList);
        events.addEditColumn(Event::getEventName, "Name", "String")
                .text(Event::updateEventName)
                .setHeader("Event Name");
        events.addEditColumn(Event::getHours, "Hours", "Double")
                .text((event, hours) -> {
                    event.updateHours(hours);
                    grid.setItems(chosen.getStudentData());
                })
                .setHeader("Hours of Event");
        events.addEditColumn(Event::getDate)
                .text(Event::setDate)
                .setHeader("Date of Event");

        //Makes them AutoWidth, which fixes width for data length
        for (Grid.Column<Event> al : events.getColumns()) {
            al.setAutoWidth(true);
        }

        events.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        List<Component> newArr = new ArrayList<>();
        newArr.add(events);
        newArr.add(layout);
        newArr.add(grid);

        return newArr;
    }

    /**
     * Sets up the student grid for easy viewing and manipulation
     *
     * @param studentData A List of all the students to be added to the grid
     * @return The Grid Containing all of the student information
     */
    private GridPro<StudentData> setUpStudentGrid(List<StudentData> studentData) {
        GridPro<StudentData> grid = new GridPro<>();
        grid.setItems(studentData);
        grid.addEditColumn(StudentData::getFirstName, "name")
                .text(StudentData::updateFirstName)
                .setHeader("First Name");
        grid.addEditColumn(StudentData::getLastName, "name")
                .text(StudentData::updateLastName)
                .setHeader("Last Name");
        grid.addEditColumn(StudentData::getStudentID, "idNumber")
                .text(StudentData::updateStudentID)
                .setHeader("Student ID");
        grid.addEditColumn(StudentData::getGrade, "grade", "integer")
                .text(StudentData::updateGrade)
                .setHeader("Grade");
        grid.addEditColumn(StudentData::getCommunityServiceHours, "hours", "double")
                .text(StudentData::updateCommunityServiceHours)
                .setHeader("CS Hours");
        List<String> categoryOptions = new ArrayList<>(Arrays.asList("CSA Community (50 Hours)", "CSA Service (200 Hours)", "CSA Achievement (500 Hours)"));
        grid.addEditColumn(StudentData::getCommunityServiceCategory, "category")
                .select(StudentData::updateCommunityServiceCategory, categoryOptions)
                .setHeader("CS Category");
        grid.addEditColumn(StudentData::getEmail)
                .text(StudentData::updateEmail)
                .setHeader("Email");
        grid.addEditColumn(StudentData::getYearsDone, "years", "integer")
                .text(StudentData::updateYearsDone)
                .setHeader("Years Done");
        grid.addColumn(StudentData::getLastEdited, "date", "lastedited").setHeader("Last Edited");

        return grid;
    }
}