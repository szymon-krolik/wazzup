package com.wazzup.config;


import com.wazzup.service.JwtTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;




/**
 * @author Szymon Królik
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwToken = request.getHeader("Authorization");

        if (jwToken == null || !jwToken.startsWith("Bearer "))
            return true;

        String newJwToken = JwtTokenService.updateJwToken(jwToken);
        response.setHeader("Authorization", newJwToken);
        response.setHeader("Access-control-expose-headers", "Authorization");

        return true;
    }
}
