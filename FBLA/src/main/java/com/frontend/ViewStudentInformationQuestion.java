package com.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

/**
 * The Page and View for Viewing Overall Student Information
 */
@Route("view-student-info")
public class ViewStudentInformationQuestion extends AppLayout {
    //Creates a boolean to help in redirection in the future
    public static boolean viewData = false;
    public ViewStudentInformationQuestion() throws Exception {
        //Creates Navigation Pane
        addToNavbar(Home.makeHeader());

        //Creates First View of the page
        HorizontalLayout question = new HorizontalLayout();
        Button viewData = new Button("View Data");
            viewData.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
            viewData.addClickListener(event -> {
                makeTrue();
                secondQuestion();
            });
        Button exportData = new Button("Export Data");
            exportData.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
            viewData.addClickListener(event -> {
                makeFalse();
                secondQuestion();
            });

        question.add(viewData, exportData);
        question.setAlignItems(FlexComponent.Alignment.CENTER);
        question.setAlignSelf(FlexComponent.Alignment.CENTER);
        setContent(question);
    }

    //These methods bypass lambda limitations of only changing static variables
    public void makeTrue() {
        viewData = true;
    }

    public void makeFalse() {
        viewData = false;
    }

    /**
     * Sets up the second question to figure out whether only an individual's data
     * or all data should be portrayed
     */
    public void secondQuestion() {
        //Horizontal Layout for 2 Buttons for the 2nd Question
        HorizontalLayout question2 = new HorizontalLayout();
        Button individual = new Button("Individual");
            individual.addClickListener(event ->
                UI.getCurrent().navigate(ViewStudentInformationIndividual.class)
            );
        Button everyone = new Button("Everyone");
    }
}
