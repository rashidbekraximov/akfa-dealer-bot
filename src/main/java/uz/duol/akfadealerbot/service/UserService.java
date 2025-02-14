package uz.duol.akfadealerbot.service;


import uz.duol.akfadealerbot.model.dto.UserDto;
import uz.duol.akfadealerbot.model.entity.UserEntity;
import uz.duol.akfadealerbot.model.response.TgUserInfo;

public interface UserService {

    UserEntity findById(Long id);

    TgUserInfo getTgInfo(String role, Long id);

    UserEntity loadByCode(String code);

    UserEntity verify(Long chatId, String code);

    UserEntity getDealerId(String name);

    UserEntity findByDealerName(String name);

    void deleteByRoleAndId(String role, Long id);

    void deleteConnectedTelegramAccount(String role, Long id);

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    boolean existByCode(String code);
}
