package uz.duol.akfadealerbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceOperation {

    private String hour;

    private Double income;

    private Double outcome;

}
