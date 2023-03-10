package scau.lcimp.lcimps.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {

    /**
     * token_secret 密钥
     */
    private static final String SECRET = "LCIMP_2023";

    /**
     * 生成token  header.payload.sing
     */
    public static String getToken(Map<String, String> map) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);//默认7天过期
        //  创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        //  payload
        map.forEach(builder::withClaim);
        String token;
        token = builder
                .withExpiresAt(instance.getTime())  //  指定令牌过期时间
                .sign(Algorithm.HMAC256(SECRET));   //  sign
        return token;
    }

    /**
     * 验证token 合法性
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

}
