package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.model.dto.MessageSend;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.KeyboardUtils;
import uz.duol.akfadealerbot.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class GeneralCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Override
    public void execute(Long chatId, Locale locale) {
        telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.main.menu"), createGeneralMenuKeyboard(locale)));
    }

    public ReplyKeyboardMarkup createGeneralMenuKeyboard(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        // Add the settings command
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(bundle.getString("label.command.day.end"));
        keyboardRows.add(firstRow);

        // Add the web command
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(bundle.getString("label.direct.web"));
        keyboardRows.add(secondRow);

        // Add the settings command
        KeyboardRow settingsRow = new KeyboardRow();
        settingsRow.add(bundle.getString("label.command.settings"));
        keyboardRows.add(settingsRow);

        return KeyboardUtils.create(keyboardRows);
    }
}
