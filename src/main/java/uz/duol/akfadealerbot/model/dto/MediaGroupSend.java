package uz.duol.akfadealerbot.model.dto;


import org.telegram.telegrambots.meta.api.objects.media.InputMedia;

import java.util.List;
import java.util.Objects;

public class MediaGroupSend {

    private final Long chatId;
    private final String caption;
    private final List<InputMedia> photoGroup;

    public MediaGroupSend(Long chatId, String caption, List<InputMedia> photoGroup) {
        this.chatId = chatId;
        this.caption = caption;
        this.photoGroup = photoGroup;
    }

    public MediaGroupSend(Long chatId, List<InputMedia> photo) {
        this.chatId = chatId;
        this.caption = null;
        this.photoGroup = photo;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getCaption() {
        return caption;
    }

    public List<InputMedia> getPhoto() {
        return photoGroup;
    }


    @Override
    public int hashCode() {
        return Objects.hash(chatId, caption, photoGroup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaGroupSend that = (MediaGroupSend) o;
        return Objects.equals(chatId, that.chatId) &&
                Objects.equals(caption, that.caption) &&
                Objects.equals(photoGroup, that.photoGroup);
    }

    @Override
    public String toString() {
        return "PhotoSend{" +
                "chatId=" + chatId +
                ", caption='" + caption + '\'' +
                ", photo='" + photoGroup + '\'' +
                '}';
    }
}
