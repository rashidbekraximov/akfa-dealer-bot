package uz.duol.akfadealerbot.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Handler<T> {

    void handle(T t) throws JsonProcessingException;

}
