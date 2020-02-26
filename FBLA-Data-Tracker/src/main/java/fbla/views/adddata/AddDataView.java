package fbla.views.adddata;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.value.ValueChangeMode;
import fbla.backend.Employee;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fbla.MainView;

import java.util.ArrayList;

@Route(value = "add-data", layout = MainView.class)
@PageTitle("Add Data")
@CssImport("./styles/views/adddata/add-data-view.css")
public class AddDataView extends Div {

    private ComboBox<String> choices = new ComboBox<String>();
    private FormLayout formLayout = new FormLayout();
    private String option;
    private TextField firstname = new TextField();
    private TextField lastname = new TextField();
    private EmailField email = new EmailField();
    private TextArea notes = new TextArea();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    public AddDataView() {
        setId("add-data-view");
        VerticalLayout wrapper = createWrapper();

        createTitle(wrapper);
        createFormLayout(wrapper);
        createButtonLayout(wrapper);

        // Configure Form
        Binder<Employee> binder = new Binder<>(Employee.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> binder.readBean(null));
        save.addClickListener(e -> {
            Notification.show("Not implemented");
        });

        add(wrapper);
    }

    private void createTitle(VerticalLayout wrapper) {
        H1 h1 = new H1("Add Data");
        wrapper.add(h1);
    }

    private VerticalLayout createWrapper() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("wrapper");
        wrapper.setSpacing(false);
        return wrapper;
    }

    private void createFormLayout(VerticalLayout wrapper) {
        resetFormLayout(wrapper);
        if (option.equals("Add a Student")) {
            firstname.setPlaceholder("John");
            firstname.setValueChangeMode(ValueChangeMode.EAGER);
            FormLayout.FormItem firstNameItem =
                    addFormItem(wrapper, formLayout, firstname, "First name");
            FormLayout.FormItem lastNameItem =
                    addFormItem(wrapper, formLayout, lastname, "Last name");

        }
        else if (option.equals("Add Student Hours")) {

        }
        FormLayout.FormItem emailFormItem = addFormItem(wrapper, formLayout,
                email, "Email");
        formLayout.setColspan(emailFormItem, 2);
        FormLayout.FormItem notesFormItem = addFormItem(wrapper, formLayout,
                notes, "Notes");
        formLayout.setColspan(notesFormItem, 2);
    }

    private void createButtonLayout(VerticalLayout wrapper) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(cancel);
        buttonLayout.add(save);
        wrapper.add(buttonLayout);
    }

    private void setUpChoices() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Add a Student");
        options.add("Add Student Hours");
        choices.setItems(options);
        choices.addValueChangeListener(e -> {
            option = choices.getValue();
        });
    }

    private FormLayout.FormItem addFormItem(VerticalLayout wrapper,
            FormLayout formLayout, Component field, String fieldName) {
        FormLayout.FormItem formItem = formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
        return formItem;
    }

    private void resetFormLayout(VerticalLayout wrapper) {
        formLayout = new FormLayout();
        setUpChoices();
        addFormItem(wrapper, formLayout, choices, "What Do You Want to Do?");
    }

}
