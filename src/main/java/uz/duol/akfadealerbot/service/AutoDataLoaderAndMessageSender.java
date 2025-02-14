package uz.duol.akfadealerbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.duol.akfadealerbot.constants.Period;
import uz.duol.akfadealerbot.model.dto.ClientDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoDataLoaderAndMessageSender {

    private final ImageProcessService imageProcessService;

    private final ClientService clientService;

    @Scheduled(cron = "0 10 * * * *")
    public void autoImageSender() {
        List<ClientDto> clients = clientService.findAllByDealerIsVerified();

        for (ClientDto client : clients) {
            try {
                if (client.getUser().getDealers().isEmpty()) {
                    imageProcessService.sendImageGroup(client.getLocale(),client.getUser().getMainId(), client.getUser().getFullName(), client.getChatId(), Period.YESTERDAY.toLowerCaseName());
                } else {
                    client.getUser().getDealers()
                            .forEach(dealer -> imageProcessService.sendImageGroup(client.getLocale(),dealer.getDealerId(), dealer.getName(), client.getChatId(), Period.YESTERDAY.toLowerCaseName()));
                }
            } catch (Exception e) {
                log.error("Xatolik yuz berdi mijozni qayta ishlashda: {}: {}", client.getChatId(), e.getMessage(), e);
            }
        }
        log.info("Barcha mijozlarga media group jo'natildi.");
    }
}