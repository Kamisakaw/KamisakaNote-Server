package pers.kamisaka.blog.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.kamisaka.blog.ratelimit.RateLimit;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Aspect
@Configuration
public class RateLimitConfig {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private DefaultRedisScript<Long> redisScript;

    //execution内的表达式含义（从左到右）：
    // *-返回类型；
    // pers.kamisaka.blog.controller-包名
    // ；..-该包及其子包；
    // *-所有的类；
    // *-所有方法名；
    // (..)所有参数
    @Around("execution(* pers.kamisaka.blog.controller..*.*(..) )")
    /*
    这个拦截功能是通过AOP在controller包下的所有方法执行时进行增强的
    限流执行过程：
    1.获取切面的方法签名
    2.获取切面所在的类、方法、注解等信息
    3.从RequestContextHolder获取请求request，从其首部获取请求的IP
    4.生成lua脚本的运行参数keys，其中包含请求的IP、切面所在类、切面所在方法名、RateLimit注解中的key值。
    5.将上面生成的keys和RateLimit注解中的参数count、expire参数作为执行参数，用于运行lua脚本
    在redis缓存中，数据是这样的：keys-time ，time的阈值是count，其持续时间即为expire
     */
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        //对加上了RateLimit注解的函数才执行限流操作
        if(rateLimit != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = getIp(request);

            //构造调用脚本时使用的keys
            StringBuffer sb = new StringBuffer();
            //存储触发限流的controller类、方法名、标识id
            sb.append(ip).append(":")
              .append(targetClass.getName()).append(":")
              .append(method.getName()).append(":")
              .append(rateLimit.key());
            List<String> keys = Collections.singletonList(sb.toString());
            //获取当前时间
            Long currentMills = (Long) redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.time());

            //调用脚本
            Long number = (Long) redisTemplate.execute(redisScript,
                    keys,rateLimit.volume(),rateLimit.rate(),rateLimit.expire(),currentMills);

            //当返回结果合法时，说明执行成功，从此处返回切面
            if(number != null && number.intValue() > 0){
                System.out.println("桶中还有"+number.toString()+"个令牌");
                return joinPoint.proceed();
            }
            //执行到这里，说明限流达到上限，请求失败
            throw new RuntimeException("已到达最大限流次数");
        }
        //对没有RateLimit注解的不执行限流操作
        else{
            return joinPoint.proceed();
        }
    }

    public static String getIp(HttpServletRequest request){
        String ip = null;
        try{
            //依次查询请求的各个请求头，获取发送请求的客户端IP，如果没有就调用getRemoteAddr()
            ip = request.getHeader("x-forward-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }
            //处理多个IP的情况，此时每个IP通过逗号隔开，第一个IP视为真实IP
            if(ip != null && ip.length() > 15){
                if(ip.indexOf(",")>0){
                    ip = ip.substring(0,ip.indexOf(","));
                }
            }
        }catch (Exception e){
            //上述方法如果发生异常，就置IP为空
            ip = "";
        }
        return ip.replace(':','：');
    }
}
