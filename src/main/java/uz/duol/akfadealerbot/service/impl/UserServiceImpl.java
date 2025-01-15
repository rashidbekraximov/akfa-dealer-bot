package uz.duol.akfadealerbot.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.duol.akfadealerbot.constants.Role;
import uz.duol.akfadealerbot.dto.UserDto;
import uz.duol.akfadealerbot.entity.DealerEntity;
import uz.duol.akfadealerbot.entity.UserEntity;
import uz.duol.akfadealerbot.repository.UserRepository;
import uz.duol.akfadealerbot.service.ClientService;
import uz.duol.akfadealerbot.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ClientService clientService;

    private final UserRepository userRepository;

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User ID boʻyicha ma'lumot topilmadi: {}", id);
                    throw new NotFoundException("User ID boʻyicha ma'lumot topilmadi: {}");
                });
    }

    @Override
    public UserEntity loadByCode(String code) {
        return userRepository.findByCode(code)
                .orElseGet(() -> {
                    log.error("Kiritilgan code boʻyicha user ma'lumot topilmadi: {}", code);
                    return null;
                });
    }

    @Override
    public UserEntity verify(Long chatId, String code) {
        if (clientService.existByDealerCode(code)){
            log.warn("User allaqachon bog'langan: {}", code);
            return null;
        }

        UserEntity user = loadByCode(code);
        if (user == null) {
            log.warn("User uchun yaroqsiz kod: {}", chatId);
            return null;
        }
        user.setVerified(true);
        user.setVerifiedDate(LocalDateTime.now());
        try {
            clientService.update(user, chatId);
            log.info("User: {} muvaffaqiyatli tasdiqlandi.", chatId);
            return user;
        } catch (Exception e) {
            log.error("Userni chatId bilan tasdiqlab bo‘lmadi: {}. Xato: {}", chatId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public UserEntity getDealerId(String name) {
        return userRepository.findByFullName(name).orElseGet(() -> {
            log.error("Kiritilgan name boʻyicha dealer ma'lumoti topilmadi: {}", name);
            return null;
        });
    }

    @Override
    public UserEntity findByDealerName(String name) {
        return userRepository.findByDealerName(name).orElseGet(() -> {
            log.error("Kiritilgan name boʻyicha user ma'lumoti topilmadi: {}", name);
            return null;
        });
    }

    @Transactional
    @Override
    public void deleteByRoleAndId(String role, Long id) {
        userRepository.findByRoleAndMainId(Role.valueOf(role), id)
                .ifPresentOrElse(user -> {
                    try {
                        user.setActive(false);
                        user.setClient(null);// Perform soft delete
                        userRepository.save(user); // Save updated user
                        log.info("User ID: {} va role: {} uchun ma'lumot o'chirildi.", id, role);
                    } catch (Exception e) {
                        log.error("User ID: {} va role: {} uchun ma'lumotni o'chirishda xatolik yuzaga keldi: {}", id, role, e.getMessage());
                        throw new RuntimeException("Failed to deactivate user with ID: " + id, e);
                    }
                }, () -> {
                    log.error("User ID: {} va role: {} topilmadi.", id, role);
                    throw new NotFoundException("User with ID: " + id + " and role: " + role + " not found.");
                });
    }


    @Override
    public UserDto create(UserDto user) {

        Optional<UserEntity> existingUser = userRepository.findByRoleAndMainId(user.getRole(),user.getMainId());

        if (existingUser.isPresent()){
            return update(user);
        }

        UserEntity userEntity = UserEntity.builder()
                .fullName(user.getFullName())
                .mainId(user.getMainId())
                .role(user.getRole())
                .isActive(true)
                .code(generateCode())
                .dealers(convertToList(user.getDealerIds()))
                .createDate(LocalDateTime.now())
                .build();

        // Save the entity if needed and map it back to DTO
        userRepository.save(userEntity);
        return mapToDto(userEntity);
    }

    @Override
    public UserDto update(UserDto userDto) {
        // Fetch the existing user
        UserEntity existingUser = userRepository.findByRoleAndMainId(userDto.getRole(),userDto.getMainId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userDto.getMainId()));

        // Update fields
        existingUser.setFullName(userDto.getFullName());
        existingUser.setMainId(userDto.getMainId());
        existingUser.setRole(userDto.getRole());
        existingUser.setDealers(convertToList(userDto.getDealerIds()));
        existingUser.setCreateDate(LocalDateTime.now());

        // Save updated entity
        userRepository.save(existingUser);

        // Map back to DTO
        return mapToDto(existingUser);
    }

    @Override
    public boolean existByCode(String code) {
        return userRepository.existsByCode(code);
    }


    private List<DealerEntity> convertToList(String dealers) {
        if (dealers == null || dealers.trim().isEmpty()) {
            return Collections.emptyList(); // Null yoki bo'sh stringni qayta ishlash
        }

        if (!dealers.contains(",")){
            Optional<UserEntity> user = userRepository.findByRoleAndMainId(Role.DEALER, Long.valueOf(dealers));
            return user.map(userEntity -> List.of(new DealerEntity(userEntity.getMainId(), userEntity.getFullName()))).orElseGet(List::of);
        }

        return Arrays.stream(dealers.split(",")) // Vergul bilan ajratamiz
                .map(String::trim) // Bo'sh joylarni kesib tashlash
                .filter(id -> !id.isEmpty()) // Bo'sh ID'larni chiqarib tashlash
                .map(id -> {
                    try {
                        long dealerId = Long.parseLong(id); // ID ni uzun son sifatida parse qilish
                        return userRepository.findByRoleAndMainId(Role.DEALER, dealerId)
                                .orElseGet(() -> {
                                    log.error("Dealer information not found for ID: {}", id);
                                    return null;
                                });
                    } catch (NumberFormatException e) {
                        log.error("Invalid dealer ID format: {}", id, e); // ID son bo'lmasa
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Null qiymatlarni filtrlash
                .map(user -> new DealerEntity(user.getMainId(), user.getFullName())) // DealerEntity yaratish
                .collect(Collectors.toList()); // Natijani yig'ish
    }



    // Mapping Entity to DTO (Placeholder)
    private UserDto mapToDto(UserEntity userEntity) {
        return UserDto.builder()
                .fullName(userEntity.getFullName())
                .mainId(userEntity.getMainId())
                .role(userEntity.getRole())
                .dealerIds(userEntity.getDealers().stream()
                        .map(DealerEntity::getId) // Assuming DealerEntity has an ID field
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .build();
    }


    private String generateCode(){
        String code = String.valueOf((int)(Math.random() * 90000) + 10000);
        if (existByCode(code)){
            return generateCode();
        }else{
            return code;
        }
    }
}
