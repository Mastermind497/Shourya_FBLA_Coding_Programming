package com.frontend.Documentation;


import com.frontend.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "documentation", layout = MainView.class)
@PageTitle("Documentation | FBLA Genie")
public class Documentation extends VerticalLayout {
    static final String JAVA_DOC_LOCATION = "https://github.com/Mastermind497/Shourya_FBLA/raw/master/JavaDoc.pdf";
    public Documentation() {
        removeAll();
        add(new H1("The Documentation"));

        Html downloader = new Html("<a href=\"" + JAVA_DOC_LOCATION + "\" download>Download JavaDoc</a>");

        add(downloader);

        setAlignItems(Alignment.CENTER);
    }

}
