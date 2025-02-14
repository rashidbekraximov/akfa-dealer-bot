package uz.duol.akfadealerbot.model.dto.base;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public class BaseDto implements Serializable {

    private Long id;
}
