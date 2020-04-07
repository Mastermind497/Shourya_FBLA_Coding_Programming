package com.Frontend.Documentation;

import com.Frontend.Home;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("documentation")
@PageTitle("Documentation | FBLA Genie")
public class Documentation extends AppLayout {
    public Documentation() {
        addToNavbar(Home.makeHeader(Home.DOC_TAB));
    }
}
