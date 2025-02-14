package uz.duol.akfadealerbot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.duol.akfadealerbot.model.entity.DealerEntity;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<DealerEntity,Long> {

    Optional<DealerEntity> findByName(String name);

}
