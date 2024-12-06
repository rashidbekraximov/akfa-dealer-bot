package uz.duol.akfadealerbot.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.duol.akfadealerbot.service.ExternalDataService;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalDataServiceImpl implements ExternalDataService {

    @Value("report.username")
    private String username;

    @Value("report.password")
    private String password;

    @Value("report.url")
    private String url;

    private final RestTemplate restTemplate;

    @Override
    public String fetchData() {
        String auth = username + ":" + password;

        // Java 8 Base64 kodlash
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Log yozish: so'rov URL va sarlavhalar
        log.info("Sending request to URL: {}", url);
        log.info("Request headers: {}", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Log yozish: javob
        log.info("Received response: {}", response.getBody());
        return response.getBody();
    }

}
