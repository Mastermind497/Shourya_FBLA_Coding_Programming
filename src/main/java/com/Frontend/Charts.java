package com.Frontend;

import com.Backend.Percent;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;

public class Charts {
    public static Chart solidGauge(double current, double max, int colorIndex) {
        Percent percent = new Percent(current, max);
        Chart chart = new Chart(ChartType.SOLIDGAUGE);

        Configuration configuration = chart.getConfiguration();
        Pane pane = configuration.getPane();
        pane.setCenter(new String[]{"50%", "50%"});

        //Change depending on how the gauge should be (more open or closed)
        pane.setStartAngle(-90);
        pane.setEndAngle(90);

        //Creates an arced background for the Gauge
        Background paneBackground = new Background();
        paneBackground.setInnerRadius("60%");
        paneBackground.setOuterRadius("100%");
        paneBackground.setShape(BackgroundShape.ARC);
        pane.setBackground(paneBackground);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTickAmount(2);
        yAxis.setTitle("Percent Completion");
        //No small ticks
        yAxis.setMinorTickInterval("null");
        yAxis.getTitle().setY(-50);
        yAxis.getLabels().setY(16);
        yAxis.setExtremes(0, 100, true);
        yAxis.setMaxPadding(0);


        PlotOptionsSolidgauge plotOptionsSolidgauge = new PlotOptionsSolidgauge();

        DataLabels dataLabels = plotOptionsSolidgauge.getDataLabels();
        dataLabels.setY(5);
        dataLabels.setUseHTML(true);

        configuration.setPlotOptions(plotOptionsSolidgauge);

        DataSeries series = new DataSeries("Percent");

        DataSeriesItem item = new DataSeriesItem();
        item.setY(percent.getPercent());
        item.setColorIndex(colorIndex);
        item.setClassName("Percent");
        DataLabels dataLabelsSeries = new DataLabels();
        dataLabelsSeries.setFormat("<div style=\"text-align:center\"><span style=\"font-size:25px;"
                + "color:black' + '\">{y}</span><br/>"
                + "<span style=\"font-size:12px;color:silver\">" + "Percent" + "</span></div>");

        item.setDataLabels(dataLabelsSeries);

        series.add(item);

        configuration.addSeries(series);

        return chart;
    }

//    public static Chart lineGraph() {
//
//    }
}
