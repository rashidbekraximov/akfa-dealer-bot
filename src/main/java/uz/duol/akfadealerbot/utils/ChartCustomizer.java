package uz.duol.akfadealerbot.utils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import uz.duol.akfadealerbot.constants.FilePath;
import uz.duol.akfadealerbot.model.dto.FinanceOperation;
import uz.duol.akfadealerbot.model.dto.SalesOperation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartCustomizer {

    private static final String CARD_COLOR = "#FAF5FB";

    private static final String PIE_CHART_SALE_COLOR = "#384D6C";

    private static final String PIE_CHART_RETURNED_COLOR = "#E2892B";

    private static final String PIE_CHART_CANCELED_COLOR = "#869BBA";

    private static final String PERMAMENT_CLIENT_COLOR = "#434C69";

    private static final String NEW_CLIENT_COLOR = "#BF9742";

    private static final String INCOME_LINE_COLOR = "#3EA2F4";

    private static final String OUTCOME_LINE_COLOR = "#E8B88A";

    private static final String SALE_AREA_COLOR = "#3EA2F4";

    private static final String RETURNED_AREA_COLOR = "#0B375C";

    public ChartCustomizer() {}

    private static final List<Paint> colors = new ArrayList<>();

    static {
        colors.add(Color.decode("#0B375C"));
        colors.add(Color.decode("#0D508C"));
        colors.add(Color.decode("#126FBC"));
        colors.add(Color.decode("#1886E8"));
        colors.add(Color.decode("#3EA2F4"));
        colors.add(Color.decode("#73B6F3"));
        colors.add(Color.decode("#A3CEF4"));
    }

    public void customizePieChartOperationCount(JFreeChart chart, DefaultPieDataset dataset) {
        chart.getTitle().setFont(getFont("Regular",18L, Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(CARD_COLOR));

        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 15));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
            chart.getLegend().setBackgroundPaint(Color.decode(CARD_COLOR));
        }

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelFont(getFont("Light",17, Font.PLAIN));
        plot.setLabelLinkPaint(Color.BLACK);

        plot.setBackgroundPaint(Color.decode(CARD_COLOR)); // White plot area
        plot.setOutlineVisible(false); // Remove the plot outline

        plot.setSectionPaint(dataset.getKey(0), Color.decode(PIE_CHART_SALE_COLOR)); // Purple
        plot.setSectionPaint(dataset.getKey(1), Color.decode(PIE_CHART_RETURNED_COLOR)); // Light Blue
        plot.setSectionPaint(dataset.getKey(2), Color.decode(PIE_CHART_CANCELED_COLOR)); // Light Red
        plot.setLabelBackgroundPaint(Color.decode(CARD_COLOR));
        plot.setLabelShadowPaint(Color.decode(CARD_COLOR));

        plot.setExplodePercent(dataset.getKey(0), 0.03); // Explode 20%
        plot.setExplodePercent(dataset.getKey(1), 0.03); // Explode 20%
        plot.setExplodePercent(dataset.getKey(2), 0.03); // Explode 20%

        plot.setLabelOutlinePaint(null);
    }

    public void customizeBarChartProductGroup(JFreeChart chart, DefaultCategoryDataset dataset) {
        // Set title font and color
        chart.getTitle().setFont(getFont("Regular", 18L, Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(CARD_COLOR));

        // Customize legend
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(getFont("Light", 15L, Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
            chart.getLegend().setBackgroundPaint(Color.decode(CARD_COLOR));
        }

        // Customize plot
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        int seriesCount = dataset.getRowCount(); // Number of series in the dataset

        for (int i = 0; i < seriesCount; i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(30); // Adjust the width of the bars
        renderer.setItemMargin(-(double)(0.5 * dataset.getColumnCount()));

        // Customize axis fonts
        plot.getDomainAxis().setLabelFont(getFont("Light", 12L, Font.PLAIN));
        plot.getRangeAxis().setLabelFont(getFont("Light", 12L, Font.PLAIN));
        plot.getDomainAxis().setTickLabelFont(getFont("Light", 12L, Font.PLAIN));
        plot.getRangeAxis().setTickLabelFont(getFont("Light", 12L, Font.PLAIN));
    }


    public void customizePieChartTodaysCustomer(JFreeChart chart,DefaultPieDataset dataset) {
        chart.getTitle().setFont(getFont("Regular",18L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(CARD_COLOR));

        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
            chart.getLegend().setBackgroundPaint(Color.decode(CARD_COLOR));
        }

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelFont(new Font("Arial", Font.PLAIN,17));
        plot.setLabelLinkPaint(Color.BLACK);

        plot.setBackgroundPaint(Color.decode(CARD_COLOR)); // White plot area
        plot.setOutlineVisible(false); // Remove the plot outline

        plot.setSectionPaint(dataset.getKey(0), Color.decode(PERMAMENT_CLIENT_COLOR)); // Purple
        plot.setSectionPaint(dataset.getKey(1), Color.decode(NEW_CLIENT_COLOR)); // Light Blue
        plot.setLabelBackgroundPaint(Color.decode(CARD_COLOR));
        plot.setLabelShadowPaint(Color.decode(CARD_COLOR));

        plot.setExplodePercent(dataset.getKey(0), 0.03); // Explode 20%
        plot.setExplodePercent(dataset.getKey(1), 0.03); // Explode 20%

        plot.setLabelOutlinePaint(null);
    }


    public void customizeLineChartFinanceOperation(JFreeChart chart, List<FinanceOperation> financeOperations) {
        chart.getTitle().setFont(getFont("Regular",18L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(CARD_COLOR));

        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
            chart.getLegend().setBackgroundPaint(Color.decode(CARD_COLOR));
        }

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.decode("#FFFFFF"));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        String[] hours = financeOperations.stream().map(FinanceOperation::getHour).toArray(String[]::new);
        SymbolAxis domainAxis = new SymbolAxis("Soat", hours);
        domainAxis.setTickUnit(new NumberTickUnit(1));
        if (hours.length > 0){
            domainAxis.setRange(0, (double) hours.length - 0.5); // Align with the dataset's range
        }
        domainAxis.setLabelFont(getFont("Light",16L,Font.PLAIN));
        domainAxis.setTickLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.setDomainAxis(domainAxis);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode(INCOME_LINE_COLOR)); // Blue for first series
        renderer.setSeriesPaint(1, Color.decode(OUTCOME_LINE_COLOR)); // Red for second series
        renderer.setSeriesStroke(0, new BasicStroke(3.5f)); // Increase thickness of the first line (3.0f)
        renderer.setSeriesStroke(1, new BasicStroke(3.5f)); // Increase thickness of the second line (3.0f)

        plot.setRenderer(renderer);
        chart.getTitle().setFont(getFont("Medium",20L,Font.PLAIN));
        chart.getTitle().setPaint(Color.decode("#2B2A46"));
    }

    public void customizeAreaChartOperationCount(JFreeChart chart, List<SalesOperation> operations) {
        chart.setBackgroundPaint(Color.decode(CARD_COLOR));
        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
            chart.getLegend().setBackgroundPaint(Color.decode(CARD_COLOR));
        }

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);  // Set background color
        plot.setDomainGridlinePaint(Color.gray);  // Set gridline color for X-axis
        plot.setRangeGridlinePaint(Color.gray);  // Set gridline color for Y-axis

        String[] hours = operations.stream().map(SalesOperation::getHour).toArray(String[]::new);
        SymbolAxis domainAxis = new SymbolAxis("Soat", hours);
        domainAxis.setTickUnit(new NumberTickUnit(1));
        if (hours.length > 0){
            domainAxis.setRange(0, (double) hours.length - 0.5);
        }
        domainAxis.setLabelFont(getFont("Light",16L,Font.PLAIN));
        domainAxis.setTickLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.setDomainAxis(domainAxis);

        XYAreaRenderer renderer = new XYAreaRenderer(XYAreaRenderer.AREA);

//        GradientPaint savdoGradient = new GradientPaint(Color.decode("#FFFFFF"));
//
//        GradientPaint vozbratGradient = new GradientPaint() /);
        renderer.setSeriesPaint(0, Color.decode(SALE_AREA_COLOR));
        renderer.setSeriesPaint(1, Color.decode(RETURNED_AREA_COLOR));

        plot.setRenderer(renderer);

        chart.getTitle().setFont(getFont("Regular",20L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46"));
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
}
