package com.Frontend.Reports;

import com.Frontend.Home;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.*;
import com.vaadin.flow.router.Route;

@Route("report")
public class GenerateReport extends AppLayout {
    public GenerateReport() {
        addToNavbar(Home.getTabs());

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
    }
}
