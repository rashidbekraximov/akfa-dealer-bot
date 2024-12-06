package uz.duol.akfadealerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.dto.DealerDto;
import uz.duol.akfadealerbot.entity.base.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableNames.AKFA_GROUP_DEALER)
public class DealerEntity extends BaseEntity {

    @Column(name= "full_name")
    private String fullName;

    @OneToOne(mappedBy = "dealer", fetch = FetchType.LAZY)
    private ClientEntity client;

    @Column(name= "code")
    private String code;

    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isVerified;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Column(name = "create_date",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createDate = LocalDateTime.now();

    public DealerDto getDto() {
        DealerDto dealerDto = new DealerDto();
        BeanUtils.copyProperties(this, dealerDto);
        return dealerDto;
    }
}
