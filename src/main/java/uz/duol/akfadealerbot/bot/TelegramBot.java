package uz.duol.akfadealerbot.bot;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.duol.akfadealerbot.handlers.impls.UpdateHandler;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    @Resource
    private UpdateHandler updateHandler;

    public TelegramBot(@Value("${telegram.bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handle(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
