package uz.duol.akfadealerbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.duol.akfadealerbot.entity.ClientEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity> {

    Optional<ClientEntity> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    @Query("SELECT COUNT(u) > 0 FROM ClientEntity u WHERE u.chatId = :chatId AND u.user.isActive = true")
    boolean existActiveUserByChatId(@Param("chatId") String chatId);

    boolean existsByUser_Code(String dealer_code);

    List<ClientEntity> findAllByUserIsNotNullAndUserIsActiveTrue();

}
