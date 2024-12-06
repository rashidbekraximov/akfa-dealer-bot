package uz.duol.akfadealerbot.handlers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.impl.*;
import uz.duol.akfadealerbot.dto.ClientActionDto;
import uz.duol.akfadealerbot.dto.ClientDto;
import uz.duol.akfadealerbot.handlers.Handler;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.DealerService;
import uz.duol.akfadealerbot.utils.R;

import java.util.Locale;

@Service
@RequiredArgsConstructor
class MessageHandler implements Handler<Message> {

    private final StartCommand startCommand;

    private final DealerService dealerService;

    private final ClientService clientService;

    private final ClientActionService clientActionService;

    private final RequestToVerifyCommand requestToVerifyCommand;

    private final ActionHandler actionHandler;

    private final SettingsCommand settingsCommand;

    private final ChangeLanguageCommand changeLanguageCommand;

    private final GeneralCommand generalCommand;

    private final BackCommand backCommand;

    private final DailyEndReportCommand dailyEndReportCommand;

    @PatchMapping
    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();

        ClientDto client = null;
        if (clientService.existByChatId(chatId))
            client = clientService.findByTelegramId(chatId).getDto();


        //TODO Entities Command handler
        if (text.startsWith(Commands.START_COMMAND)) {
            startCommand.execute(chatId, (client != null && client.getLocale() != null) ? client.getLocale() : null);
            clientService.save(message.getFrom());
            return;
        }

        Locale locale = null;
        if (client != null) {
            locale = client.getLocale();
        }

        ClientActionDto action = clientActionService.findLastActionByChatId(chatId);

        //TODO Language Checker handler
        if (text.equals(Commands.LANGUAGE_RU) || text.equals(Commands.LANGUAGE_KR) || text.equals(Commands.LANGUAGE_UZ)) {
            client = clientService.updateLanguage(chatId, Commands.getLanguage(text));
            if (client.getDealer() == null || !client.getDealer().isVerified()){
                requestToVerifyCommand.execute(chatId,client.getLocale());
            }else{
                generalCommand.execute(chatId, client.getLocale());
            }
            return;
        }

        //TODO Daily End handler
        if (text.equals(R.bundle(locale).getString("label.command.day.end"))) {
//            clientActionService.saveAction(Actions.CHANGE_LANGUAGE_ACTION, chatId);
            dailyEndReportCommand.execute(chatId,locale);
            return;
        }

        //TODO Daily Report handler
        if (text.equals(R.bundle(locale).getString("label.command.daily"))) {
//            clientActionService.saveAction(Actions.CHANGE_LANGUAGE_ACTION, chatId);
            return;
        }

        //TODO Change language  handler
        if (text.equals(R.bundle(locale).getString("label.command.settings.language"))) {
            clientActionService.saveAction(Actions.CHANGE_LANGUAGE_ACTION, chatId);
            changeLanguageCommand.execute(chatId, locale);
            return;
        }

        //TODO Settings handler
        if (text.equals(R.bundle(locale).getString("label.command.settings.language"))) {
            clientActionService.saveAction(Actions.CHANGE_LANGUAGE_ACTION, chatId);
            changeLanguageCommand.execute(chatId, locale);
            return;
        }

        //TODO Settings handler
        if (text.equals(R.bundle(locale).getString("label.command.settings.language"))) {
            clientActionService.saveAction(Actions.CHANGE_LANGUAGE_ACTION, chatId);
            changeLanguageCommand.execute(chatId, locale);
            return;
        }

        if (text.equals(R.bundle(locale).getString("label.command.settings"))) {
            clientActionService.saveAction(Actions.SETTING_ACTION, chatId);
            settingsCommand.execute(chatId, locale);
            return;
        }

        //TODO General handlers
        if (text.equals(R.bundle(locale).getString("label.command.back"))) {
            backCommand.execute(message, locale);
            return;
        }

        if (action != null) {
            actionHandler.handle(message);
            return;
        }
    }
}
