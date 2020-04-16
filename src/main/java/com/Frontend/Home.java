package com.Frontend;

import com.vaadin.flow.component.html.H1;
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

public class Home extends VerticalLayout {

    //Creates the Home Screen
    public Home() {
        add(new H1("This is the HOME layout. This text is a placeholder for what is to come."));
    }
}
