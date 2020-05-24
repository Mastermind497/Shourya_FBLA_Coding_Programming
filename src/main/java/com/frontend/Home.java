package com.frontend;

import com.frontend.Add.CreateStudent.CreateStudent;
import com.frontend.Documentation.Documentation;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;

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
@Route(value = "", layout = MainViewAdmin.class)
@PreserveOnRefresh
@UIScope
public class Home extends VerticalLayout {

    /**
     * Creates the Home Screen
     */
    public Home() {
        removeAll();
        setPadding(true);
        setMargin(true);

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

        Html moreInfo = new Html(
                "<p style='font-size:150%'>To Create This App, a powerful tool called <a href='https://www.vaadin.com/' target='_blank'>Vaadin</a> was used, which " +
                        "allowed the creation of webapps using java. To Access its API, <a href='https://www.vaadin.com/api' target='_blank'>navigate here</a>.</p>"
        );

        add(introInfo, moreInfo);

        //Links to Documentation.java
        RouterLink toDocumentation = new RouterLink("Not sure how to get started? Check Out the Documentation, Tutorials, and Frequently Asked Questions!", Documentation.class);
        toDocumentation.setHighlightCondition(HighlightConditions.always());
        H2 getStarted = new H2(toDocumentation);

        //Links to CreateStudent.java
        RouterLink toCreateStudent = new RouterLink("Or you can get straight to it and start by Adding your First Student!", CreateStudent.class);
        toCreateStudent.setHighlightCondition((HighlightConditions.always()));
        H2 getStraightToIt = new H2(toCreateStudent);

        add(getStarted, getStraightToIt);
    }
}
