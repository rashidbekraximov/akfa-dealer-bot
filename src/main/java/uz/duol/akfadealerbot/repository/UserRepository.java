package uz.duol.akfadealerbot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.duol.akfadealerbot.constants.Role;
import uz.duol.akfadealerbot.model.entity.UserEntity;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByCode(String code);

    @Query("SELECT u FROM UserEntity u JOIN u.dealers d WHERE d.name = :dealerName")
    Optional<UserEntity> findByDealerName(@Param("dealerName") String dealerName);

    @Modifying
    void deleteByRoleAndMainId(Role role, Long mainId);

    Optional<UserEntity> findByRoleAndMainId(Role role, Long mainId);

    Optional<UserEntity> findByMainId(Long mainId);

    Optional<UserEntity> findByFullName(String fullName);

    boolean existsByCode(String code);

    @Query(value = "SELECT get_employee_salary()", nativeQuery = true)
    BigDecimal getEmployeeSalary();
    
}
