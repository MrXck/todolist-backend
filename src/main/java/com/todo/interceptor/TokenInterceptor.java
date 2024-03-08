package com.todo.interceptor;

import cn.hutool.json.JSONUtil;
import com.todo.common.R;
import com.todo.utils.JwtUtils;
import com.todo.utils.NoAuthorization;
import com.todo.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于统一校验token的有效性，如果token有效就将userId存储到本地线程中，否则响应401
 *
 * @author xck
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            // 没有匹配到Controller中的方法
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //判断该方法是否是@NoAuthorization请求
        if (handlerMethod.hasMethodAnnotation(NoAuthorization.class)) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            try {
                String userId = jwtUtils.checkToken(token);
                UserThreadLocal.set(Long.valueOf(userId));
                Map<String, Object> claims = new HashMap<>(16);
                claims.put("userId", UserThreadLocal.get());
                response.setHeader("Authorization", jwtUtils.createToken(claims, 720));
                return true;
            } catch (Exception ignored) {

            }
        }
        //给客户端响应401状态码
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(R.error("请登录")));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //把本地线程中的用户id删除
        UserThreadLocal.remove();
    }

}
