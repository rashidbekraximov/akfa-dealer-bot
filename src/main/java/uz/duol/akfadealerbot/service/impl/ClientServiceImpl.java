package uz.duol.akfadealerbot.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.duol.akfadealerbot.model.dto.ClientDto;
import uz.duol.akfadealerbot.model.entity.ClientEntity;
import uz.duol.akfadealerbot.model.entity.UserEntity;
import uz.duol.akfadealerbot.model.response.AuthResponse;
import uz.duol.akfadealerbot.model.response.TgUserInfo;
import uz.duol.akfadealerbot.repository.ClientRepository;
import uz.duol.akfadealerbot.service.ClientService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Value("${authentication.code.expiry.time}")
    private Long expiryTime;

    private final ClientRepository clientRepository;

    @Override
    public AuthResponse validateByCode(String code) {
        ClientEntity client = clientRepository.findByCode(code).orElse(null);
        if (client == null){
            return AuthResponse.builder()
                    .status(500)
                    .message("Kirish kodingiz muddati o‘tgan, iltimos, uni yangilang.")
                    .build();
        }

        if (validateExpiryDate(client.getCreatedCodeTime())){
            return AuthResponse.builder()
                    .status(500)
                    .message("Bu kodning yaroqlilik muddati tugagan. Iltimos, yangi kodni kiriting.")
                    .build();
        }

        return AuthResponse.builder()
                .id(client.getUser().getMainId())
                .status(200)
                .message("Foydalanuvchi topildi")
                .build();
    }

    @Override
    public String generateCode(Long chatId) {
        ClientEntity client = findByTelegramId(chatId);
        if (client == null){
            throw new NotFoundException("Klient ID bo'yicha ma'lumot topilmadi: {}");
        }

        String code = generateRandomCode();

        try {
            client.setCode(code);
            client.setCreatedCodeTime(LocalDateTime.now());
            clientRepository.save(client);
        }catch (Exception e){
            throw new RuntimeException("Kod yaratilmadi .");
        }
        return code;
    }

    @Override
    public ClientEntity findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Klient ID boʻyicha ma'lumot topilmadi: {}", id);
                    return new NotFoundException("Klient ID boʻyicha ma'lumot topilmadi: {}");
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
                .language("uz")
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
    public void update(UserEntity user, Long chatId) {
        ClientEntity client = findByTelegramId(chatId);
        try {
            if (client != null){
                client.setUser(user);
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
        ClientEntity user = findByTelegramId(chatId);
        user.setLanguage(language);
        try {
            log.info("Klientning tili yangilandi: {}. Yangi til: {}", chatId, language);
            return clientRepository.save(user).getDto();
        } catch (Exception e) {
            log.error("Klientning tilini yangilab bo‘lmadi: {}. Xato: {}", chatId, e.getMessage(), e);
            throw new RuntimeException("Klient tilini yangilab bo‘lmadi: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClientDto> findAllByDealerIsVerified() {
        return clientRepository.findAllByUserIsNotNullAndUserIsActiveTrue().stream().map(ClientEntity::getDto).collect(Collectors.toList());
    }

    @Override
    public boolean existByChatId(Long chatId) {
        return clientRepository.existsByChatId(chatId);
    }

    @Override
    public boolean userIsActiveByChatId(Long chatId) {
        return clientRepository.existActiveUserByChatId(String.valueOf(chatId));
    }

    @Override
    public boolean existByDealerCode(String code) {
        return clientRepository.existsByUser_Code(code);
    }


    private boolean validateExpiryDate(LocalDateTime createdCodeTime){
        LocalDateTime now = LocalDateTime.now();

        long minutesPassed = ChronoUnit.MINUTES.between(createdCodeTime, now);
        return minutesPassed >= expiryTime;
    }

    public boolean existByCode(String code) {
        return clientRepository.existsByCode(code);
    }

    private String generateRandomCode(){
        String code = String.valueOf((int)(Math.random() * 90000) + 10000);
        if (existByCode(code)){
            return generateRandomCode();
        }else{
            return code;
        }
    }
}
