package com.frontend;

import com.backend.MySQLMethods;
import com.frontend.Add.AddHours.AddHours;
import com.frontend.Add.CreateStudent.CreateStudent;
import com.frontend.Documentation.Documentation;
import com.frontend.GetStudentInformation.GetStudentInformation;
import com.frontend.Reports.Reports;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;

/* FIXME
 *  Add Title to Each Page
 *  Implement Graduation Year
 *  Add Name Label to All Dropdowns
 *  Calculate Grade level by Grad year
 *  Make Years Done Automatically Calculated
 *  Fix Forget Password
 */

/**
 * MainView is the main view of the entire app. Most pages use MainView as the base to format themselves, keeping everything
 * uniform
 *
 * @author Shourya Bansal
 */
@PWA(name = "FBLA Genie",
        shortName = "FBLA Genie",
        description = "This is the Application made by Shourya Bansal for the FBLA Coding " +
                "& Programming competition in the 2019-2020 school year")
@PreserveOnRefresh
@UIScope
public class MainView extends AppLayout {
    public static final Tab HOME_TAB = createTab(VaadinIcon.HOME, "Home", Home.class);
    public static final Tab ADD_STUDENT_TAB = createTab(VaadinIcon.FILE_ADD, "Add a Student", CreateStudent.class);
    public static final Tab ADD_HOURS_TAB = createTab(VaadinIcon.EDIT, "Add Hours to Student", AddHours.class);
    public static final Tab VIEW_EDIT_TAB = createTab(VaadinIcon.EYE, "View and Edit Students", GetStudentInformation.class);
    public static final Tab REPORT_TAB = createTab(VaadinIcon.RECORDS, "Generate Reports", Reports.class);
    public static final Tab DOC_TAB = createTab(VaadinIcon.QUESTION, "Documentation and FAQs", Documentation.class);
    public static final Tabs tabs = getTabs();

    /**
     * Creates the Main View that formats the rest of the App
     */
    public MainView() {
        final Tabs tabs = new Tabs();

        //Uses Tabs for Navigation
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(getAvailableTabs());
        tabs.setFlexGrowForEnclosedTabs(1);

        Image logo = new Image("https://github.com/Mastermind497/Shourya_FBLA/raw/master/logo/Logo.png", "Logo");
        logo.setHeight("12em");

        Button toGitHubPage = new Button("Go To GitHub Page");
        toGitHubPage.addClickListener(buttonClickEvent -> UI.getCurrent().getPage().executeJs("window.open(\"https://github.com/Mastermind497/Shourya_FBLA\", \"_blank\", \"\");"));

        //A Button for Toggling Dark Mode
        final Button toggleButton = new Button("Dark Mode", VaadinIcon.MOON.create());
        toggleButton.addClickListener(click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                toggleButton.setText("Dark Mode");
                toggleButton.setIcon(VaadinIcon.MOON.create());
            } else {
                themeList.add(Lumo.DARK);
                toggleButton.setText("Light Mode");
                toggleButton.setIcon(VaadinIcon.SUN_O.create());
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(toGitHubPage, toggleButton);
        //Creates a Vertical Layout to store all the above components
        VerticalLayout verticalLayout = new VerticalLayout();

        //Adds component to Vertical Layout
        verticalLayout.setSizeFull();
        verticalLayout.add(logo);
        verticalLayout.add(buttons);
        verticalLayout.add(tabs);

        //Aligns everything to the center
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        //Checks to see if main database is made. Makes if not
        MySQLMethods.setUp();

        addToNavbar(verticalLayout);
        this.setDrawerOpened(false);
    }

    /**
     * Generates the tabs for the Navbar
     *
     * @return The Tabs for the navBar
     */
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

    /**
     * Creates a Link that Logs Out of the App
     *
     * @return An Anchor to Connect to the Log Out Feature
     */
    private static Anchor createLogoutLink() {
        final Anchor a = populateLink(new Anchor(), VaadinIcon.LOCK, "Log Out");
        a.setHref("/logout");
        return a;
    }

    /**
     * Creates a Tab with the given parameters
     * <p>
     * Uses {@link #createTab(Component)} to finish up the process
     *
     * @param icon      The Icon for the Tab
     * @param title     The Title of the Tab
     * @param viewClass The Class that the tab should redirect to
     * @return The Tab with all of the constraints
     */
    private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
        RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setHighlightCondition(HighlightConditions.sameLocation());
        return createTab(populateLink(routerLink, icon, title));
    }

    /**
     * Creates a Tab with the given Components
     *
     * @param content The content inside the tab
     * @return The final Tab
     */
    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(content);
        return tab;
    }

    /**
     * Format the "T" (Usually {@link RouterLink}) to have the icon and title inside
     *
     * @param a     the RouterLink or Component that should have the constraints
     * @param icon  The Icon
     * @param title The Title
     * @param <T>   Usually a RouterLink, something allowing tab redirection
     * @return The "T" with the icon and title connected
     */
    private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
        a.add(icon.create());
        a.add(title);
        return a;
    }
}
