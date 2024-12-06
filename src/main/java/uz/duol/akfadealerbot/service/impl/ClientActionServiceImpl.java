package uz.duol.akfadealerbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.duol.akfadealerbot.dto.ClientActionDto;
import uz.duol.akfadealerbot.entity.ClientActionEntity;
import uz.duol.akfadealerbot.entity.ClientEntity;
import uz.duol.akfadealerbot.repository.ClientActionRepository;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.ClientService;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientActionServiceImpl implements ClientActionService {

    private final ClientActionRepository clientActionRepository;

    private final ClientService clientService;

    @Override
    public ClientActionDto findLastActionByChatId(Long chatId) {
        return clientActionRepository.findFirstByChatIdOrderByActionDateDesc(chatId).map(ClientActionEntity::getDto)
                .orElseGet(() -> {
                    log.error("Klientning oxirgi holat ma'lumoti topilmadi: {}", chatId);
                    return null;
                });
    }

    @Override
    public void saveAction(String action, Long chatId) {
        ClientEntity client = clientService.findByTelegramId(chatId);

        ClientActionEntity clientActionEntity = new ClientActionEntity();
        clientActionEntity.setChatId(chatId);
        clientActionEntity.setActionDate(new Date());
        clientActionEntity.setAction(action);
        clientActionEntity.setClientId(client.getId());

        try {
            clientActionRepository.save(clientActionEntity);
            log.info("Klientning harakati saqlandi: chatId: {}, clientId: {}, action: {}", chatId, client.getId(), action);
        } catch (Exception e) {
            log.error("Klientning harakati saqlanib bo‘lmadi: chatId: {}, clientId: {}, action: {}. Xato: {}", chatId, client.getId(), action, e.getMessage());
        }
    }

    @Override
    public void clearActions(Long chatId) {
        try {
            clientActionRepository.deleteAllByChatId(chatId);
            log.info("Klientning barcha harakatlari chatId bo‘yicha o‘chirildi: {}", chatId);
        } catch (Exception e) {
            log.error("Klientning harakatlarini chatId bo‘yicha o‘chirishda xato yuz berdi: {}. Xato: {}", chatId, e.getMessage());
        }
    }
}
