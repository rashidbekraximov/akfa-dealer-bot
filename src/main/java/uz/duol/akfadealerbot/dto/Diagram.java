package uz.duol.akfadealerbot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Diagram {

    private InvoicePieChartSale invoicePieChartSale;

    private InvoicePieChartClient invoicePieChartClient;

    private List<ProductGroupBarChart> productGroupBarCharts;

    private List<FinanceOperation> financeOperations;

    private List<SalesOperation> saleOperations;

}
