package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.constants.FilePath;
import uz.duol.akfadealerbot.model.dto.PhotoSend;
import uz.duol.akfadealerbot.service.TelegramService;

import java.io.File;
import java.util.Locale;

@Component
public class CallDataCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Override
    public void execute(Long chatId, Locale locale) {
        String caption = """
                \uD83C\uDFE2 DUOL\s

                ☎️ Aloqa markazi: +998712302626""";

        telegramService.sendPhoto(new PhotoSend(chatId, caption,new File(new FilePath().IMAGE_PATH + "logo.png"),null));
    }

}
