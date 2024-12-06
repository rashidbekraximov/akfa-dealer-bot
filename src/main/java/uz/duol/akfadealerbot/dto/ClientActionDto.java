package uz.duol.akfadealerbot.dto;

import lombok.Getter;
import lombok.Setter;
import uz.duol.akfadealerbot.dto.base.BaseDto;
import uz.duol.akfadealerbot.entity.ClientEntity;

import java.util.Date;

@Getter
@Setter
public class ClientActionDto extends BaseDto {

    private Long chatId;

    private ClientDto client;

    private String action;

    private Date actionDate;
}

