package pers.kamisaka.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //1.通用方法

    //设置缓存有效时间
    public boolean expire(String key,long time){
        try{
            if(time > 0){
                redisTemplate.expire(key, (long) (time+Math.random()), TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //获取缓存有效时间
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    //检查键为key的缓存是否存在
    public boolean hasKey(String key){
        try{
            return redisTemplate.hasKey(key);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //删除缓存，允许传入多个key值进行删除
    public void del(String... key){
        if(key != null && key.length > 0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }
            else{
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    //2.值为Object时的操作
    //获取缓存
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    //添加缓存
    public boolean set(String key,Object value){
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){

            e.printStackTrace();
            return false;
        }
    }

    //添加缓存并设置有效时间
    public boolean set(String key,Object value,long time){
        try{
            if(time > 0){
                redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
            }
            else{
                this.set(key, value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //递增
    public long increment(String key,long delta){
        return redisTemplate.opsForValue().increment(key, delta);
    }

    //递减
    public long decr(String key,long delta){
        if(delta < 0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,-delta);
    }

    //3.值为Map时的操作
    //获取键值为key的哈希表下，键item映射的值
    public Object hget(String key,String item){
        return redisTemplate.opsForHash().get(key,item);
    }

    //获取键值为key的哈希表
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    //添加哈希表到缓存，键值为key，内容为map
    public boolean hmset(String key,Map<String,Object> map){
        try{
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    //添加哈希表到缓存，其键值为key，内容为map，并设置有效时间
    public boolean hmset(String key,Map<String,Object> map,long time){
        try{
            redisTemplate.opsForHash().putAll(key,map);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    //为键值为key的哈希表下，键值为item的映射赋值value
    public boolean hset(String key,String item,Object value){
        try{
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //为键值为key的哈希表下，键值为item的映射赋值value，并设置有效时间
    public boolean hset(String key,String item,Object value,long time){
        try{
            redisTemplate.opsForHash().put(key,item,value);
            if(time>0){
                expire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除键值为key的哈希表下，键值为item的映射
    public void hdel(String key,Object... item){
        redisTemplate.opsForHash().delete(key, item);
    }

    //判断键值为key的哈希表下，是否存在键值为item的映射
    public boolean hHasKey(String key,String item){
        return redisTemplate.opsForHash().hasKey(key,item);
    }

    //为键值为key的哈希表下，键值为item的映射值递增delta
    public double hincr(String key,String item,double delta){
        return redisTemplate.opsForHash().increment(key,item,delta);
    }

    //为键值为key的哈希表下，键值为item的映射值递减delta
    public double hdecr(String key,String item,double delta){
        if(delta < 0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key,item,-delta);
    }

    //4.值为set时的操作
    //获取键值为key的集合
    public Set<Object> sGet(String key){
        try{
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //检查键值为key的集合中，是否存在元素item
    public boolean sHasKey(String key,Object item){
        try{
            return redisTemplate.opsForSet().isMember(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //为键值为key的集合存入值items，返回成功存入的元素个数
    public long sSet(String key,Object... items){
        try {
            return redisTemplate.opsForSet().add(key, items);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //为键值为key的集合存入值items，并设置有效时间，返回成功存入的元素个数
    public long sSet(String key,long time,Object... items){
        try{
            long count = redisTemplate.opsForSet().add(key,items);
            if(time>0){
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获取键值为key的集合的大小
    public long sSize(String key){
        try{
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //从键值为key的集合中删除元素，返回成功删除元素的个数
    public long sRemove(String key,Object... items){
        try {
            Long count = redisTemplate.opsForSet().remove(key,items);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //5.值为list的操作
    //从键值为key的列表中获取[start,end]范围中的元素，end=-1代表获取所有元素
    public List<Object> lGet(String key,long start,long end){
        try{
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取键值key的列表的长度
    public long lSize(String key){
        try{
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获取键值为key的列表中，索引值为index的元素的值，index>=0从表头计算，index<0从表尾计算
    public Object lIndex(String key,long index){
        try{
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //向键值为key的列表添加元素value
    public boolean lSet(String key,Object value){
        try{
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //向键值为key的列表添加元素value，并设置有效时间
    public boolean lSet(String key,Object value,long time){
        try{
            redisTemplate.opsForList().rightPush(key, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //向键值为key的列表添加一个列表的元素value
    public boolean lSet(String key,List<Object> value){
        try{
            redisTemplate.opsForList().rightPushAll(key,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //向键值为key的列表添加一个列表的元素value，并设置有效时间
    public boolean lSet(String key,List<Object> value,long time){
        try{
            redisTemplate.opsForList().rightPushAll(key,value);
            expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //修改键值为key的列表中索引为index的元素
    public boolean lUpdate(String key,long index,Object value){
        try{
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除键值为key的列表中count个值为value的元素
    public long lRemove(String key,long count,Object value){
        try{
            long res = redisTemplate.opsForList().remove(key, count, value);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
