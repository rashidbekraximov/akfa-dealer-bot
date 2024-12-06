package uz.duol.akfadealerbot.dto;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.File;
import java.util.Objects;

public class PhotoSend {
    private final Long chatId;
    private final String caption;
    private final File photo;
    private final ReplyKeyboard keyboard;

    public PhotoSend(Long chatId, String caption, File photo, ReplyKeyboard keyboard) {
        this.chatId = chatId;
        this.caption = caption;
        this.photo = photo;
        this.keyboard = keyboard;
    }

    public PhotoSend(Long chatId, File photo) {
        this.chatId = chatId;
        this.caption = null;
        this.photo = photo;
        this.keyboard = null;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getCaption() {
        return caption;
    }

    public File getPhoto() {
        return photo;
    }

    public ReplyKeyboard getKeyboard() {
        return keyboard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, caption, photo, keyboard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoSend that = (PhotoSend) o;
        return Objects.equals(chatId, that.chatId) &&
                Objects.equals(caption, that.caption) &&
                Objects.equals(photo, that.photo) &&
                Objects.equals(keyboard, that.keyboard);
    }

    @Override
    public String toString() {
        return "PhotoSend{" +
                "chatId=" + chatId +
                ", caption='" + caption + '\'' +
                ", photo='" + photo + '\'' +
                ", keyboard=" + keyboard +
                '}';
    }

}
