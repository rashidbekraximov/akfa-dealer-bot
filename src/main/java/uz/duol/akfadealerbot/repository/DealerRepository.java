package uz.duol.akfadealerbot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.duol.akfadealerbot.entity.DealerEntity;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<DealerEntity,Long>, JpaSpecificationExecutor<DealerEntity> {

    Optional<DealerEntity> findByCode(String code);
}
