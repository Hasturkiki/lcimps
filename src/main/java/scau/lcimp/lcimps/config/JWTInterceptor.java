package scau.lcimp.lcimps.config;

import org.jetbrains.annotations.NotNull;
import scau.lcimp.lcimps.utils.JWTUtils;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt拦截器
 **/
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        Map<String, Object> map = new HashMap<>();
        /* 获取Cookie中lcimp-token */
        String token = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            System.out.println("cookies is null");
            return false;
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lcimp-token"))
                    token = cookie.getValue();
            }
        }
        if (token == null) {
            System.out.println("token is null");
            return false;
        }

        try {
            JWTUtils.verify(token); //  验证令牌
            return true;    //  放行请求
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "token算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
        }
        map.put("success", false);  //  设置状态
        //将map 转为json
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json); //  返回给前端结果
        return false;
    }
}
