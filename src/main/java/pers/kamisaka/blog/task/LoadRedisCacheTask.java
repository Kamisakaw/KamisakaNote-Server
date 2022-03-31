package pers.kamisaka.blog.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pers.kamisaka.blog.util.RedisUtil;

import java.util.List;

@Component
public class LoadRedisCacheTask {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    public void execute(){
        List<String> articleList = redisTemplate.execute()
    }
}
