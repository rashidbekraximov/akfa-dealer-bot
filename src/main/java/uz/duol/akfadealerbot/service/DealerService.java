package uz.duol.akfadealerbot.service;


import uz.duol.akfadealerbot.entity.DealerEntity;

public interface DealerService {

    DealerEntity findById(Long id);

    DealerEntity loadByCode(String code);

    DealerEntity verify(Long chatId,String code);
}
