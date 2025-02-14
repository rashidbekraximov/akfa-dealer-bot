package uz.duol.akfadealerbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.duol.akfadealerbot.model.dto.ActionSend;
import uz.duol.akfadealerbot.model.dto.MediaGroupSend;
import uz.duol.akfadealerbot.model.dto.MessageSend;
import uz.duol.akfadealerbot.model.dto.PhotoSend;
import uz.duol.akfadealerbot.exception.FailedSendMessageException;

@Service
public class TelegramService extends DefaultAbsSender {

    protected TelegramService(@Value("${telegram.bot.token}") String token) {
        super(new DefaultBotOptions(), token);
    }

    public Message sendMessage(MessageSend message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(message.getText());
        sendMessage.setParseMode("HTML");
            sendMessage.setReplyMarkup(message.getKeyboard());
        try {
            return execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new FailedSendMessageException(String.format("Failed send text message %s", message), e);
        }
    }

    public Message sendPhoto(PhotoSend photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(photo.getChatId());
        sendPhoto.setCaption(photo.getCaption());
        sendPhoto.setParseMode("HTML");
        sendPhoto.setPhoto(new InputFile(photo.getPhoto()));
        if (photo.getKeyboard() != null) {
            sendPhoto.setReplyMarkup(photo.getKeyboard());
        }
        try {
            return execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new FailedSendMessageException(String.format("Failed send text message %s", photo), e);
        }
    }

    public Boolean sendAction(ActionSend actionSend) {
        SendChatAction action  = new SendChatAction ();
        action.setChatId(actionSend.getChatId());
        action.setAction(actionSend.getAction());
        try {
            return execute(action);
        } catch (TelegramApiException e) {
            throw new FailedSendMessageException(String.format("Failed send action %s", action), e);
        }
    }

    public void sendMediaGroup(MediaGroupSend mediaGroupSend) {
        SendMediaGroup sendMedia = new SendMediaGroup();
        sendMedia.setChatId(mediaGroupSend.getChatId());
        sendMedia.setMedias(mediaGroupSend.getPhoto());
        try {
            execute(sendMedia);
        } catch (TelegramApiException e) {
            throw new FailedSendMessageException(String.format("Failed send text message %s", sendMedia), e);
        }
    }
}

