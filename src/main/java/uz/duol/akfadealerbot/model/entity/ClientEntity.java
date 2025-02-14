package uz.duol.akfadealerbot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.model.dto.ClientDto;
import uz.duol.akfadealerbot.model.entity.base.BaseEntity;

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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "language")
    private String language;

    @Column(name = "code", length = 5)
    private String code;

    @Column(name = "created_code_time")
    private LocalDateTime createdCodeTime;

    @Column(name = "createDate",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createDate = LocalDateTime.now();

    public ClientDto getDto() {
        ClientDto clientDto = new ClientDto();
        BeanUtils.copyProperties(this, clientDto);
        if (getUser() != null) {
            clientDto.setUser(getUser());
        }
        return clientDto;
    }
}

