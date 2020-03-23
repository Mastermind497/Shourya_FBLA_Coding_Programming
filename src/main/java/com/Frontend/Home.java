package com.Frontend;

import com.Backend.MySQLMethods;
import com.Frontend.Add.AddHours;
import com.Frontend.Add.CreateStudent;
import com.Frontend.Edit.EditStudentEvents;
import com.Frontend.Edit.EditStudentInformation;
import com.Frontend.Get.GetStudentInformation;
import com.Frontend.Reports.GenerateIndividualReport;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
@Route("")
@PWA(name = "FBLA Data Tracker",
        shortName = "Data Tracker",
        description = "This is the Application made by Shourya Bansal for the FBLA Coding " +
                "& Programming competition in the 2019-2020 school year")
public class Home extends AppLayout {
    //Creates the Home Screen
    public Home() {
        addToNavbar(makeHeader());
    }

    /**
     * This method is used to add a header to the GUI on any page of the App.
     * This speeds up the process significantly instead of having to recreate and
     * retype the code each and every time. This also cleans code up.
     */
    public static VerticalLayout makeHeader() {
        //If there is internet, sets main image to the FBLA-PBL logo
        Image logo = new Image("https://www.fbla-pbl.org/media/FBLA-PBL_registered.png", "FBLA-PBL Logo");
        logo.setHeight("60px");

        //Sets up a MenuBar for Navigation
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true); //Opens options on hover

        //Starts Setting up MenuBar Options
        MenuItem home = menuBar.addItem("Home", event ->
                UI.getCurrent().navigate(Home.class)
        );
        MenuItem addData = menuBar.addItem("Add Data");
        MenuItem editData = menuBar.addItem("Edit Data");
        MenuItem viewData = menuBar.addItem("View Data", event ->
                UI.getCurrent().navigate(GetStudentInformation.class));
        MenuItem genReport = menuBar.addItem("Reports");
        MenuItem documentation = menuBar.addItem("Documentation");

        //Creates Submenus for the MenuBar Options to make it clean and easy to read
        SubMenu addDataSubMenu = addData.getSubMenu();
        addDataSubMenu.addItem("Add a Student", event ->
                UI.getCurrent().navigate(CreateStudent.class)
        );
        addDataSubMenu.addItem("Add Student Hours", event ->
                UI.getCurrent().navigate(AddHours.class)
        );

        SubMenu editDataSubMenu = editData.getSubMenu();
        editDataSubMenu.addItem("Edit Student Information", event ->{
            EditStudentInformation.selected = null;
            UI.getCurrent().navigate(EditStudentInformation.class);
        });
        editDataSubMenu.addItem("Edit Student Event Information", event -> {
            EditStudentEvents.selected = null;
            EditStudentEvents.oldEvent = null;
            UI.getCurrent().navigate(EditStudentEvents.class);
        });

        SubMenu genRepSubMenu = genReport.getSubMenu();
        genRepSubMenu.addItem("Generate Individual Report", e ->
                UI.getCurrent().navigate(GenerateIndividualReport.class));
        genRepSubMenu.addItem("Generate Overall Report");


        Anchor logout = new Anchor("/logout", "Log Out");

        HorizontalLayout header = new HorizontalLayout(menuBar, logout);
        header.expand(menuBar);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.addClassName("header");

        //Creates a Vertical Layout to store all the above components
        VerticalLayout verticalLayout = new VerticalLayout();

        //Adds component to Vertical Layout
        verticalLayout.setSizeFull();
        verticalLayout.add(logo, header);

        //Aligns everything to the center
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        //Checks to see if main database is made. Makes if not
        MySQLMethods.setUp();

        return verticalLayout;
    }
}
