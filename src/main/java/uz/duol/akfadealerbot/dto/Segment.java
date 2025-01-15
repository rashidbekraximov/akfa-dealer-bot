package uz.duol.akfadealerbot.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Segment {

    private String segmentName;

    private Double dsProfitAmount;
}
