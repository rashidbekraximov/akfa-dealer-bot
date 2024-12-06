package uz.duol.akfadealerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.dto.ClientActionDto;
import uz.duol.akfadealerbot.entity.base.BaseEntity;

import java.util.Date;

@Entity
@Table(name = TableNames.AKFA_GROUP_CLIENT_ACTION)
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientActionEntity extends BaseEntity {

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private ClientEntity client;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Date actionDate;

    public ClientActionDto getDto() {
        ClientActionDto dto = new ClientActionDto();
        BeanUtils.copyProperties(this,dto);
        if (getClient() != null) {
            dto.setClient(getClient().getDto());
        }
        return dto;
    }
}

