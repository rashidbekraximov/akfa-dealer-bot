package uz.duol.akfadealerbot.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import uz.duol.akfadealerbot.constants.FilePath;
import uz.duol.akfadealerbot.dto.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ChartCreator {

    private static final String CARD_COLOR = "#FAF5FB";

    private final ChartCustomizer chartCustomizer;

    private final Locale locale;

    public ChartCreator(Locale locale) {
        this.chartCustomizer = new ChartCustomizer();  // Create instance of ChartCustomizer
        this.locale = locale;
    }

    public JFreeChart createPieChartOperationCount(InvoicePieChartSale invoicePieChartSale) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(String.format(R.bundle(locale).getString("label.sale.chart"), invoicePieChartSale.getSalesInvoice()), invoicePieChartSale.getSalesInvoice());
        dataset.setValue(String.format(R.bundle(locale).getString("label.returned.chart"), invoicePieChartSale.getReturnedInvoice()), invoicePieChartSale.getReturnedInvoice());
        dataset.setValue(String.format(R.bundle(locale).getString("label.cancel.chart"), invoicePieChartSale.getDeletedInvoice()), invoicePieChartSale.getDeletedInvoice());

        JFreeChart chart = ChartFactory.createPieChart(
                R.bundle(locale).getString("label.sale.operation"), // Chart title
                dataset,              // Dataset
                true,                 // Include legend
                true,                 // Include tooltips
                false                 // Exclude URLs
        );
        chartCustomizer.customizePieChartOperationCount(chart, dataset);
        return chart;
    }

    public JFreeChart createBarChartCategory(List<ProductGroupBarChart> productGroupBarCharts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        productGroupBarCharts.forEach(productGroup ->
                dataset.addValue(productGroup.getQty(), productGroup.getName() + " (" +  formatNumber(productGroup.getQty()) + ")", productGroup.getName() )
        );

        JFreeChart chart = ChartFactory.createBarChart(
                R.bundle(locale).getString("label.purchased.product"), R.bundle(locale).getString("label.products"), R.bundle(locale).getString("label.sale"), dataset, PlotOrientation.VERTICAL, true, true, false
        );
        chartCustomizer.customizeBarChartProductGroup(chart, dataset);
        return chart;
    }

    public JFreeChart createPieChartCurrentCustomers(InvoicePieChartClient invoicePieChartClient) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(String.format(R.bundle(locale).getString("label.permament.client"),invoicePieChartClient.getOldDealerClientCount()), invoicePieChartClient.getOldDealerClientCount());
        dataset.setValue(String.format(R.bundle(locale).getString("label.new.client"),invoicePieChartClient.getNewDealerClientCount()), invoicePieChartClient.getNewDealerClientCount());

        JFreeChart chart = ChartFactory.createPieChart(R.bundle(locale).getString("label.clients"), dataset, true, true, false);
        chartCustomizer.customizePieChartTodaysCustomer(chart, dataset);
        return chart;
    }

    public JFreeChart createLineChartFinanceOperation(List<FinanceOperation> financeOperations) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries revenueSeries = new XYSeries(R.bundle(locale).getString("label.income"));
        XYSeries expenditureSeries = new XYSeries(R.bundle(locale).getString("label.outcome"));

        for (int i = 0; i < financeOperations.size(); i++) {
            FinanceOperation operation = financeOperations.get(i);
            revenueSeries.add(i, operation.getIncome());
            expenditureSeries.add(i, operation.getOutcome());
        }

        dataset.addSeries(revenueSeries);
        dataset.addSeries(expenditureSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                R.bundle(locale).getString("label.hourly.operation"), R.bundle(locale).getString("label.hour"), R.bundle(locale).getString("label.summ"), dataset, PlotOrientation.VERTICAL, true, true, false
        );
        chartCustomizer.customizeLineChartFinanceOperation(chart ,financeOperations);
        return chart;
    }

    //5. Area Chart - Operatsiyalar soni
    public JFreeChart createAreaChartOperationCount(List<SalesOperation> operations) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries saleSeries = new XYSeries(R.bundle(locale).getString("label.sale"));
        XYSeries returnedSeries = new XYSeries(R.bundle(locale).getString("label.returned"));

        for (int i = 0; i < operations.size(); i++) {
            SalesOperation operation = operations.get(i);
            saleSeries.add(i, operation.getSold());
            returnedSeries.add(i, operation.getReturned());
        }
        dataset.addSeries(saleSeries);
        dataset.addSeries(returnedSeries);

        JFreeChart chart = ChartFactory.createXYAreaChart(
                R.bundle(locale).getString("label.hourly.sale"),    // Chart title
                R.bundle(locale).getString("label.hour"),                 // X-axis label
                R.bundle(locale).getString("label.count"),           // Y-axis label
                dataset,                // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true,                   // Legend
                true,                   // Tooltips
                false                   // URLs
        );

        chartCustomizer.customizeAreaChartOperationCount(chart, operations);
        return chart;
    }

    public void drawChartWithCard(Graphics2D g2d, JFreeChart chart, int x, int y, int width, int height, int padding, int margin, int borderThickness) {
        int cardX = x;
        int cardY = y;
        int cardWidth = width + 2 * padding;
        int cardHeight = height + 2 * padding;

        cardX += margin;
        cardY += margin;

        g2d.setColor(Color.decode(CARD_COLOR)); // Light gray
        g2d.fillRoundRect(cardX, cardY, cardWidth, cardHeight, 20, 20);

        g2d.setColor(Color.decode(CARD_COLOR)); // Light gray
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.drawRoundRect(cardX, cardY, cardWidth, cardHeight, 20, 20);

        BufferedImage chartImage = chart.createBufferedImage(width, height);
        g2d.drawImage(chartImage, cardX + padding, cardY + padding, null);
    }

    public Font getFont(String fontStyle, long size, int style) {
        try {
            String fontFilePath = new FilePath().FONT_PATH + "Roboto-" + fontStyle + ".ttf";
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath));

            return customFont.deriveFont(style, size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatNumber(double number) {
        if (number % 1 == 0) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            return numberFormat.format((long) number);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            return decimalFormat.format(number);
        }
    }
}
