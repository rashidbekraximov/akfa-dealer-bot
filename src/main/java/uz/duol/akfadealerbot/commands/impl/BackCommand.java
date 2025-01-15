package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.dto.ClientActionDto;
import uz.duol.akfadealerbot.service.ClientActionService;

import java.util.Locale;
import java.util.Objects;


@Component
public class BackCommand implements Command<Message> {

    @Resource
    private ClientActionService clientActionService;

    @Resource
    private GeneralCommand generalCommand;

    @Transactional
    @Override
    public void execute(Message message, Locale locale) {
        Long chatId = message.getChatId();
        ClientActionDto action = clientActionService.findLastActionByChatId(chatId);

        if (Objects.equals(action.getAction(), Actions.LOAD_DEALERS_ACTION)) {
            clientActionService.saveAction(Actions.MAIN_MENU_ACTION, chatId);
            backAll(chatId,locale);
            return;
        }

        backAll(chatId, locale);
    }

    private void backAll(Long chatId, Locale locale) {
        generalCommand.execute(chatId, locale);
    }
}
