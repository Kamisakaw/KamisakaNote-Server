package pers.kamisaka.blog.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//设置作用范围：类和方法，设置运行时生效
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    //用于在redis中唯一标识被限流请求的key
    String key() default "";
    //限流信息的持续时间
    int time();
    //限流的次数
    int count();
}
