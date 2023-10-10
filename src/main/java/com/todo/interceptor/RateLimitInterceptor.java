package com.todo.interceptor;

import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.todo.common.R;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    // 每秒钟最多处理10个请求
    private final double permitsPerSecond = 10;
    private final RateLimiter limiter = RateLimiter.create(permitsPerSecond);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (limiter.tryAcquire(1, 1, TimeUnit.SECONDS)) {
            // 令牌桶中有足够的令牌，放行请求
            return true;
        } else {
            // 令牌桶中没有足够的令牌，限流处理
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONUtil.toJsonStr(R.error("rate limit exceeded")));
            return false;
        }
    }
}
