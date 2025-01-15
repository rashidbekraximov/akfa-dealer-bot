package uz.duol.akfadealerbot.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CombinedBarAndLineChartExample {

    // Create the Bar Chart as XY Dataset
    private JFreeChart createBarChart() {
        // Create an XY Dataset for the bar chart
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Sales");
        series.add(1, 5000);  // Jan
        series.add(2, 4000);  // Feb
        series.add(3, 6000);  // Mar
        series.add(4, 7000);  // Apr
        dataset.addSeries(series);

        // Create a chart with XYPlot
        JFreeChart chart = ChartFactory.createXYBarChart(
                "Sales by Month", // Title
                "Month",          // X-Axis label
                false,            // Not for date/time axis
                "Sales",          // Y-Axis label
                dataset,          // Dataset
                PlotOrientation.VERTICAL,
                false,            // No legend
                true,             // Tooltips enabled
                false             // No URL
        );

        XYPlot plot = chart.getXYPlot();
        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setSeriesPaint(0, Color.BLUE); // Color for the bars
        plot.setRenderer(renderer);
        return chart;
    }

    // Create the Line Chart
    private JFreeChart createLineChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Growth");
        series.add(1, 150);
        series.add(2, 200);
        series.add(3, 250);
        series.add(4, 300);
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Growth Trend",  // Title
                "Month",         // X-Axis label
                "Growth",        // Y-Axis label
                dataset,         // Dataset
                PlotOrientation.VERTICAL,
                false,           // No legend
                true,            // Tooltips enabled
                false            // No URL
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED); // Color for the line
        plot.setRenderer(renderer);
        return chart;
    }

    // Combine both the Bar and Line charts into one chart
    private JFreeChart createCombinedChart() {
        // Create the bar and line charts
        JFreeChart barChart = createBarChart();
        JFreeChart lineChart = createLineChart();

        // Create the XYPlot for the bar chart
        XYPlot barPlot = barChart.getXYPlot();

        // Create the XYPlot for the line chart
        XYPlot linePlot = lineChart.getXYPlot();

        // Combine the plots into a single plot
        CombinedDomainXYPlot combinedPlot = new CombinedDomainXYPlot(new NumberAxis("Month"));
        combinedPlot.add(barPlot, 1);  // Bar chart
        combinedPlot.add(linePlot, 1); // Line chart on top
        combinedPlot.setDomainPannable(true); // Enable scrolling for the x-axis
        combinedPlot.setRangePannable(true);  // Enable scrolling for the y-axis

        // Create the final combined chart
        JFreeChart combinedChart = new JFreeChart("Combined Bar and Line Chart",
                JFreeChart.DEFAULT_TITLE_FONT,
                combinedPlot,
                false);
        return combinedChart;
    }

    // Save the combined chart to an image
    public void saveChartToImage(JFreeChart chart, String outputPath) throws IOException {
        int width = 800;
        int height = 600;
        BufferedImage image = chart.createBufferedImage(width, height);
        File file = new File(outputPath);
        ImageIO.write(image, "PNG", file);
        System.out.println("Chart image saved to: " + file.getAbsolutePath());
    }

    public static void main(String[] args) {
        CombinedBarAndLineChartExample chartExample = new CombinedBarAndLineChartExample();

        try {
            // Create the combined chart
            JFreeChart combinedChart = chartExample.createCombinedChart();

            // Save the chart to an image file
            chartExample.saveChartToImage(combinedChart, "combined_bar_and_line_chart.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
