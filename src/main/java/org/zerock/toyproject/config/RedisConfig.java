package org.zerock.redisinspring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

//수동으로 메서드를 빈을 등록할 떄 해당 클래스에 쓰는 어노테이션이다.
@Configuration
public class RedisConfig {
    //.yml , .properies에 설정된 값들을 넣어주는 어노테이션이다.
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        /**
         * Lettuce라는 라이브러리를 통해 Redis 연결을 관리하는 객체 생성
         * Redis 서버에 관한 정보(host, port)를 설정
         */
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }
}
