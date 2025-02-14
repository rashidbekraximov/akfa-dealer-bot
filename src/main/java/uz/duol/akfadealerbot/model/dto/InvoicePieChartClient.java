package uz.duol.akfadealerbot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePieChartClient {

    private Long newDealerClientCount;

    private Long oldDealerClientCount;
}
