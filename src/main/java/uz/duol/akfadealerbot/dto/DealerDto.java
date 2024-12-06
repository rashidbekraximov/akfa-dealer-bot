package uz.duol.akfadealerbot.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.dto.base.BaseDto;
import uz.duol.akfadealerbot.entity.DealerEntity;

@Getter
@Setter
public class DealerDto extends BaseDto {

    private String fullName;

    private ClientDto client;

    private String code;

    private boolean isVerified;

    public DealerEntity getDto() {
        DealerEntity dealer = new DealerEntity();
        BeanUtils.copyProperties(this, dealer);
        return dealer;
    }
}

