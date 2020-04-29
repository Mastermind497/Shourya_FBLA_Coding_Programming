package com.frontend.Documentation;


import com.frontend.MainView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "documentation", layout = MainView.class)
@PageTitle("Documentation | FBLA Genie")
public class Documentation extends VerticalLayout {
    public Documentation() {
        removeAll();
        add(new H1("The Documentation"));
        setAlignItems(Alignment.CENTER);
    }
}
