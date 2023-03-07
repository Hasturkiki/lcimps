package scau.lcimp.lcimps.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    private String accessToken;

    private String refreshToken;

    private Long expires;
}
