package uz.duol.akfadealerbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.duol.akfadealerbot.dto.ClientDto;
import uz.duol.akfadealerbot.dto.PhotoSend;
import uz.duol.akfadealerbot.entity.ClientEntity;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoMessageSender {

    private final ImageWriterService imageWriterService;

    private final ExternalDataService externalDataService;

    private final TelegramService telegramService;

    private final ClientService clientService;

    @Scheduled(cron = "0 17 * * * *")
    public void autoImageSender() {
        List<ClientDto> clients = clientService.findAllByDealerIsVerified();

        // ExecutorService yordamida parallel ishlov berish
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 ta ip yaratish

        for (ClientDto client : clients) {
            // Har bir client uchun yangi thread yaratish
            executorService.submit(() -> {
                try {
                    // Rasmni yaratish
                    File output = imageWriterService.writeImage("total_income.png", "3 000 000");

                    telegramService.sendPhoto(new PhotoSend(client.getChatId(), "Daily end report", output, null));

                    // Muvaffaqiyatli log yozuvi
                    log.info("Rasm yuborildi: {} fayli bilan, Chat ID: {}", output.getName(), client.getChatId());

                } catch (Exception e) {
                    // Xatolikni loglash
                    log.error("Xato yuz berdi rasm yuborishda, Chat ID: {}: {}", client.getChatId(), e.getMessage(), e);
                }
            });
        }

        // ExecutorService ni to'xtatish
        executorService.shutdown();

        // Valyuta kurslari haqida umumiy log
        log.info("Barcha dilerlarga ko'rsatkichlar yuborildi.");
    }



    @Scheduled(cron = "0 0,20,40 * * * *")
    public void autoLoaderFinancialIndicators() {
        // ExecutorService yordamida parallel ishlov berish
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 ta ip yaratish

        log.info("Dilerlarning ko'rsatkichlar yangilandi.");
    }
}
