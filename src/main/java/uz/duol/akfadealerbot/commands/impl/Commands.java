package uz.duol.akfadealerbot.commands.impl;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import uz.duol.akfadealerbot.utils.KeyboardUtils;
import uz.duol.akfadealerbot.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Commands {

    public static final String START_COMMAND = "/start";
    public static final String MENU_COMMAND = "/menu";

    public static final String LANGUAGE_UZ = "\uD83C\uDDFA\uD83C\uDDFF O'zbekcha";
    public static final String LANGUAGE_KR = "\uD83C\uDDFA\uD83C\uDDFF Ўзбекча (Крилл)";
    public static final String LANGUAGE_RU = "\uD83C\uDDF7\uD83C\uDDFA Русский";

    private Commands() {
    }

    public static ReplyKeyboardMarkup backOnly(Locale locale) {
        ResourceBundle bundle = R.bundle(locale);
        return KeyboardUtils.create(new ArrayList<KeyboardRow>() {{
            add(new KeyboardRow() {{
                add(bundle.getString("label.command.back"));
            }});
        }});
    }

    public static ReplyKeyboardMarkup createLanguageKeyboard() {
        return KeyboardUtils.create(new ArrayList<KeyboardRow>() {{
            add(new KeyboardRow() {{
                add(LANGUAGE_UZ);
                add(LANGUAGE_RU);
            }});
            add(new KeyboardRow() {{
                add(LANGUAGE_KR);
            }});
        }});
    }

    public static String getLanguage(String command) {
        if (command.equals(LANGUAGE_UZ)) return "uz";
        if (command.equals(LANGUAGE_RU)) return "ru";
        if (command.equals(LANGUAGE_KR)) return "kr";
        return null;
    }

    public static ReplyKeyboardRemove createEmptyKeyboard() {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        return replyKeyboardRemove;
    }
}

