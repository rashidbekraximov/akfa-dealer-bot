package uz.duol.akfadealerbot.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.entity.base.BaseEntity;

import java.util.List;

@Entity
@Table(name = TableNames.AKFA_INVOICE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntity extends BaseEntity {

    @Column(name = "sale_count")
    private int salesCount;

    @Column(name = "revenue")
    private Double revenue;

    @Column(name = "debt")
    private Double debt;

    @Column(name = "average_sale")
    private Double averageSale;

    @ElementCollection
    @CollectionTable(name = "invoice_top_products", // Alohida jadval nomi
            joinColumns = @JoinColumn(name = "invoice_id")) // Bosh jadval bilan bog‘lanish
    @Column(name = "product_name")
    private List<String> topProducts; // Eng yaxshi mahsulotlar ro‘yxati

    @Column(name = "customer_count")
    private int customerCount;

}
