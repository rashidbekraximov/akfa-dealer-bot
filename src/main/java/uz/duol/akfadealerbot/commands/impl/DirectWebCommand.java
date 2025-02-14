package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.model.dto.MessageSend;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.TelegramService;

import java.util.Locale;

@Component
public class DirectWebCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Resource
    private ClientService clientService;

    @Override
    public void execute(Long chatId, Locale locale) {
        String code = clientService.generateCode(chatId);

        if (code == null) {
            return;
        }

        String message = String.format("""
                Hurmatli foydalanuvchi, sizning bir martalik kodingiz: %s ✅\s
                
                <a href="http://report.akfadiler.uz/">\uD83C\uDF0D Rasmiy saytga kirish</a>""",code);

        telegramService.sendMessage(new MessageSend(chatId, message));
    }
}
