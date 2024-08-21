package org.zerock.redisinspring.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//Cache 기능을 수행하는 메서드를 수동저장하기위한 명시
@Configuration
//캐시 기능이 가능하도록 하는 어노테이션
@EnableCaching
public class RedisCacheConfig {
    //boardCacheManager 메서드를 빈으로 등록
    @Bean
    public CacheManager boardCacheManager(RedisConnectionFactory redisConnectionFactory) {
        //RedisConfiguration 객체를 생서하고, 캐시를 관리하는 기본 Redis의 기본설정
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                //key 값을 직렬화 하여 redis에 저장하는 것, 문자열로 직렬화
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()))
                //value 값을 직렬화 하여 redis애 저장하는 것, Jackson에서 제공하는 라이브러리를 통해 json으로 직렬화
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new Jackson2JsonRedisSerializer<Object>(Object.class)
                        )
                )
                //TTL 시간 설정 , 1분
                .entryTtl(Duration.ofMinutes(1L));

        //builder 패턴을 사용하여 객체를 반환하는 것
        return RedisCacheManager
                .RedisCacheManagerBuilder
                //RedisConfig에서 설정한 Redis 관리 객체를 주입
                .fromConnectionFactory(redisConnectionFactory)
                //Redis 캐시 기능을 가진 configuration을 주입
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}

