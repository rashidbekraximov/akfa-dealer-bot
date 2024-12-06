package uz.duol.akfadealerbot.commands;

import java.util.Locale;

public interface Command<T> {

    void execute(T response, Locale locale);

}
