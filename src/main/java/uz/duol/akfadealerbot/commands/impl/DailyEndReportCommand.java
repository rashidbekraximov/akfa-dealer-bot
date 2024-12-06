package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.dto.ActionSend;
import uz.duol.akfadealerbot.dto.PhotoSend;
import uz.duol.akfadealerbot.service.ImageWriterService;
import uz.duol.akfadealerbot.service.TelegramService;

import java.io.File;
import java.util.Locale;

@Component
public class DailyEndReportCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Resource
    private ImageWriterService imageWriterService;

    @Override
    public void execute(Long chatId, Locale locale) {
        telegramService.sendAction(new ActionSend(chatId, ActionType.UPLOADPHOTO));
        try {
            File output = imageWriterService.writeImage("total_income.png","3 000 000");
            telegramService.sendPhoto(new PhotoSend(chatId, "Daily end report",output,null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
