package uz.duol.akfadealerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import uz.duol.akfadealerbot.constants.Role;
import uz.duol.akfadealerbot.constants.TableNames;
import uz.duol.akfadealerbot.dto.UserDto;
import uz.duol.akfadealerbot.entity.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableNames.AKFA_GROUP_USER)
public class UserEntity extends BaseEntity {

    @Column(name= "full_name")
    private String fullName;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private ClientEntity client;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name= "code", unique = true)
    private String code;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id") // Foreign key in the Order table
    private List<DealerEntity> dealers;

    @Column(name= "main_id")
    private Long mainId;

    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isVerified;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Column(name = "create_date",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createDate = LocalDateTime.now();

    public UserDto getDto() {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(this, userDto);
        return userDto;
    }
}
