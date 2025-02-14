package uz.duol.akfadealerbot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DailyData {

    private Long totalSales; // Umumiy sotuvlar soni

    private String topProducts; // Eng ko'p sotilgan mahsulotlar

    private Double totalIncome; // Umumiy daromad

    private Integer currencyId; // Valyuta ID (masalan, 1 = UZS)

    private Integer clientsCount; // Mijozlar soni

    private Double averageSalesCost; // O'rtacha sotuv qiymati (null bo'lishi mumkin)

    private Long lastSyncedDate; // Oxirgi sinxronizatsiya vaqti (timestamp)

}
