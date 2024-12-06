package uz.duol.akfadealerbot.handlers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.duol.akfadealerbot.handlers.Handler;

@Service
@RequiredArgsConstructor
public class UpdateHandler implements Handler<Update> {

    private final MessageHandler messageHandler;

    @Override
    public void handle(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                messageHandler.handle(message);
            }
        } else if (update.hasCallbackQuery()) {
        }
    }
}

