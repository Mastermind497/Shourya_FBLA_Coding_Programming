package com.frontend.Documentation;


import com.frontend.Add.AddHours.AddHours;
import com.frontend.Add.CreateStudent.CreateStudent;
import com.frontend.GetStudentInformation.GetStudentInformation;
import com.frontend.MainView;
import com.frontend.Reports.Reports;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;

@Route(value = "documentation", layout = MainView.class)
@PageTitle("Documentation | FBLA Genie")
@PreserveOnRefresh
@UIScope
public class Documentation extends VerticalLayout {
    static final String JAVA_DOC_LOCATION = "https://github.com/Mastermind497/Shourya_FBLA/raw/master/JavaDoc.pdf";
    static final String TUTORIAL_LOCATION = "";
    public Documentation() {
        removeAll();
        setPadding(true);
        setSpacing(true);
        setMargin(true);
        H1 header = new H1("The Documentation");
        add(header);

        Html javadoc = new Html("<a href=\"" + JAVA_DOC_LOCATION + "\" download>Download JavaDoc</a>");
        Html tutorial = new Html("<a href=\"" + TUTORIAL_LOCATION + "\" download>Download the Tutorial</a>");
        Anchor javadocLive = new Anchor("https://mastermind497.github.io/Shourya_FBLA");
        javadocLive.setText("View the Online JavaDoc");
        javadocLive.setTarget("_blank");
        Anchor vaadin = new Anchor("https://www.vaadin.com/api");
        vaadin.setText("View the Vaadin API");
        vaadin.setTarget("_blank");

        HorizontalLayout downloads = new HorizontalLayout(javadoc, javadocLive, tutorial, vaadin);

        add(downloads);
        setHorizontalComponentAlignment(Alignment.CENTER, header, downloads);

        //FAQs
        Details getStarted = new Details("How do I get started?",
                addDetails(
                        new Text(
                                "Getting started is simple. The first thing necessary to start using this app is having a student " +
                                        "in the database. This requires navigating to the \"Add a Student\" tab and typing in the information. After that, the functionality " +
                                        "of the app can be seen! For a more detailed guide, download the tutorial"
                        ),
                        new RouterLink(
                                "Navigate to \"Add a Student\"", CreateStudent.class
                        )
                )
        );

        Details createAStudent = new Details("How do I create a student?",
                addDetails(
                        new Text(
                                "Creating a Student follows a very straightforward process. Once you have all the necessary information about the student, which includes their " +
                                        "Name, Student ID, Email Address, Grade, Community Service Category Goal, and know the amount of time they have participated in your chapter, the process can begin. " +
                                        "After navigating to the \"Add a Student\" page, all you need to do is plug in the information and click save. If all is successful, you should get a success notification. " +
                                        "Otherwise you will get an error notification, which usually means that something entered is invalid."
                        ),
                        new RouterLink(
                                "Navigate to \"Add a Student\"", CreateStudent.class
                        )
                )
        );

        Details addingHours = new Details("How do I add hours?",
                addDetails(
                        new Text(
                                "Adding Hours is one of the easiest things to do in this Application. Once an event is completed, all you have to do is get the names of all of the " +
                                        "students who participated. After compiling the data, navigate to the \"Add Hours Tab\". " +
                                        "All you need to do after navigating is to select the first student who participated in the event and enter the event details (namely the Name of " +
                                        "the event, the Length of the Event, and the Date of the Event). After that is done, clicking save will add that information to the database and clear the student name field. " +
                                        "This makes it so that you can keep selecting students who did the same event without having to re-enter the event details, as usually one event is done by multiple people."
                        ),
                        new RouterLink(
                                "Navigate to \"Add Hours\"", AddHours.class
                        )
                )
        );

        Details viewingData = new Details("How do I view the stored information?",
                addDetails(
                        new Text(
                                "Viewing the information is quite simple. After information is added to view, all that needs to be done is navigate to \" View and Edit Students\". " +
                                        "Once that is done, the student information is visible first. If you want to see a student's event history, all you need to do is click the expand button in the " +
                                        "expand column and a popup will cover the screen, listing all of the events that said student has participated in. "
                        ),
                        new RouterLink(
                                "Navigate to \"View and Edit Students\"", GetStudentInformation.class
                        )
                )
        );

        Details editData = new Details("How do I edit students and events?",
                addDetails(
                        new Text(
                                "There are two different ways to edit both students and their respective events. If a small change needs to be made, such as a spelling error or grade number, " +
                                        "all you need to do is double click the current value and an editor will take the place of the value instead, allowing you to type in a new piece of information for both. " +
                                        "If the change is larger, or requires changing multiple pieces of data, you can also click the edit button, which opens a popup similar to what was present when initially adding data. " +
                                        "You can replace the old values with the new ones you need and click save, rendering them into the database. Take note that changes can not be undone."
                        ),
                        new RouterLink(
                                "Navigate to \"View and Edit Students\"", GetStudentInformation.class
                        )
                )
        );

        Details useGenReports = new Details("How do I use and generate reports?",
                addDetails(
                        new Text(
                                "This also is simple. To use or generate reports, all you do is navigate to the Generate Reports page. There are two types of reports, individual and " +
                                        "group reports.\n " +

                                        "Individual reports allow you to analyze the progress of an individual student. This includes helpful charts which analyze progress and " +
                                        "consistency. When selecting individual reports, you need to choose a student to analyze and a time frame to analyze them from. You can select a general range, which " +
                                        "includes options such as \"Week\", \"Month\", \"Year\", and \"All Time\", or you can choose to select a specific starting date. After clicking save, your screen will show the individual report.\n " +

                                        "On the other hand, you can also choose to generate a group report. This helps analyze large-scale contributions. It helps differentiate between active and inactive students, " +
                                        "each student's community service hour contribution, and compares them all to demonstrate who did the most. Group reports allow a similar selection of general date range options to " +
                                        "select analysis from, changing the weighting of a good and bad number of hours depending on the selection. It also analyzes the success of each and every student.\n " +

                                        "All reports have versatile and useful dynamic charts, which help make visualizing data much easier and makes their comparison much simple."
                        ),
                        new RouterLink(
                                "Navigate to \"Generate Reports\"", Reports.class
                        )
                )
        );

        add(getStarted, createAStudent, addingHours, viewingData, editData, useGenReports);
    }

    public VerticalLayout addDetails(Text text, Component routerLink) {
        return new VerticalLayout(text, routerLink);
    }

}
