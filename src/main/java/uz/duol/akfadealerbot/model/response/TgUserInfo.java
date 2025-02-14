package uz.duol.akfadealerbot.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.duol.akfadealerbot.model.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TgUserInfo {

    private Long chatId;

    private String phone;

    private String fullName;

    private String userName;

    private String code;

    private LocalDateTime verifiedDate;
}
