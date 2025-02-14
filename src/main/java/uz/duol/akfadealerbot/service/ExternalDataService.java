package uz.duol.akfadealerbot.service;

import uz.duol.akfadealerbot.model.dto.*;
import uz.duol.akfadealerbot.model.response.ApiResponse;

public interface ExternalDataService {

    ApiResponse<Invoice> fetchCaptionData(Long dealerId, String period);

    ApiResponse<InvoicePieChartSale> fetchPieChartOperations(Long dealerId, String period);

    ApiResponse<InvoicePieChartClient> fetchPieChartClients(Long dealerId, String period);

    ApiResponse<ProductGroupBarChart> fetchProductGroupBarChart(Long dealerId, String period);

    ApiResponse<FinanceOperation> fetchFinanceOperationLineChart(Long dealerId, String period);

    ApiResponse<SalesOperation> fetchSaleOperationLineChart(Long dealerId, String period);

}
