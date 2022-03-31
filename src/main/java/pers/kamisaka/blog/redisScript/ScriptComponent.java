package pers.kamisaka.blog.ratelimit;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

@Component
public class RateLimitComponent {
    //读取编写的redis限流脚本
    @Bean
    public DefaultRedisScript<Long> redisScript(){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("TokenBucket.lua")));
        redisScript.setResultType(java.lang.Long.class);
        return redisScript;
    }

}
