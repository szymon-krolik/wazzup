package com.wazzup.service;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


@Service
public class CookieService {
    private static final String TOKEN_COOKIE = "TOKEN";
    public static Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie(TOKEN_COOKIE, token);
        cookie.setMaxAge(3600);
        return cookie;
    }

    public static void addCookieToResponse(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    public static Cookie getCookieFromRequest(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        for (int i = 0; i < cookies.length; ++i) {
            if (name.equals(cookies[i].getName()))
                return cookies[i];
        }
        return null;
    }

    public static void killAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; ++i) {
            response.addCookie(invalidateCookie(cookies[i]));
        }
    }

    private static Cookie invalidateCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        return cookie;
    }
}
