package uz.duol.akfadealerbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.duol.akfadealerbot.model.entity.ClientActionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientActionRepository extends JpaRepository<ClientActionEntity,Long>, JpaSpecificationExecutor<ClientActionEntity> {

    Optional<ClientActionEntity> findFirstByChatIdOrderByActionDateDesc(Long chatId);


    List<ClientActionEntity> findAllByChatIdOrderByActionDateDesc(Long chatId);

    Optional<ClientActionEntity> findFirstByClientIdOrderByActionDateDesc(Long clientId);

    @Transactional
    void deleteAllByChatId(Long chatId);
}
