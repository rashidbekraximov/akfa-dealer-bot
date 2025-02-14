package uz.duol.akfadealerbot.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.model.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableNames.AKFA_GROUP_DEALER)
public class DealerEntity extends BaseEntity {

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "name")
    private String name;
}
