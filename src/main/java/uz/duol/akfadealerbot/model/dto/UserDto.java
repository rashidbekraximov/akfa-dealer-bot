package uz.duol.akfadealerbot.model.dto;

import lombok.*;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.constants.Role;
import uz.duol.akfadealerbot.model.dto.base.BaseDto;
import uz.duol.akfadealerbot.model.entity.UserEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto extends BaseDto {

    private String fullName;

    private Role role;

    private Long mainId;

    private String dealerIds;

    private boolean isVerified;

    public UserEntity getDto() {
        UserEntity dealer = new UserEntity();
        BeanUtils.copyProperties(this, dealer);
        return dealer;
    }
}

