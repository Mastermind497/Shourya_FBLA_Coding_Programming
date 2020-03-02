package com.frontend;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * A Designer generated component for the frontend-design template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("frontend-design")
@JsModule("./frontend-design.js")
public class FrontendDesign extends PolymerTemplate<FrontendDesign.FrontendDesignModel> {

    @Id("vaadinVerticalLayout")
    private VerticalLayout vaadinVerticalLayout;

    /**
     * Creates a new FrontendDesign.
     */
    public FrontendDesign() {
        // You can initialise any data required for the connected UI components here.
    }

    /**
     * This model binds properties between FrontendDesign and frontend-design
     */
    public interface FrontendDesignModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
