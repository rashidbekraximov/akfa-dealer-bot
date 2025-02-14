package uz.duol.akfadealerbot.service;


import org.telegram.telegrambots.meta.api.objects.User;
import uz.duol.akfadealerbot.model.dto.ClientDto;
import uz.duol.akfadealerbot.model.entity.ClientEntity;
import uz.duol.akfadealerbot.model.entity.UserEntity;
import uz.duol.akfadealerbot.model.response.AuthResponse;
import uz.duol.akfadealerbot.model.response.TgUserInfo;

import java.util.List;

public interface ClientService {

    AuthResponse validateByCode(String code);

    String generateCode(Long chatId);

    ClientEntity findById(Long id);

    ClientEntity findByTelegramId(Long chatId);

    void save(User dealer);

    void update(UserEntity dealer, Long chatId);

    boolean existByChatId(Long chatId);

    boolean userIsActiveByChatId(Long chatId);

    boolean existByDealerCode(String code);

    ClientDto updateLanguage(Long chatId, String language);

    List<ClientDto> findAllByDealerIsVerified();
}
