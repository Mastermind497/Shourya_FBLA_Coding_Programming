package com.Frontend.Reports.GenerateReport;

import com.Frontend.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "report", layout = MainView.class)
public class GenerateReport extends VerticalLayout {
    public GenerateReport() {
        removeAll();
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new ResponsiveStep("20em", 1),
                new ResponsiveStep("30em", 2),
                new ResponsiveStep("40em", 3));
    }

    /**
     * This method returns a the pre-formatted data of a student. This simplifies the job significantly when
     * writing multiple pieces of data
     *
     * @param header The heading text of the piece of data
     * @param text   The piece of data
     * @return A Div containing the formatted Student Data
     */
    public static Div setText(String header, String text) {
        Div div = new Div();
        div.add(new H6(header + ":"));
        div.add(text);

        return div;
    }
    public static void nextSection(Board dataBoard) {
        //Adding two lines to signify the end of a section
        dataBoard.addRow(new Html("<hr>"));
        dataBoard.addRow(new Html("<hr>"));
    }
}
