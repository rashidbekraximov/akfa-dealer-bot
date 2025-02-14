package uz.duol.akfadealerbot.service;

import uz.duol.akfadealerbot.model.dto.ClientActionDto;

public interface ClientActionService {

    ClientActionDto findLastActionByChatId(Long chatId);

    void saveAction(String action, Long chatId);

    void clearActions(Long chatId);
}
