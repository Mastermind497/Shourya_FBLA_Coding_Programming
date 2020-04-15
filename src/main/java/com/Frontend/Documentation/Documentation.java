package com.Frontend.Documentation;

import com.Frontend.MainView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "documentation", layout = MainView.class)
@PageTitle("Documentation | FBLA Genie")
public class Documentation extends VerticalLayout {
    public Documentation() {
        add(new H1("This is where all the documentation and FAQs will be available"));
    }
}
