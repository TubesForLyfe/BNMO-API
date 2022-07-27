package bnmo.bnmoapi.api.admin;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bnmo.bnmoapi.classes.message.Message;
import bnmo.bnmoapi.classes.token.Token;
import bnmo.bnmoapi.classes.users.UserInfo;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("api/admin")
public class AccountVerify {
    
    @Autowired
    private JdbcTemplate db;

    @GetMapping("/unverified-customer")
    public ResponseEntity<?> getUnverifiedCustomer(HttpServletRequest request) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("admin")) {
                sql = "SELECT * FROM users WHERE verified = 'false'";
                List<UserInfo> unverified_users = db.query(sql, (rs, rowNum) -> new UserInfo(
                    rs.getString("nama"),
                    rs.getString("username"),
                    rs.getString("image"),
                    rs.getFloat("saldo")
                ));
                return ResponseEntity.ok(unverified_users);
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }

    @GetMapping("/verify-customer/{username}")
    public ResponseEntity<?> verifyCustomer(HttpServletRequest request, @PathVariable("username") String username) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("admin")) {
                sql = "UPDATE users SET verified = 'true' WHERE username = '" + username + "'";
                try {
                    db.update(sql);
                } catch (Exception e) {}
                return ResponseEntity.ok(new Message("Customer " + username + " berhasil diverifikasi."));
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }
}
