package bnmo.bnmoapi.api.customer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bnmo.bnmoapi.classes.message.Message;
import bnmo.bnmoapi.classes.users.UserInfo;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("api/customer")
public class Profile {
    
    @Autowired
    private JdbcTemplate db;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String token = "";
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            if (header.substring(0, 6).equals("Bearer")) {
                token = header.substring(7);
            }
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("bnmo_token")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            } else {
                return ResponseEntity.ok(new Message("Unauthorized"));
            }
        }
        
        String sql = "SELECT role FROM users WHERE token = '" + token + "'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("customer")) {
                sql = "SELECT * FROM users WHERE token = '" + token + "'";
                UserInfo user = db.queryForObject(sql, (rs, rowNum) -> new UserInfo(
                    rs.getString("nama"),
                    rs.getString("username"),
                    rs.getString("image"),
                    rs.getFloat("saldo")
                ));
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }
}
