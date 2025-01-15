package uz.duol.akfadealerbot.commands.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import uz.duol.akfadealerbot.commands.Actions;
import uz.duol.akfadealerbot.commands.Command;
import uz.duol.akfadealerbot.dto.MessageSend;
import uz.duol.akfadealerbot.entity.ClientEntity;
import uz.duol.akfadealerbot.service.ClientActionService;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.TelegramService;
import uz.duol.akfadealerbot.utils.R;

import java.util.Locale;

@Component
public class StartCommand implements Command<Long> {

    @Resource
    private TelegramService telegramService;

    @Resource
    private GeneralCommand generalCommand;

    @Resource
    private ClientService clientService;

    @Resource
    private ClientActionService clientActionService;

    @Override
    public void execute(Long chatId, Locale locale) {
        ClientEntity client = clientService.findByTelegramId(chatId);
        if (client == null){
            telegramService.sendMessage(new MessageSend(chatId, getWelcomeText(),Commands.createLanguageKeyboard()));
            return;
        }
        if (client.getUser() != null && client.getUser().isVerified()){
            generalCommand.execute(chatId, locale);
        }else if (client.getLanguage() == null){
            telegramService.sendMessage(new MessageSend(chatId, getWelcomeText(), Commands.createLanguageKeyboard()));
        }else {
            clientActionService.saveAction(Actions.REQUEST_CODE_ACTION, chatId);
            telegramService.sendMessage(new MessageSend(chatId, R.bundle(locale).getString("label.authentication"),Commands.createEmptyKeyboard()));
        }
    }

    public String getWelcomeText() {
        return "Assalomu alaykum va xush kelibsiz! \uD83D\uDC4B\uD83C\uDF89\n" +
                "\n" +
                "Bu bot faqat **Akfa Dilerlar** uchun mo‘ljallangan. \uD83D\uDCBC  \n" +
                "Bu bot orqali o‘z faoliyatingizni boshqarishingiz va kerakli ma'lumotlarni olish imkoniyatiga ega bo‘lasiz. \uD83D\uDCCA\n" +
                "\n" +
                "\uD83C\uDF10 Botdan foydalanishni boshlash uchun iltimos, o‘zingizga qulay bo‘lgan tilni tanlang:  \n" +
                "\n" +
                "Tilni tanlash uchun quyidagi menyudan kerakli variantni tanlang yoki tegishli tugmani bosing.  \n" +
                "\uD83D\uDC47 Tanlang \uD83D\uDC47\n";
    }
}

