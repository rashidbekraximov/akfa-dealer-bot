package uz.duol.akfadealerbot.service;

import uz.duol.akfadealerbot.model.dto.Diagram;

import java.io.File;
import java.util.Locale;

public interface ImageProcessService {

    File writeImage(Locale locale, Diagram diagram);

    void sendImageGroup(Locale locale, Long dealerId, String name, Long chatId, String period);

}
