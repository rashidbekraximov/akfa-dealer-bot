package uz.duol.akfadealerbot.service;


import org.telegram.telegrambots.meta.api.objects.User;
import uz.duol.akfadealerbot.dto.ClientDto;
import uz.duol.akfadealerbot.dto.DealerDto;
import uz.duol.akfadealerbot.entity.ClientEntity;
import uz.duol.akfadealerbot.entity.DealerEntity;

import java.util.List;

public interface ClientService {

    ClientEntity findById(Long id);

    ClientEntity findByTelegramId(Long chatId);

    void save(User dealer);

    void update(DealerEntity dealer,Long chatId);

    boolean existByChatId(Long chatId);

    boolean existByDealerCode(String code);

    ClientDto updateLanguage(Long chatId, String language);

    List<ClientDto> findAllByDealerIsVerified();
}
