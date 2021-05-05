package pers.kamisaka.blog.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pers.kamisaka.blog.entity.AuthenticationException;
import pers.kamisaka.blog.entity.User;
import pers.kamisaka.blog.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.TimeUnit;

@Component
public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //System.out.println(token);
        //不对非web method做出拦截
        if(token==null || token.equals("undefined")){
            throw new AuthenticationException("tokenError","无令牌，请重新登陆");
            //request.getRequestDispatcher("/blog/admin/login").forward(request,response);
        }
        String uid;
        try{
            //根据JwtUtil类封装的顺序，token的第一项就是用户名
            uid = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException e){
            throw new AuthenticationException("tokenError","解码错误，请重新登陆");
            //request.getRequestDispatcher("/blog/admin/login").forward(request,response);
        }
        User user = userService.getUserByUid(uid);
        if(user == null){
            throw new AuthenticationException("tokenError","用户不存在，请重新登陆");
            //request.getRequestDispatcher("/blog/admin/login").forward(request,response);
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        DecodedJWT jwt = null;
        try{
            jwt = jwtVerifier.verify(token);
            System.out.println(jwt);
        }catch (JWTVerificationException e){
            throw new AuthenticationException("tokenError","校验错误，请重新登陆");
            //request.getRequestDispatcher("/blog/admin/login").forward(request,response);
        }
        Long timeStamp = jwt.getClaim("timeStamp").asLong();
        //System.out.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-timeStamp));
        if(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-timeStamp) > 10){
            throw new AuthenticationException("tokenError","令牌过期，请重新登陆");
            //request.getRequestDispatcher("/blog/admin/login").forward(request,response);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
