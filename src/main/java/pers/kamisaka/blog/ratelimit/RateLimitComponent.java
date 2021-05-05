package pers.kamisaka.blog.ratelimit;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class RateLimitComponent {
    //读取编写的redis限流脚本
    @Bean
    public DefaultRedisScript<Number> redisScript(){
        DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("TokenBucket.lua")));
        redisScript.setResultType(Number.class);
        return redisScript;
    }

//    @Bean
//    public RedisTemplate<String,Serializable> RateLimitTemplate(LettuceConnectionFactory factory){
//        RedisTemplate<String,Serializable> template = new RedisTemplate<String, Serializable>();
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setConnectionFactory(factory);
//        return template;
//    }
}
