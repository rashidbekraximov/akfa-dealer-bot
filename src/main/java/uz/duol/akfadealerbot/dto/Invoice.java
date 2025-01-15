package uz.duol.akfadealerbot.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    private int totalInvoice;

    private Double totalSalesCost;

    private int totalSalesQty;

    private Double totalPurchaseCost;

    private int totalPurchaseQty;

    private Double totalReturnsCost;

    private int totalReturnsQty;

    private byte currency;

    private Double income;

    private Double expense;

    private Double profit;

    private int fullAdjQty;

    private Double fullAdjSum;

    private int partitionAdjQty;

    private Double partitionAdjSum;

    private Long lastSyncDate;

    private String topThreeProducts;

    private List<Segment> dealerSegmentProfitCaptions;


}
