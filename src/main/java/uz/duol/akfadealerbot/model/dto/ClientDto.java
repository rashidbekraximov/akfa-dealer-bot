package uz.duol.akfadealerbot.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.model.dto.base.BaseDto;
import uz.duol.akfadealerbot.model.entity.ClientEntity;
import uz.duol.akfadealerbot.model.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
public class ClientDto extends BaseDto {

    private Long chatId;

    private UserEntity user;

    private String phone;

    private String firstName;

    private String lastName;

    private String userName;

    private String language;

    private String code;

    private String createdCodeTime;

    private LocalDateTime createDate;

    public Locale getLocale() {
        return language != null ? new Locale(language) : null;
    }

    public ClientEntity getDto() {
        ClientEntity client = new ClientEntity();
        BeanUtils.copyProperties(this, client);
        return client;
    }
}

