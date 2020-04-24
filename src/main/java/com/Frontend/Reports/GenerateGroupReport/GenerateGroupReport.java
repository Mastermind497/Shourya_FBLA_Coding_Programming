package com.Frontend.Reports.GenerateGroupReport;

import com.Backend.MySQLMethods;
import com.Backend.Percent;
import com.Backend.StudentData;
import com.Frontend.Charts;
import com.Frontend.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import static com.Frontend.Reports.GenerateReport.GenerateReport.nextSection;
import static com.Frontend.Reports.GenerateReport.GenerateReport.setText;

@Route(value = "group-report", layout = MainView.class)
@PageTitle("FBLA Genie | Generate Group Report")
public class GenerateGroupReport extends VerticalLayout {
    Button selector = new Button("Generate Report");

    public GenerateGroupReport() {
        removeAll();
        ComboBox<String> rangeSelector = new ComboBox<>("Range of Data", Charts.WEEK_CHART, Charts.MONTH_CHART, Charts.YEAR_CHART, Charts.ALL_TIME_CHART);
        rangeSelector.setValue(Charts.ALL_TIME_CHART);
        add(rangeSelector, selector);
        selector.addClickListener(onClick -> {
            remove(selector);
            generateReport(rangeSelector.getValue());
        });
        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);
    }

    public static Div makeBulletList(String label, List<StudentData> list) {
        Html header = new Html("<h4 style = \"color:#800517\">" + label + "</h4>");
        StringBuilder html = new StringBuilder("<ul style = \"color:#c11b17\">");
        for (StudentData s : list) {
            html.append("<li>").append(s).append("</li>");
        }
        html.append("</ul>");
        Html bullet = new Html(String.valueOf(html));
        Div div = new Div(header);
        div.add(bullet);
        return div;
    }

    public void generateReport(String option) {
        removeAll();
        Board dataBoard = new Board();
        //This list contains all of the students in the chapter for easy reference for the rest of the report
        List<StudentData> studentDataList = MySQLMethods.getStudentData();
        List<StudentData> studentDataListInRange = MySQLMethods.getStudentData(option);
        List<StudentData> inactiveStudentList = StudentData.removeActive(studentDataListInRange);
        int activeMembers = 0;
        double totalHours = 0;
        for (StudentData s : studentDataListInRange) {
            if (s.getCommunityServiceHours() > 0) {
                activeMembers++;
                totalHours += s.getCommunityServiceHours();
            }
        }

        /* Section 1: General Information
         * This section is mainly for a brief overview. It will give a few numbers
         * and overarching data which is important for most when looking for basic information
         *
         * This section will include, but is not limited to:
         *   Number of Students
         *   Average Student Hours
         *   Average Student Hours Discounting all Zeros
         *   Number of Students without hours
         *   Average Community Service Category goal
         *   Average Achieved Community Service Category
         */
        H1 generalInformation = new H1("General Information");
        dataBoard.addRow(generalInformation);

        H2 chapterDetails = new H2("Chapter Details");
        Div numMembers = setText("Number of Chapter Members", Integer.toString(studentDataListInRange.size()));
        Div numActiveMembers = setText("Number of Active Members", Integer.toString(activeMembers)); //Note: An Active Member is someone who has more than 0 hours
        Div percentActiveMembers = setText("Percent of Members Active", new Percent(activeMembers, studentDataListInRange.size()).toString());
        Div numHours = setText("Total Hours In Chapter", Double.toString(totalHours));

        //Heading for Section
        dataBoard.addRow(chapterDetails);

        //Add A Few Details
        dataBoard.addRow(numMembers, numActiveMembers, percentActiveMembers, numHours);

        nextSection(dataBoard);

        /*
         * A Section for the Specific Member Details, such as Average Hours and such
         */
        H2 memberDetails = new H2("Member Details");
        Div averageHours = setText("Average Hours Per Student", Double.toString(totalHours / studentDataListInRange.size()));
        Div averageCategory = setText("Average Achieved Community Service Category", StudentData.getAverageCategory(studentDataListInRange));
        Div averageGoal = setText("Average Community Service Goal", StudentData.getAverageGoal(studentDataListInRange));

        Div averageActiveHours = setText("Average Hours Per Active Student", "0 (0 Active Students)");
        Div averageActiveCategory = setText("Average Achieved Community Service Category by Active Students", "None (No Active Students)");
        Div averageActiveGoal = setText("Average Community Service Goal of Active Students", "None (No Active Students)");

        if (activeMembers > 0) {
            averageActiveHours = setText("Average Hours Per Active Student", Double.toString(totalHours / activeMembers));
            averageActiveCategory = setText("Average Achieved Community Service Category by Active Students", StudentData.getActiveCategory(studentDataListInRange));
            averageActiveGoal = setText("Average Community Service Goal of Active Students", StudentData.getActiveGoal(studentDataListInRange));
        }

        //Heading for Section
        dataBoard.addRow(memberDetails);

        //A Few general, all-student details
        dataBoard.addRow(averageHours, averageCategory, averageGoal);

        //More Specific Details with only activeStudents
        dataBoard.addRow(averageActiveHours, averageActiveCategory, averageActiveGoal);

        nextSection(dataBoard);

        //This next section is to help visualize the data
        Chart chart = Charts.contributionTreemapChart(option);
        dataBoard.addRow(chart);
        if (inactiveStudentList.size() > 0) {
            Div inactiveStudentsHTML = makeBulletList("Inactive Students", inactiveStudentList);
            dataBoard.addRow(inactiveStudentsHTML);
        }

        /*
         *  Section 2: Community Service Award Analysis
         *
         * This section is for an overview of the Community Service Award Categories and their achievements. This will
         * help in analyzing the goals and achievements of each student, differentiating between the active and inactive students
         *
         * This Section will include, but is not limited to:
         *    A Bar Graph for goals and achievements
         *    A Pie Chart for how many people achieved their goals
         */
        H1 csaAnalysis = new H1("Community Service Award Analysis");
        dataBoard.addRow(csaAnalysis);
        H2 evolutionChart = new H2("Community Service Division Bar/Pie Graph");

        Chart goalChart = Charts.barGraphCommunityServiceCategoryGoals(studentDataList);
        Chart achievedChart = Charts.barGraphCommunityServiceCategoryAchieved(studentDataList);

        dataBoard.addRow(evolutionChart);
        dataBoard.addRow(goalChart, achievedChart);
        dataBoard.addRow(new Html("<hr>"));
        dataBoard.addRow(Charts.goalDivisionChart(studentDataList, "Student Goal Division"),
                Charts.currentDivisionChart(studentDataList, "Student Current Division"));

        nextSection(dataBoard);

        dataBoard.addRow(new H2("Community Service Goal Achievement Analysis"));
        dataBoard.addRow(Charts.achievedGoalPieChart(studentDataList, "Students Who Achieved vs Didn't Achieve Their Goals"));


        add(dataBoard);
    }
}
