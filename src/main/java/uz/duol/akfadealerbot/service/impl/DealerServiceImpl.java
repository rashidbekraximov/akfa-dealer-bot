package uz.duol.akfadealerbot.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.duol.akfadealerbot.entity.DealerEntity;
import uz.duol.akfadealerbot.repository.DealerRepository;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.DealerService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealerServiceImpl implements DealerService {

    private final ClientService clientService;

    private final DealerRepository dealerRepository;

    @Override
    public DealerEntity findById(Long id) {
        return dealerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Diler ID boʻyicha ma'lumot topilmadi: {}", id);
                    throw new NotFoundException("Diler ID boʻyicha ma'lumot topilmadi: {}");
                });
    }

    @Override
    public DealerEntity loadByCode(String code) {
        return dealerRepository.findByCode(code)
                .orElseGet(() -> {
                    log.error("Kiritilgan code boʻyicha diler ma'lumot topilmadi: {}", code);
                    return null;
                });
    }

    @Override
    public DealerEntity verify(Long chatId, String code) {
        if (clientService.existByDealerCode(code)){
            log.warn("Diler allaqachon bog'langan: {}", code);
            return null;
        }

        DealerEntity dealer = loadByCode(code);
        if (dealer == null) {
            log.warn("Diler uchun yaroqsiz kod: {}", chatId);
            return null;
        }
        dealer.setVerified(true);
        dealer.setVerifiedDate(LocalDateTime.now());
        try {
            clientService.update(dealer, chatId);
            log.info("Diler: {} muvaffaqiyatli tasdiqlandi.", chatId);
            return dealer;
        } catch (Exception e) {
            log.error("Dilerni chatId bilan tasdiqlab bo‘lmadi: {}. Xato: {}", chatId, e.getMessage(), e);
            return null;
        }
    }
}
