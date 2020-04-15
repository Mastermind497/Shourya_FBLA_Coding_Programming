package com.Frontend;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/* FIXME
    * Add Title to Each Page
    * Implement Graduation Year
    * Make Community Service Category a Radio Button
    * Make "RESET" Red
    * Add Name Label to All Dropdowns
    * Calculate Grade level by Grad year
    * Edit Student Event
    * Add Delete Button within Edit
    * Make Years Done Automatically Calculated
    * Edit Buttons Automatically Reset
    * View All + View Individual
    * Fix Forget Password
    * Add Student Hours swaps month and days
 */

/**
 * Home is the home view for the App.
 * This view has a brief introduction to the project and allows navigation to every
 * other part of the app.
 *
 * @author Shourya Bansal
 */
@Route(value = "", layout = MainView.class)
@PWA(name = "FBLA Genie",
        shortName = "FBLA Genie",
        description = "This is the Application made by Shourya Bansal for the FBLA Coding " +
                "& Programming competition in the 2019-2020 school year")
public class Home extends VerticalLayout {

    //Creates the Home Screen
    public Home() {

    }
}
