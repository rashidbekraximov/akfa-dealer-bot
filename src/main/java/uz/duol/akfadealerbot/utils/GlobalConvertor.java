package uz.duol.akfadealerbot.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import uz.duol.akfadealerbot.constants.FilePath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GlobalConvertor {

//    private static String cardColor = "#EEF6F8";
    private static String cardColor = "#FFFFFF";


    public static List<InputMedia> convertFilesToInputMedia(List<File> files, String caption) {
        return IntStream.range(0, files.size())
                .mapToObj(i -> {
                    File file = files.get(i);
                    InputMediaPhoto inputMedia = new InputMediaPhoto();
                    inputMedia.setMedia(file, file.getName());
                    inputMedia.setHasSpoiler(true);
                    inputMedia.setParseMode("HTML");
                    if (i == 0) inputMedia.setCaption(caption);
                    return inputMedia;
                })
                .collect(Collectors.toList());
    }

    public static LocalDateTime convertLongToLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String formatLocalDateTime(Long timestamp) {
        if (timestamp == null) return "";
        LocalDateTime date = convertLongToLocalDateTime(timestamp);
        return date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String formatNowDate(String period) {
        LocalDate date = LocalDate.now();
        if (period.equals("yesterday")) date = date.minusDays(1);
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static void main(String[] args) {
        try {
            // Create all the requested charts
            JFreeChart pieChart1 = createPieChartOperatsiyalarSonni();
            JFreeChart barChart2 = createBarChartTovarGruppasi();
            JFreeChart pieChart2 = createPieChartBugungiMijozlar();
            JFreeChart lineChart2 = createLineChartPulKirimChiqim();
            JFreeChart areaChart = createAreaChartOperatsiyalarSonni();

            // Chart dimensions
            int chartWidth = 550;
            int chartHeight = 400;
            int cardPadding = 20;   // Padding inside the card
            int cardMargin = 20;    // Margin between cards
            int cardBorderThickness = 4; // Thickness of the card border

            // Total dimensions accounting for margins and padding
            int cardWidth = chartWidth + 2 * cardPadding;
            int cardHeight = chartHeight + 2 * cardPadding;

            int headlineHeight = 100; // Space for the headline
            int totalWidth = cardWidth * 3 + cardMargin * 2 + 40; // 3 charts in the first row
            int totalHeight = cardHeight * 2 + cardMargin + headlineHeight  + 40;   // 2 charts in the second row

            Color cardBackground = new Color(240, 240, 240); // Light gray
            Color cardBorder = new Color(210, 204, 204);    // Light border

            // Create an image large enough to hold the charts with cards, margins, and padding
            BufferedImage finalImage = new BufferedImage(
                    totalWidth,
                    totalHeight,
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2d = finalImage.createGraphics();


            // Draw card background
            g2d.fillRect(cardMargin, cardMargin, cardWidth - 2 * cardMargin, cardHeight - 2 * cardMargin);

            // Draw card border
            g2d.setColor(cardBorder);
            g2d.setStroke(new BasicStroke(4)); // Border thickness
            g2d.drawRect(cardMargin, cardMargin, cardWidth - 2 * cardMargin, cardHeight - 2 * cardMargin);

            // Set anti-aliasing for smoother rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw headline card
            Color headlineBackground = Color.decode("#FAF5FB"); // Purple background for the headline
            g2d.setColor(headlineBackground);
            g2d.fillRect(0, 0, totalWidth, totalHeight);


            // Add headline text
            String headlineText = GlobalConvertor.formatNowDate("today") + " holat bo'yicha umumiy ko'rsatkichlar";
            g2d.setColor(Color.decode("#2B2A46")); // White text
            g2d.setFont(getFont("Regular", 20L, Font.BOLD));
            int headlineTextX = cardMargin + 610; // Left margin for text
            int headlineTextY = (headlineHeight + g2d.getFontMetrics().getAscent()) / 2; // Center vertically
            g2d.drawString(headlineText, headlineTextX, headlineTextY - 10);

            int top = 70;
            // Draw charts with card effect, padding, and margin
            drawChartWithCard(g2d, pieChart1, 0, top, chartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);
            drawChartWithCard(g2d, barChart2, cardWidth + cardMargin, top, chartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);
            drawChartWithCard(g2d, pieChart2, 2 * (cardWidth + cardMargin), top, chartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);

            int secondRowChartWidth = (totalWidth - 3 * cardMargin) / 2 - 40; // Split total width between 2 charts

            int lineChartX = cardMargin; // Left margin for the first chart
            int lineChartY = cardHeight + cardMargin; // Position in the second row
            drawChartWithCard(g2d, lineChart2, 0, lineChartY + top, secondRowChartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);

            int barChartX = lineChartX + secondRowChartWidth + cardMargin; // Add margin between the two charts
            int barChartY = cardHeight + cardMargin; // Same row as the first chart
            drawChartWithCard(g2d, areaChart, barChartX + 20, barChartY + top, secondRowChartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);

            // Add footer
            String footerText = "duol.uz";
            g2d.setColor(Color.BLACK);
            g2d.setFont(getFont("Light",16L,Font.PLAIN));

            // Footer position
            int footerX = barChartX + 870;
            int footerY = totalHeight - 18; // 40 pixels above the bottom edge
            g2d.drawString(footerText, footerX, footerY);
            // Dispose graphics
            g2d.dispose();

            // Save the final image to a file
            File outputFile = new File("five-charts-layout.png");
            ImageIO.write(finalImage, "png", outputFile);
            System.out.println("Image saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to draw a chart with a card effect, padding, and margin
    private static void drawChartWithCard(Graphics2D g2d, JFreeChart chart, int x, int y, int width, int height, int padding, int margin, int borderThickness) {
        int cardX = x;
        int cardY = y;
        int cardWidth = width + 2 * padding;
        int cardHeight = height + 2 * padding;

        // Apply margin to the card position
        cardX += margin;
        cardY += margin;

        // Draw card background
//        g2d.setColor(new Color(240, 240, 240)); // Light gray
        g2d.setColor(Color.decode(cardColor)); // Light gray
        g2d.fillRoundRect(cardX, cardY, cardWidth, cardHeight, 20, 20);

        // Draw card border
        g2d.setColor(Color.decode(cardColor)); // Light gray
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.drawRoundRect(cardX, cardY, cardWidth, cardHeight, 20, 20);

        // Render the chart within the card
        BufferedImage chartImage = chart.createBufferedImage(width, height);
        g2d.drawImage(chartImage, cardX + padding, cardY + padding, null);
    }


    private static JFreeChart createPieChartOperatsiyalarSonni() {
        // Create dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Sotilgan", 50);
        dataset.setValue("Qaytarilgan", 30);
        dataset.setValue("Bekor qilingan", 20);

        // Create Pie Chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Operatsiyalar soni", // Chart title
                dataset,              // Dataset
                true,                 // Include legend
                true,                 // Include tooltips
                false                 // Exclude URLs
        );

        // Customize the Pie Chart
        customizePieChart(chart);

        return chart; // Return the JFreeChart object for further use
    }


    // Area Chart - Operatsiyalar soni
    private static JFreeChart createAreaChartOperatsiyalarSonni() {
        // Create the dataset
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Create the "Savdo" (Sales) data series
        XYSeries savdoSeries = new XYSeries("Sotilgan");
        savdoSeries.add(0, 50); // Index 0 corresponds to 8:00
        savdoSeries.add(1, 70); // Index 1 corresponds to 9:00
        savdoSeries.add(2, 80); // Index 2 corresponds to 10:00
        savdoSeries.add(3, 90); // Index 3 corresponds to 11:00
        savdoSeries.add(4, 100); // Index 3 corresponds to 11:00
        savdoSeries.add(5, 200); // Index 3 corresponds to 11:00

        // Create the "Vozbrat" (Returns) data series
        XYSeries vozbratSeries = new XYSeries("Qaytarilgan");
        vozbratSeries.add(0, 30); // Index 0 corresponds to 8:00
        vozbratSeries.add(1, 40); // Index 1 corresponds to 9:00
        vozbratSeries.add(2, 50); // Index 2 corresponds to 10:00
        vozbratSeries.add(3, 60); // Index 3 corresponds to 11:00
        vozbratSeries.add(4, 80); // Index 3 corresponds to 11:00
        vozbratSeries.add(5, 100); // Index 3 corresponds to 11:00


        // Add series to the dataset
        dataset.addSeries(savdoSeries);
        dataset.addSeries(vozbratSeries);

        // Create an area chart
        JFreeChart chart = ChartFactory.createXYAreaChart(
                "Soatlik savdo",    // Chart title
                "Soat",                 // X-axis label
                "Operations",           // Y-axis label
                dataset,                // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true,                   // Legend
                true,                   // Tooltips
                false                   // URLs
        );

        // Customize the chart appearance
        customizeAreaChart(chart);

        return chart;
    }

    private static void customizeAreaChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.decode(cardColor));

        // Customize the legend font and color
        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
        }

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);  // Set background color
        plot.setDomainGridlinePaint(Color.gray);  // Set gridline color for X-axis
        plot.setRangeGridlinePaint(Color.gray);  // Set gridline color for Y-axis

        // Customizing the X-axis with hour labels
        String[] hours = {"8:00", "9:00", "10:00", "11:00","12:00", "15:00", "16:00"};
        SymbolAxis domainAxis = new SymbolAxis("Soat", hours);
        domainAxis.setTickUnit(new NumberTickUnit(1));
        domainAxis.setRange(0, hours.length); // Align with the dataset's range
        domainAxis.setLabelFont(getFont("Light",16L,Font.PLAIN));
        domainAxis.setTickLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.setDomainAxis(domainAxis);

        XYAreaRenderer renderer = new XYAreaRenderer(XYAreaRenderer.AREA);

        // Gradient for "Savdo" series
        GradientPaint savdoGradient = new GradientPaint(
                0, 0, new Color(173, 216, 230), // Light blue at the top
                0, 1000, new Color(135, 206, 235) // Slightly darker blue at the bottom
        );
        renderer.setSeriesPaint(0, savdoGradient);

        // Gradient for "Vozbrat" series
        GradientPaint vozbratGradient = new GradientPaint(
                0, 0, new Color(110, 93, 218), // Purple at the top
                0, 1000, new Color(85, 72, 190) // Darker purple at the bottom
        );
        renderer.setSeriesPaint(1, vozbratGradient);

        // Set the customized renderer back to the plot
        plot.setRenderer(renderer);

        // Customize chart title font
        chart.getTitle().setFont(getFont("Regular",20L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46"));
    }

    // 4. Pie Chart - Bugungi mijozlar soni
    private static JFreeChart createPieChartBugungiMijozlar() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Bugungi mijozlar", 100);
        dataset.setValue("Bugun yaratilganlar", 40);

        JFreeChart chart = ChartFactory.createPieChart("Bugungi mijozlar soni", dataset, true, true, false);
        customizePieChartTodaysCustomer(chart);
        return chart;
    }

    // 5. Line Chart - Soat bo'yicha pul kirim chiqimlar
    private static JFreeChart createLineChartPulKirimChiqim() {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries revenueSeries = new XYSeries("Kirim");
        revenueSeries.add(0, 500);
        revenueSeries.add(1, 2500);
        revenueSeries.add(2, 5000);
        revenueSeries.add(3, 3500);
        revenueSeries.add(4, 8700);
        revenueSeries.add(5, 4000);

        XYSeries expenditureSeries = new XYSeries("Chiqim");
        expenditureSeries.add(0, 1500);
        expenditureSeries.add(1, 4700);
        expenditureSeries.add(2, 3800);
        expenditureSeries.add(3, 6000);
        expenditureSeries.add(4, 7300);
        expenditureSeries.add(5, 5900);

        dataset.addSeries(revenueSeries);
        dataset.addSeries(expenditureSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Soatlik moliyaviy operatsiyalar", "Soat", "Summa", dataset, PlotOrientation.VERTICAL, true, true, false
        );
        customizeLineChart(chart);
        return chart;
    }

    // 6. Bar Chart - Sotilgan tovar gruppasi bo'yicha
    private static JFreeChart createBarChartTovarGruppasi() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5000, "Radiator", "Radiator");
        dataset.addValue(7000, "Alukabond", "Alukabond");
        dataset.addValue(3000, "Kotel", "Kotel");
        dataset.addValue(1000, "Alumin & PVH", "Alumin & PVH");
        dataset.addValue(4000, "Granula", "Granula");
        dataset.addValue(8000, "Mebel", "Mebel");

        JFreeChart chart = ChartFactory.createBarChart(
                "Sotilgan tovar guruhlari tahlili", "Tovarlar", "Savdo", dataset, PlotOrientation.VERTICAL, true, true, false
        );
        customizeBarChart(chart);
        return chart;
    }

    // Customize Bar chart (font sizes, color, etc.)
    private static void customizeBarChart(JFreeChart chart) {

        // Change the chart title font and color
        chart.getTitle().setFont(getFont("Regular",18L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(cardColor));

        // Customize the legend font and color
        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
        }

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // More distinct and contrasting color gradients for bars
        renderer.setSeriesPaint(0, new GradientPaint(0, 0, new Color(85, 172, 238), 0, 1, new Color(30, 130, 230))); // Radiator (modern blue)
        renderer.setSeriesPaint(1, new GradientPaint(0, 0, new Color(255, 138, 0), 0, 1, new Color(255, 98, 0))); // Alukabond (strong orange)
        renderer.setSeriesPaint(2, new GradientPaint(0, 0, new Color(46, 204, 113), 0, 1, new Color(39, 174, 96))); // Kotel (vibrant green)
        renderer.setSeriesPaint(3, new GradientPaint(0, 0, new Color(240, 142, 158), 0, 1, new Color(220, 87, 105))); // New color: #F08E9E (soft pink)
        renderer.setSeriesPaint(4, new GradientPaint(0, 0, new Color(109, 215, 211), 0, 1, new Color(56, 176, 169))); // New color: #6DD7D3 (light turquoise)
        renderer.setSeriesPaint(5, new GradientPaint(0, 0, new Color(110, 94, 218), 0, 1, new Color(85, 73, 185))); // New color: #6E5EDA (purple)

        renderer.setMaximumBarWidth(100); // Adjust the width of the bars
        renderer.setItemMargin(-3.5);
        plot.getDomainAxis().setLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.getRangeAxis().setLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.getDomainAxis().setTickLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.getRangeAxis().setTickLabelFont(getFont("Light",14L,Font.PLAIN));
    }

    private static void customizePieChart(JFreeChart chart) {

        // Change the chart title font and color
        chart.getTitle().setFont(getFont("Regular",18L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(cardColor));

        // Customize the legend font and color
        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 15));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
        }

        // Get the PiePlot object
        PiePlot plot = (PiePlot) chart.getPlot();

//        plot.setLabelFont(new Font("Arial", Font.PLAIN,17));
        plot.setLabelFont(getFont("Light",17, Font.PLAIN));
        plot.setLabelLinkPaint(Color.BLACK);

        // Customize the plot background
        plot.setBackgroundPaint(Color.decode(cardColor)); // White plot area
        plot.setOutlineVisible(false); // Remove the plot outline

        // Set custom colors for each category
        plot.setSectionPaint("Sotilgan", Color.decode("#6E5EDA")); // Purple
        plot.setSectionPaint("Qaytarilgan", Color.decode("#73B7DF")); // Light Blue
        plot.setSectionPaint("Bekor qilingan", Color.decode("#F08E9E")); // Light Red
        plot.setLabelBackgroundPaint(Color.decode(cardColor));
        plot.setLabelShadowPaint(Color.decode(cardColor));

        // Explode a specific section (e.g., "Bekor qilingan")
        plot.setExplodePercent("Sotilgan", 0.03); // Explode 20%
        plot.setExplodePercent("Qaytarilgan", 0.03); // Explode 20%
        plot.setExplodePercent("Bekor qilingan", 0.03); // Explode 20%

        plot.setLabelOutlinePaint(null);
    }

    private static void customizePieChartTodaysCustomer(JFreeChart chart) {

        // Change the chart title font and color
        chart.getTitle().setFont(getFont("Regular",18L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(cardColor));

        // Customize the legend font and color
        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
        }

        // Get the PiePlot object
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelFont(new Font("Arial", Font.PLAIN,17));
        plot.setLabelLinkPaint(Color.BLACK);

        // Customize the plot background
        plot.setBackgroundPaint(Color.decode(cardColor)); // White plot area
        plot.setOutlineVisible(false); // Remove the plot outline

        // Set custom colors for each category
        plot.setSectionPaint("Bugungi mijozlar", Color.decode("#5C9EFF")); // Purple
        plot.setSectionPaint("Bugun yaratilganlar", Color.decode("#FFC880")); // Light Blue
        plot.setLabelBackgroundPaint(Color.decode(cardColor));
        plot.setLabelShadowPaint(Color.decode(cardColor));

        // Explode a specific section (e.g., "Bekor qilingan")
        plot.setExplodePercent("Bugungi mijozlar", 0.03); // Explode 20%
        plot.setExplodePercent("Bugun yaratilganlar", 0.03); // Explode 20%

        plot.setLabelOutlinePaint(null);
    }


    // Customize Line chart (font sizes, colors, etc.)
    private static void customizeLineChart(JFreeChart chart) {
        // Change the chart title font and color
        chart.getTitle().setFont(getFont("Regular",18L,Font.BOLD));
        chart.getTitle().setPaint(Color.decode("#2B2A46")); // Purple color for the title
        chart.setBackgroundPaint(Color.decode(cardColor));

        // Customize the legend font and color
        if (chart.getLegend() != null) { // Check if legend exists
            chart.getLegend().setItemFont(getFont("Light",15L,Font.PLAIN));
            chart.getLegend().setItemPaint(Color.decode("#333333")); // Light blue for legend text
        }

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.decode("#FFFFFF"));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        // Customizing the X-axis with hour labels
        String[] hours = {"8:00", "9:00", "10:00", "11:00", "15:00", "16:00"};
        SymbolAxis domainAxis = new SymbolAxis("Soat", hours);
        domainAxis.setTickUnit(new NumberTickUnit(1));
        domainAxis.setRange(0, hours.length); // Align with the dataset's range
        domainAxis.setLabelFont(getFont("Light",16L,Font.PLAIN));
        domainAxis.setTickLabelFont(getFont("Light",14L,Font.PLAIN));
        plot.setDomainAxis(domainAxis);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode("#5681DD")); // Blue for first series
        renderer.setSeriesPaint(1, Color.decode("#FF87A2")); // Red for second series
        // Increase the line width (stroke)
        renderer.setSeriesStroke(0, new BasicStroke(3.5f)); // Increase thickness of the first line (3.0f)
        renderer.setSeriesStroke(1, new BasicStroke(3.5f)); // Increase thickness of the second line (3.0f)

        plot.setRenderer(renderer);
        // Customize chart title font
        chart.getTitle().setFont(getFont("Medium",20L,Font.PLAIN));
        chart.getTitle().setPaint(Color.decode("#2B2A46"));
    }

    private static Font getFont(String fontStyle, long size, int style) {
        try {
            // Build the font file path dynamically based on the provided style
            String fontFilePath = new FilePath().FONT_PATH + "Roboto-" + fontStyle + ".ttf";
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath));

            // Return the derived font with the specified style and size
            return customFont.deriveFont(style, size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }



}
