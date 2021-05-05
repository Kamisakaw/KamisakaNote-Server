//package pers.kamisaka.blog.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class RedisUtil {
//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
//
//    //1.通用方法
//
//    public boolean setExpire(String key,long time){
//        try{
//            if(time > 0){
//                redisTemplate.expire(key,time, TimeUnit.SECONDS);
//            }
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public long getExpire(String key){
//        return redisTemplate.getExpire(key);
//    }
//
//    public boolean hasKey(String key){
//        try{
//            return redisTemplate.hasKey(key);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public void del(String... key){
//        if(key != null && key.length > 0){
//            redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
//        }
//    }
//
//    //2.值为Object时的操作
//    public Object get(String key){
//        return key == null ? null : redisTemplate.opsForValue().get(key);
//    }
//
//    public boolean set(String key,Object value){
//        try{
//            redisTemplate.opsForValue().set(key,value);
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean set(String key,Object value,long time){
//        try{
//            if(time > 0){
//                redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
//            }
//            else{
//                this.set(key, value);
//            }
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public long increment(String key,long delta){
//        return redisTemplate.opsForValue().increment(key, delta);
//    }
//
//    //3.对
//}
