package uz.duol.akfadealerbot.entity.base;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !getClass().getName().equalsIgnoreCase(obj.getClass().getName())){
            return true;
        }
        if(this==obj){
            return true;
        }
        return Objects.equals(getId(),((BaseEntity)obj).getId());
    }

    @Override
    public String toString() {
        return getClass().getName()+"{" +
                "id=" + id +
                '}';
    }
}

