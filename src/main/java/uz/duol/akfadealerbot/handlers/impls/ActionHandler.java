package uz.duol.akfadealerbot.handlers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.impl.GeneralCommand;
import uz.duol.akfadealerbot.dto.ClientActionDto;
import uz.duol.akfadealerbot.dto.MessageSend;
import uz.duol.akfadealerbot.entity.DealerEntity;
import uz.duol.akfadealerbot.handlers.Handler;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.DealerService;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.R;
import uz.duol.akfadealerbot.utils.Validator;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
class ActionHandler implements Handler<Message> {

    private final ClientActionService clientActionService;

    private final DealerService dealerService;

    private final ClientService clientService;

    private final TelegramService telegramService;

    private final GeneralCommand generalCommand;

    @Transactional
    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        ClientActionDto action = clientActionService.findLastActionByChatId(chatId);

        if (action.getClient() == null){
            return;
        }

        Locale locale =  action.getClient().getLocale();

        if (Objects.equals(action.getAction(), Actions.REQUEST_CODE_ACTION)) {
            String code = message.getText();

            if (!Validator.isValidFourDigitNumber(code)) {
                telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.validation.error")));
                return;
            }

            DealerEntity dealer = dealerService.verify(chatId, code);
            if (dealer != null) {
                clientActionService.saveAction(Actions.MAIN_MENU_ACTION, chatId);
                telegramService.sendMessage(new MessageSend(chatId, String.format(R.bundle(locale).getString("label.success"), dealer.getFullName()),
                        generalCommand.createGeneralMenuKeyboard(locale)));
            } else {
                telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.already.exist")));
            }
        }
    }
}
