package uz.duol.akfadealerbot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.duol.akfadealerbot.model.dto.*;
import uz.duol.akfadealerbot.model.response.ApiResponse;
import uz.duol.akfadealerbot.service.ExternalDataService;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalDataServiceImpl implements ExternalDataService {

    @Value("${akfa.report.username}")
    private String username;

    @Value("${akfa.report.password}")
    private String password;

    @Value("${akfa.report.url}")
    private String url;

    private final RestTemplate restTemplate;

    @Override
    public ApiResponse<Invoice> fetchCaptionData(Long dealerId, String period) {
        String auth = username + ":" + password;

        /* Java 8 Base64 kodlash */
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String formattedUrl = String.format(url + "report?dealer_id=%s&type=%s",dealerId,period);
        ResponseEntity<ApiResponse<Invoice>> response = restTemplate.exchange(formattedUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        handleResponseStatus(response, "Caption ma'lumotlari ");
        return response.getBody();
    }


    @Override
    public ApiResponse<InvoicePieChartSale> fetchPieChartOperations(Long dealerId, String period) {
        String base_url = url + "pie-chart/invoices?type=%s&dealerId=%s";
        String auth = username + ":" + password;

        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String formattedUrl = String.format(base_url,period,dealerId);

        ResponseEntity<ApiResponse<InvoicePieChartSale>> response = restTemplate.exchange(formattedUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        handleResponseStatus(response, "Savdo operatsiyalari");
        return response.getBody();
    }

    @Override
    public ApiResponse<InvoicePieChartClient> fetchPieChartClients(Long dealerId, String period) {
        String base_url = url + "pie-chart/dealer-clients?type=%s&dealerId=%s";
        String auth = username + ":" + password;

        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String formattedUrl = String.format(base_url,period,dealerId);
        ResponseEntity<ApiResponse<InvoicePieChartClient>> response = restTemplate.exchange(formattedUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        handleResponseStatus(response, "Mijozlar");
        return response.getBody();
    }

    @Override
    public ApiResponse<ProductGroupBarChart> fetchProductGroupBarChart(Long dealerId, String period) {
        String base_url = url + "bar-chart/product-groups?type=%s&dealerId=%s";
        String auth = username + ":" + password;

        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String formattedUrl = String.format(base_url,period,dealerId);

        ResponseEntity<ApiResponse<ProductGroupBarChart>> response = restTemplate.exchange(formattedUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        handleResponseStatus(response, "Tovar guruhlari");
        return response.getBody();
    }

    @Override
    public ApiResponse<FinanceOperation> fetchFinanceOperationLineChart(Long dealerId, String period) {
        String base_url = url + "line-chart/payment-receipt?type=%s&dealerId=%s";
        String auth = username + ":" + password;

        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String formattedUrl = String.format(base_url,period,dealerId);

        ResponseEntity<ApiResponse<FinanceOperation>> response = restTemplate.exchange(formattedUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        handleResponseStatus(response, "Moliyaviy operatsiyalar");
        return response.getBody();
    }

    @Override
    public ApiResponse<SalesOperation> fetchSaleOperationLineChart(Long dealerId, String period) {
        String base_url = url + "line-chart/invoices?type=%s&dealerId=%s";
        String auth = username + ":" + password;

        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String formattedUrl = String.format(base_url,period,dealerId);

        ResponseEntity<ApiResponse<SalesOperation>> response = restTemplate.exchange(formattedUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        handleResponseStatus(response, "Soatlik savdo");
        return response.getBody();
    }

    private void handleResponseStatus(ResponseEntity<?> response, String operationName) {
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            log.info("{} muvaffaqiyatli olingan: {}", operationName, response.getBody());
        } else {
            log.error("{} ni olishda xatolik yuz berdi: Status = {}", operationName, response.getStatusCode());
        }
    }
}
