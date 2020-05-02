package com.frontend.Login;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.Collections;

/**
 * A Class that allows logging in to the system. Huge potential for security, currently not using
 * its full capabilities for demonstration purposes.
 */
@Route(value = "login")
@PageTitle("Login | FBLA Genie")
@PreserveOnRefresh
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    /**
     * The main login form
     */
    LoginForm login = new LoginForm();

    /**
     * Sets up the Login View and adds it to the its page
     */
    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        login.setAction("login");

        Image logo = new Image("https://github.com/Mastermind497/Shourya_FBLA/raw/master/logo/Logo.png", "Logo");
        logo.setHeight("24em");

        add(
                logo,
                login
        );
    }

    /**
     * Turns on security, preventing access to the rest of the app before login is complete
     *
     * @param beforeEnterEvent A lambda event that is used by the login view to validate if the app should
     *                         be open or remain closed
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //Checks whether there is a parameter error
        if (!beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .getOrDefault("error", Collections.emptyList())
                .isEmpty()) {

            //makes error visible
            login.setError(true);
        }
    }
}
