package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.constants.Period;
import uz.duol.akfadealerbot.dto.ActionSend;
import uz.duol.akfadealerbot.dto.MessageSend;
import uz.duol.akfadealerbot.entity.ClientEntity;
import uz.duol.akfadealerbot.entity.DealerEntity;
import uz.duol.akfadealerbot.repository.UserRepository;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.ImageProcessService;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.KeyboardUtils;
import uz.duol.akfadealerbot.utils.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class TodayReportCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Resource
    private ImageProcessService imageProcessService;

    @Resource
    private ClientService clientService;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ClientActionService clientActionService;

    @Override
    public void execute(Long chatId, Locale locale) {
        ClientEntity client = clientService.findByTelegramId(chatId);
        if (client == null) {
            return;
        }

        telegramService.sendAction(new ActionSend(chatId, ActionType.UPLOADPHOTO));
        if (client.getUser().getDealers().isEmpty()){
            imageProcessService.sendImageGroup(locale, client.getUser().getMainId(),client.getUser().getFullName(), chatId, Period.TODAY.toLowerCaseName());
        }else {
            clientActionService.saveAction(Actions.LOAD_DEALERS_ACTION, chatId);
            telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.loaded.dealer.list"),createDealerMenuKeyboard(locale, client.getUser().getDealers())));
        }
    }

    public ReplyKeyboardMarkup createDealerMenuKeyboard(Locale locale, List<DealerEntity> dealers) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        // Loop through the list of dealers and create a keyboard button for each
        for (DealerEntity dealer : dealers) {
            KeyboardRow row = new KeyboardRow();

            // Add dealer's name or any other field to the button text
            row.add(new KeyboardButton(dealer.getName()));

            // Add the row to the keyboard
            keyboardRows.add(row);
        }
        // Add the settings command
        KeyboardRow settingsRow = new KeyboardRow();
        settingsRow.add(bundle.getString("label.command.back"));
        keyboardRows.add(settingsRow);
        return KeyboardUtils.create(keyboardRows);
    }
}
