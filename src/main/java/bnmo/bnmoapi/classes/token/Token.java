package bnmo.bnmoapi.classes.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

public class Token {
    public String value;

    public Token(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            if (header.substring(0, 6).equals("Bearer")) {
                this.value = header.substring(7);
            }
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("bnmo_token")) {
                        this.value = cookie.getValue();
                        break;
                    }
                }
            }
        }
    }
}
