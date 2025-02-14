package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.model.dto.MessageSend;
import uz.duol.akfadealerbot.service.TelegramService;

import java.util.Locale;

@Component
public class DetectorCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Override
    public void execute(Long chatId, Locale locale) {
        telegramService.sendMessage(new MessageSend(chatId, "Sizga botdan foydalanishga ruxsat berilmagan ❌", Commands.createEmptyKeyboard()));
    }
}
