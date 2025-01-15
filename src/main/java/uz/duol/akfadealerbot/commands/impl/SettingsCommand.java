package uz.duol.akfadealerbot.commands.impl;


import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.dto.MessageSend;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.KeyboardUtils;
import uz.duol.akfadealerbot.utils.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class SettingsCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Override
    public void execute(Long chatId, Locale locale) {
        ResourceBundle bundle = R.bundle(locale);
        telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.command.settings"),
                KeyboardUtils.create(new ArrayList<KeyboardRow>() {{
                    add(new KeyboardRow() {{
                        add(bundle.getString("label.command.settings.language"));
                    }});
                    add(new KeyboardRow() {{
                        add(bundle.getString("label.command.call-center"));
                    }});
                    add(new KeyboardRow() {{
                        add(bundle.getString("label.command.back"));
                    }});
                }})));

    }
}

