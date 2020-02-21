package com.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

/**
 * The Page and View for Viewing Overall Student Information
 */
@Route("get-student-info")
public class GetStudentInformationQuestion extends AppLayout {
    public GetStudentInformationQuestion() throws Exception {
        //Creates Navigation Pane
        addToNavbar(Home.makeHeader());

        //Creates First View of the page
        HorizontalLayout question = new HorizontalLayout();

        Button individual = new Button("Individual");
        individual.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        individual.addClickListener(event ->
            UI.getCurrent().navigate(GetStudentInformationIndividual.class)
        );

        Button everyone = new Button("Everyone");
        everyone.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        everyone.addClickListener(event ->
            UI.getCurrent().navigate(GetStudentInformationEveryone.class)
        );

        question.add(individual, everyone);
        setContent(question);
    }
}
