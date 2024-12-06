package uz.duol.akfadealerbot.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.duol.akfadealerbot.dto.ClientDto;
import uz.duol.akfadealerbot.dto.DealerDto;
import uz.duol.akfadealerbot.entity.ClientEntity;
import uz.duol.akfadealerbot.entity.DealerEntity;
import uz.duol.akfadealerbot.repository.ClientRepository;
import uz.duol.akfadealerbot.repository.DealerRepository;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.DealerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientEntity findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Klient ID boʻyicha ma'lumot topilmadi: {}", id);
                    throw new NotFoundException("Klient ID boʻyicha ma'lumot topilmadi: {}");
                });
    }

    @Override
    public ClientEntity findByTelegramId(Long chatId) {
        return clientRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    log.error("Klient CHATID boʻyicha ma'lumot topilmadi: {}", chatId);
                    return null;
                });
    }

    @Override
    public void save(User user) {
        if (existByChatId(user.getId())) {
            return;
        }

        ClientEntity client = ClientEntity.builder()
                .chatId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUserName())
                .build();

        try {
            clientRepository.save(client);
            log.info("Yangi klient muvaffaqiyatli saqlandi: {}", client);
        } catch (Exception e) {
            log.error("Klientni chatId bilan saqlab boʻlmadi: {}. Xato: {}", user.getId(), e.getMessage(), e);
            throw new RuntimeException("Could not save dealer: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(DealerEntity dealer, Long chatId) {
        ClientEntity client = findByTelegramId(chatId);
        try {
            if (client != null){
                client.setDealer(dealer);
                clientRepository.save(client);
                log.info("Klient ma'lumotlari yangilandi: {}", client);
            }
        } catch (Exception e) {
            log.error("Klientni chatId bilan saqlab boʻlmadi: {}. Xato: {}", chatId, e.getMessage(), e);
            throw new RuntimeException("Could not save dealer: " + e.getMessage(), e);
        }
    }

    @Override
    public ClientDto updateLanguage(Long chatId, String language) {
        ClientEntity dealer = findByTelegramId(chatId);
        dealer.setLanguage(language);
        try {
            log.info("Klientning tili yangilandi: {}. Yangi til: {}", chatId, language);
            return clientRepository.save(dealer).getDto();
        } catch (Exception e) {
            log.error("Klientning tilini yangilab bo‘lmadi: {}. Xato: {}", chatId, e.getMessage(), e);
            throw new RuntimeException("Klient tilini yangilab bo‘lmadi: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClientDto> findAllByDealerIsVerified() {
        return clientRepository.findAllByDealerIsNotNull().stream().map(ClientEntity::getDto).collect(Collectors.toList());
    }

    @Override
    public boolean existByChatId(Long chatId) {
        return clientRepository.existsByChatId(chatId);
    }

    @Override
    public boolean existByDealerCode(String code) {
        return clientRepository.existsByDealer_Code(code);
    }
}
