package com.Frontend;

import com.Backend.MySQLMethods;
import com.Frontend.Add.AddHours.AddHours;
import com.Frontend.Add.CreateStudent.CreateStudent;
import com.Frontend.Documentation.Documentation;
import com.Frontend.Get.GetStudentInformation.GetStudentInformation;
import com.Frontend.Reports.GenerateReport.GenerateReport;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

import java.util.ArrayList;

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
@PWA(name = "FBLA Genie",
        shortName = "FBLA Genie",
        description = "This is the Application made by Shourya Bansal for the FBLA Coding " +
                "& Programming competition in the 2019-2020 school year")
public class MainView extends AppLayout {
    public static final Tab HOME_TAB = createTab(VaadinIcon.HOME, "Home", "", Home.class);
    public static final Tab ADD_STUDENT_TAB = createTab(VaadinIcon.FILE_ADD, "Add a Student", "", CreateStudent.class);
    public static final Tab ADD_HOURS_TAB = createTab(VaadinIcon.EDIT, "Add Hours", "to Student", AddHours.class);
    public static final Tab VIEW_EDIT_TAB = createTab(VaadinIcon.EYE, "View and Edit", "Students", GetStudentInformation.class);
    public static final Tab REPORT_TAB = createTab(VaadinIcon.RECORDS, "Generate Reports", "", GenerateReport.class);
    public static final Tab DOC_TAB = createTab(VaadinIcon.QUESTION, "Documentation", "and FAQs", Documentation.class);
    public static final Tabs tabs = getTabs();

    //Creates the Home Screen
    //TODO get active tab feature working
    public MainView() {
        final Tabs tabs = new Tabs();

        //Uses Tabs for Navigation
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(getAvailableTabs());
        tabs.setFlexGrowForEnclosedTabs(1);

        H2 appName = new H2("FLBA Genie");

        //Creates a Vertical Layout to store all the above components
        VerticalLayout verticalLayout = new VerticalLayout();

        //Adds component to Vertical Layout
        verticalLayout.setSizeFull();
        verticalLayout.add(appName, tabs);

        //Aligns everything to the center
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        //Checks to see if main database is made. Makes if not
        MySQLMethods.setUp();

        addToNavbar(verticalLayout);
        this.setDrawerOpened(false);
    }

    public static Tabs getTabs() {
        return new Tabs(getAvailableTabs());
    }

    /**
     * Creates an ArrayList of the Tabs for Each page
     *
     * @return The Tabs
     */
    private static Tab[] getAvailableTabs() {
        final ArrayList<Tab> tabs = new ArrayList<>(7);
        tabs.add(HOME_TAB);
        tabs.add(ADD_STUDENT_TAB);
        tabs.add(ADD_HOURS_TAB);
        tabs.add(VIEW_EDIT_TAB);
        tabs.add(REPORT_TAB);
        tabs.add(DOC_TAB);
        tabs.add(createTab(createLogoutLink()));

        return tabs.toArray(new Tab[0]);
    }

    private static Anchor createLogoutLink() {
        final Anchor a = populateLink(new Anchor(), VaadinIcon.LOCK, "Log Out", "");
        a.setHref("/logout");
        return a;
    }

    private static Tab createTab(VaadinIcon icon, String title1, String title2, Class<? extends Component> viewClass) {
        RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setHighlightCondition(HighlightConditions.sameLocation());
        return createTab(populateLink(routerLink, icon, title1, title2));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title1, String title2) {
        a.add(icon.create());
        a.add(title1);
        a.add("\n");
        a.add(title2);
        return a;
    }
}
