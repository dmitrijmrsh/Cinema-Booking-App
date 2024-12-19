package cinema.management.app.screeningservice.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class JwtUtil {

    private static final String BEARER = "Bearer ";

    public static String getTokenFromHttpRequest(HttpServletRequest httpRequest) {
       String token;
       Cookie authCookie = null;
       Cookie[] cookies = httpRequest.getCookies();

       for (var cookie : cookies) {
           if (cookie.getName().equals("AuthToken")) {
               authCookie = cookie;
           }
       }

        if (authCookie != null && !authCookie.getValue().isEmpty()) {
            token = authCookie.getValue();
        } else {
            token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        }

        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(BEARER.length());
        }

        return token;
    }

}
