package com.frontend.Documentation;


import com.frontend.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@Route(value = "documentation", layout = MainView.class)
@PageTitle("Documentation | FBLA Genie")
@PreserveOnRefresh
public class Documentation extends VerticalLayout {
    static final String JAVA_DOC_LOCATION = "https://github.com/Mastermind497/Shourya_FBLA/raw/master/JavaDoc.pdf";
    static final String TUTORIAL_LOCATION = "";
    public Documentation() {
        removeAll();
        add(new H1("The Documentation"));

        Html javadoc = new Html("<a href=\"" + JAVA_DOC_LOCATION + "\" download>Download JavaDoc</a>");
        Html tutorial = new Html("<a href=\"" + TUTORIAL_LOCATION + "\" download>Download the Tutorial</a>");
        Anchor vaadin = new Anchor("https://www.vaadin.com/api");
        vaadin.setText("View the Vaadin API");
        vaadin.setTarget("_blank");

        HorizontalLayout downloads = new HorizontalLayout(javadoc, tutorial, vaadin);

        add(downloads);

        setAlignItems(Alignment.CENTER);
    }

}
