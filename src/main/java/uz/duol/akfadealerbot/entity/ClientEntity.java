package uz.duol.akfadealerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.dto.ClientDto;
import uz.duol.akfadealerbot.entity.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = TableNames.AKFA_CLIENT)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity extends BaseEntity {

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dealer_id", referencedColumnName = "id")
    private DealerEntity dealer;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "language")
    private String language;

    @Column(name = "createDate",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createDate = LocalDateTime.now();

    public ClientDto getDto() {
        ClientDto clientDto = new ClientDto();
        BeanUtils.copyProperties(this, clientDto);
        if (getDealer() != null) {
            clientDto.setDealer(getDealer().getDto());
        }
        return clientDto;
    }

}

