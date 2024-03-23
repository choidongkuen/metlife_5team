package com.metlife.core.property;


import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtPropertyTest {

    @Autowired
    private JwtProperty jwtProperty;

    @Test
    @DisplayName("jwtProperty 바인딩 테스트를 진행한다.")
    void test_jwtProperty_binding(){
        // given
        String bearer = jwtProperty.getBearer();
        String secret = jwtProperty.getSecret();

        // then
        assertThat(bearer).isEqualTo("Bearer");
    }
}
