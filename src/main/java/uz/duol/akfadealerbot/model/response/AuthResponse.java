package uz.duol.akfadealerbot.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {

    private Long id;

    private Integer status;

    private String message;
}
