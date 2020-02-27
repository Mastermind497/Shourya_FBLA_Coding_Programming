package com.frontend.Get;

import com.frontend.Home;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.Route;

@Route("get-student-events")
public class GetStudentEvents extends AppLayout {
    public GetStudentEvents() {
        addToNavbar(Home.makeHeader());


    }
}
