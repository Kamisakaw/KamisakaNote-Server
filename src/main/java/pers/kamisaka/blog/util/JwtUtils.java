package pers.kamisaka.blog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import pers.kamisaka.blog.entity.User;

public class JwtUtil {
    public static String generateJwt(User user){
        String token = "";
        token = JWT.create().withAudience(user.getUid())
                .withClaim("timeStamp",System.currentTimeMillis())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
