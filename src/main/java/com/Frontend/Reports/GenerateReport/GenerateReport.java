package com.Frontend.Reports.GenerateReport;

import com.Frontend.MainView;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "report", layout = MainView.class)
public class GenerateReport extends VerticalLayout {
    public GenerateReport() {
        removeAll();
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
    }
}
