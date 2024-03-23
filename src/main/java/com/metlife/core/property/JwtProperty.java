package com.metlife.core.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {
    private String bearer;
    private String secret;
    private Access access;
    private Refresh refresh;

    @Getter
    @Setter
    public static class Access {
        private Long expiration;
        private String header;
    }

    @Getter
    @Setter
    public static class Refresh {
        private Long expiration;
        private String header;
    }

}
