package com.frontend;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * A Designer generated component for the main-design template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("main-design")
@JsModule("./main-design.js")
public class MainDesign extends PolymerTemplate<MainDesign.MainDesignModel> {

    /**
     * Creates a new MainDesign.
     */
    public MainDesign() {
        // You can initialise any data required for the connected UI components here.
    }

    /**
     * This model binds properties between MainDesign and main-design
     */
    public interface MainDesignModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
