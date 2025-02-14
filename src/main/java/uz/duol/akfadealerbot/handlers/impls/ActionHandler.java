package uz.duol.akfadealerbot.handlers.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.impl.GeneralCommand;
import uz.duol.akfadealerbot.constants.Period;
import uz.duol.akfadealerbot.model.dto.ActionSend;
import uz.duol.akfadealerbot.model.dto.ClientActionDto;
import uz.duol.akfadealerbot.model.dto.MessageSend;
import uz.duol.akfadealerbot.model.entity.UserEntity;
import uz.duol.akfadealerbot.handlers.Handler;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.ImageProcessService;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.service.UserService;
import uz.duol.akfadealerbot.utils.R;
import uz.duol.akfadealerbot.utils.Validator;

import java.util.Locale;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
class ActionHandler implements Handler<Message> {

    private final ClientActionService clientActionService;

    private final UserService userService;

    private final ImageProcessService imageProcessService;

    private final TelegramService telegramService;

    private final GeneralCommand generalCommand;

    @Transactional
    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        ClientActionDto action = clientActionService.findLastActionByChatId(chatId);

        String text = message.getText();
        if (action.getClient() == null) {
            return;
        }

        Locale locale = action.getClient().getLocale();

        if (Objects.equals(action.getAction(), Actions.REQUEST_CODE_ACTION)) {
            if (!Validator.isValidFourDigitNumber(text)) {
                telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.validation.error")));
                return;
            }

            UserEntity dealer = userService.verify(chatId, text);
            if (dealer != null) {
                clientActionService.saveAction(Actions.MAIN_MENU_ACTION, chatId);
                telegramService.sendMessage(new MessageSend(chatId, String.format(R.bundle(locale).getString("label.success"), dealer.getFullName()),
                        generalCommand.createGeneralMenuKeyboard(locale)));
            } else {
                telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.already.exist")));
            }
            return;
        }

        if (Objects.equals(action.getAction(), Actions.LOAD_DEALERS_ACTION)) {
            UserEntity dealer = userService.getDealerId(text);
            telegramService.sendAction(new ActionSend(chatId, ActionType.UPLOADPHOTO));
            if (dealer == null) {
                telegramService.sendMessage(new MessageSend(chatId, "❌ Diler topilmadi."));
                return;
            }
            imageProcessService.sendImageGroup(locale, dealer.getMainId(), dealer.getFullName(), chatId, Period.YESTERDAY.toLowerCaseName());
        }
    }
}
