package com.frontend;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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
 *
 * This view has a brief introduction to the project and allows navigation to every
 * other part of the app.
 *
 * @author Shourya Bansal
 */
@Route(value = "", layout = MainView.class)
public class Home extends VerticalLayout {

    /**
     * Creates the Home Screen
     */
    public Home() {
        H1 header = new H1("Welcome to FBLA Genie!");

        Image logo = new Image("https://github.com/Mastermind497/Shourya_FBLA/raw/master/logo/Logo.png", "Logo");
        logo.setHeight("30em");

        add(header);
        add(logo);
        setHorizontalComponentAlignment(Alignment.CENTER, header, logo);

        Html introInfo = new Html(
                "<p style='font-size:200%'>FBLA Genie is an Application designed for the FBLA Coding and Programming Competitive Event. " +
                        "It stores student information for everyone in an FBLA Chapter and makes organization of Hours, Community Service Award Category, " +
                        "Activity, as well as Analysis of all data, Easier!</p>"
        );

        add(introInfo);

        Html getStarted = new Html("<h2>Want to Get Started? <a href='documentation'>Check Out the Documentation, Tutorials, and Frequently Asked Questions!</a></h2>");
        add(getStarted);
    }
}
