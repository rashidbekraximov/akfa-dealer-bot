package uz.duol.akfadealerbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.duol.akfadealerbot.entity.ClientEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity> {

    Optional<ClientEntity> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    boolean existsByDealer_Code(String dealer_code);

    List<ClientEntity> findAllByDealerIsNotNull();
}
