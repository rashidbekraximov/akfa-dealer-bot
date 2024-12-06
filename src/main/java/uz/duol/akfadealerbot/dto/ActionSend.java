package uz.duol.akfadealerbot.dto;


import org.telegram.telegrambots.meta.api.methods.ActionType;

import java.util.Objects;

public class ActionSend {
    private final Long chatId;
    private final ActionType action;

    public ActionSend(Long chatId, ActionType action) {
        this.chatId = chatId;
        this.action = action;
    }

    public ActionType getAction() {
        return action;
    }

    public Long getChatId() {
        return chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionSend that = (ActionSend) o;
        return Objects.equals(chatId, that.chatId) &&
                Objects.equals(action, that.action);
    }

    @Override
    public String toString() {
        return "PhotoSend{" +
                "chatId=" + chatId +
                ", caption='" + action +
                '}';
    }

}


