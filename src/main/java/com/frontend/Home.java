package com.frontend;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/* FIXME
    * Add Title to Each Page
    * Implement Graduation Year
    * Make Community Service Category a Radio Button
    * Make "RESET" Red
    * Add Name Label to All Dropdowns
    * Calculate Grade level by Grad year
    * Edit Student Event
    * Make Years Done Automatically Calculated
    * Edit Buttons Automatically Reset
    * Fix Forget Password
 */

/**
 * Home is the home view for the App.
 * This view has a brief introduction to the project and allows navigation to every
 * other part of the app.
 *
 * @author Shourya Bansal
 */
@Route(value = "", layout = MainView.class)

public class Home extends VerticalLayout {

    //Creates the Home Screen
    public Home() {
        H1 header = new H1("Welcome to FBLA Genie");
        HorizontalLayout headerLayout = new HorizontalLayout(header);
        headerLayout.setAlignItems(Alignment.CENTER);
        add(headerLayout);

        add(new H5("This application is meant to make your life as a member of an FBLA Chapter much easier. This should " +
                "help in organization, hour management, and Community Service Award Management"));
    }
}
