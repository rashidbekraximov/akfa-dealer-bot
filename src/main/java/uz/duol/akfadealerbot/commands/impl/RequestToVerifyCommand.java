package uz.duol.akfadealerbot.commands.impl;


import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.dto.MessageSend;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.R;

import java.util.Locale;

@Component
public class RequestToVerifyCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Resource
    private ClientActionService clientActionService;

    @Override
    public void execute(Long chatId, Locale locale) {
        clientActionService.saveAction(Actions.REQUEST_CODE_ACTION, chatId);
        telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.authentication"),Commands.createEmptyKeyboard()));
    }
}