package uz.duol.akfadealerbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;
import uz.duol.akfadealerbot.constants.FilePath;
import uz.duol.akfadealerbot.dto.*;
import uz.duol.akfadealerbot.service.ExternalDataService;
import uz.duol.akfadealerbot.service.ImageProcessService;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.ChartCreator;
import uz.duol.akfadealerbot.utils.GlobalConvertor;
import uz.duol.akfadealerbot.utils.R;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageProcessServiceImpl implements ImageProcessService {

    private final ExternalDataService externalDataService;

    private final TelegramService telegramService;

    @SneakyThrows
    @Override
    public void sendImageGroup(Locale locale, Long dealerId, String name, Long chatId, String period) {

        CompletableFuture<ApiResponse<Invoice>> invoiceFuture =
                CompletableFuture.supplyAsync(() -> externalDataService.fetchCaptionData(dealerId, period));

        CompletableFuture<ApiResponse<InvoicePieChartSale>> pieChartSaleFuture =
                CompletableFuture.supplyAsync(() -> externalDataService.fetchPieChartOperations(dealerId, period));

        CompletableFuture<ApiResponse<InvoicePieChartClient>> pieChartClientFuture =
                CompletableFuture.supplyAsync(() -> externalDataService.fetchPieChartClients(dealerId, period));

        CompletableFuture<ApiResponse<ProductGroupBarChart>> productGroupBarChartFuture =
                CompletableFuture.supplyAsync(() -> externalDataService.fetchProductGroupBarChart(dealerId, period));

        CompletableFuture<ApiResponse<FinanceOperation>> financeOperationFuture =
                CompletableFuture.supplyAsync(() -> externalDataService.fetchFinanceOperationLineChart(dealerId, period));

        CompletableFuture<ApiResponse<SalesOperation>> saleOperationFuture =
                CompletableFuture.supplyAsync(() -> externalDataService.fetchSaleOperationLineChart(dealerId, period));

        ApiResponse<Invoice> invoiceApiResponse = invoiceFuture.get();
        ApiResponse<InvoicePieChartSale> invoicePieChartSale = pieChartSaleFuture.get();
        ApiResponse<InvoicePieChartClient> invoicePieChartClient = pieChartClientFuture.get();
        ApiResponse<ProductGroupBarChart> productGroupBarChart = productGroupBarChartFuture.get();
        ApiResponse<FinanceOperation> financeOperation = financeOperationFuture.get();
        ApiResponse<SalesOperation> saleOperation = saleOperationFuture.get();

        if (invoiceApiResponse == null || invoiceApiResponse.getBody() == null || invoiceApiResponse.getBody().isEmpty()){
            log.info("Diller hali sinxronizatsiya qilmagan: {}: Id {}", name, dealerId);
            telegramService.sendMessage(new MessageSend(chatId, String.format(R.bundle(locale).getString("label.not.sync"), name)));
            return;
        }

        Invoice invoice = invoiceApiResponse.getBody().get(0);

        try {
            File image = writeImage(locale,
                    Diagram.builder().
                            invoicePieChartSale(invoicePieChartSale.getBody().get(0)).
                            invoicePieChartClient(invoicePieChartClient.getBody().get(0)).
                            productGroupBarCharts(productGroupBarChart.getBody()).
                            financeOperations(financeOperation.getBody()).
                            saleOperations(saleOperation.getBody())
                            .build());

            if (image.exists()) {
                telegramService.sendPhoto(new PhotoSend(chatId, buildInvoiceReport(locale, invoice, name, period),image,null));
                log.info("Rasm yuborildi: Chat ID: {}", chatId);
            }
        } catch (Exception e) {
            log.error("Xato yuz berdi rasm yuborishda, Chat ID: {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public File writeImage(Locale locale, Diagram diagram) {
        ChartCreator chartCreator = new ChartCreator(locale);

        File outputFile = null;

        try {
            JFreeChart pieChartOperationCount = chartCreator.createPieChartOperationCount(diagram.getInvoicePieChartSale());
            JFreeChart barChartCategory = chartCreator.createBarChartCategory(diagram.getProductGroupBarCharts());
            JFreeChart pieChartCurrentCustomer = chartCreator.createPieChartCurrentCustomers(diagram.getInvoicePieChartClient());
            JFreeChart lineChartFinanceOperation = chartCreator.createLineChartFinanceOperation(diagram.getFinanceOperations());
            JFreeChart areaChartOperationCount = chartCreator.createAreaChartOperationCount(diagram.getSaleOperations());

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
            Color headlineBackground = Color.decode("#F0F0F0"); // Purple background for the headline
            g2d.setColor(headlineBackground);
            g2d.fillRect(0, 0, totalWidth, totalHeight);

            // Add headline text
            String headlineText = String.format(R.bundle(locale).getString("label.header"), GlobalConvertor.formatNowDate("today"));
            g2d.setColor(Color.decode("#2B2A46")); // White text
            g2d.setFont(chartCreator.getFont("Regular", 20L, Font.BOLD));
            int headlineTextX = cardMargin + 610; // Left margin for text
            int headlineTextY = (headlineHeight + g2d.getFontMetrics().getAscent()) / 2; // Center vertically
            g2d.drawString(headlineText, headlineTextX, headlineTextY - 10);

            int top = 70;
            // Draw charts with card effect, padding, and margin
            chartCreator.drawChartWithCard(g2d, pieChartOperationCount, 0, top, chartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);
            chartCreator.drawChartWithCard(g2d, barChartCategory, cardWidth + cardMargin, top, chartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);
            chartCreator.drawChartWithCard(g2d, pieChartCurrentCustomer, 2 * (cardWidth + cardMargin), top, chartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);

            int secondRowChartWidth = (totalWidth - 3 * cardMargin) / 2 - 40; // Split total width between 2 charts

            int lineChartX = cardMargin; // Left margin for the first chart
            int lineChartY = cardHeight + cardMargin; // Position in the second row
            chartCreator.drawChartWithCard(g2d, lineChartFinanceOperation, 0, lineChartY + top, secondRowChartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);

            int barChartX = lineChartX + secondRowChartWidth + cardMargin; // Add margin between the two charts
            int barChartY = cardHeight + cardMargin; // Same row as the first chart
            chartCreator.drawChartWithCard(g2d, areaChartOperationCount, barChartX + 20, barChartY + top, secondRowChartWidth, chartHeight, cardPadding, cardMargin, cardBorderThickness);

            // Add footer
            String footerText = "duol.uz";
            g2d.setColor(Color.BLACK);
            g2d.setFont(chartCreator.getFont("Light",16L,Font.PLAIN));

            // Footer position
            int footerX = barChartX + 870;
            int footerY = totalHeight - 18;
            g2d.drawString(footerText, footerX, footerY);
            g2d.dispose();

            outputFile = new File(new FilePath().IMAGE_PATH +  "dashboard.png");
            ImageIO.write(finalImage, "png", outputFile);

            log.info("Rasm muvaffaqiyatli ishlov berildi va saqlandi: {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Rasmga ishlov berishda xatolik yuz berdi: {} ", e.getMessage());
        }
        return outputFile;
    }


    public String buildInvoiceReport(Locale locale, Invoice invoice,String name,String period) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null.");
        }
        StringBuilder captionHeader = new StringBuilder();
        captionHeader.append("\uD83D\uDCCA ").append(R.bundle(locale).getString("label.sale.report")).append(" \n\n");
        captionHeader.append("\uD83C\uDFE2 ").append(R.bundle(locale).getString("label.dealer")).append(":  ").append(name).append(" \n\n");

        String [] products = invoice.getTopThreeProducts().split("&!");
        String [] emoji = {"\uD83E\uDD47","\uD83E\uDD48","\uD83E\uDD49","\uD83E\uDD50"};

        StringBuilder topProducts = new StringBuilder();
        for (int i = 0; i < products.length; i++) {
            topProducts.append("     ").append(emoji[i]).append(" ").append(products[i]).append("\n");
        }
        StringBuilder topSegments = new StringBuilder("");
        String profitCaption = invoice.getDealerSegmentProfitCaptions().size() > 0 ? (R.bundle(locale).getString("label.profit") + """
                \s
                %s\t
                """) :"%s";

        for (int i = 0; i < invoice.getDealerSegmentProfitCaptions().size(); i++) {
            Segment segment = invoice.getDealerSegmentProfitCaptions().get(i);
            topSegments.append(String.format("     \uD83D\uDD38 %s: %.2f$%n",
                    segment.getSegmentName(),
                    segment.getDsProfitAmount()));
        }

        String body = String.format(
                "\uD83D\uDD39 " + R.bundle(locale).getString("label.header") +
                        "\n\n" +
                        "\uD83D\uDD39 " + R.bundle(locale).getString("label.total.contract") + "  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.trade.deals") + "  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.redemptions") + "  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.crimean.deals") + "  \n" +
                        "\t\n" +
                        "\uD83D\uDD39 " + R.bundle(locale).getString("label.adjustment") + "  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.adjustment.full") + "  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.adjustment.partition") + "  \n" +
                        "\t\n" +
                        "\uD83D\uDD39 " + R.bundle(locale).getString("label.kassa") + ":  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.income") + ": %.2f$  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.outcome") + ": %.2f$  \n" +
                        "     \uD83D\uDD38 " + R.bundle(locale).getString("label.remainder") + ": %.2f$  \n" +
                        "\t\n" +
                        profitCaption +
                        "\uD83D\uDD39 " + R.bundle(locale).getString("label.top.products") + "\n" + "%s" + "\n" +
                        "\uD83D\uDCC5 " + R.bundle(locale).getString("label.last.sync.date"),
                GlobalConvertor.formatNowDate(period),
                invoice.getTotalInvoice(),
                invoice.getTotalSalesCost() == null ? 0.0 : invoice.getTotalSalesCost(),
                invoice.getTotalSalesQty(),
                invoice.getTotalReturnsCost() == null ? 0.0 : invoice.getTotalReturnsCost(),
                invoice.getTotalReturnsQty(),
                invoice.getTotalPurchaseCost() == null ? 0.0 : invoice.getTotalPurchaseCost(),
                invoice.getTotalPurchaseQty(),
                invoice.getFullAdjSum() == null ? 0.0 : invoice.getFullAdjSum(),
                invoice.getFullAdjQty(),
                invoice.getPartitionAdjSum() == null ? 0.0 : invoice.getPartitionAdjSum(),
                invoice.getPartitionAdjQty(),
                invoice.getIncome() == null ? 0.0 : invoice.getIncome(),
                invoice.getExpense() == null ? 0.0 : invoice.getExpense(),
                invoice.getProfit() == null ? 0.0 : invoice.getProfit(),
                topSegments,
                topProducts,
                GlobalConvertor.formatLocalDateTime(invoice.getLastSyncDate())
        );

        return "<strong>" + captionHeader + body + "</strong>";
    }
}
