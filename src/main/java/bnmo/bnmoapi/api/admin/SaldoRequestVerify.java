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
import bnmo.bnmoapi.classes.saldo.SaldoReqDetail;
import bnmo.bnmoapi.classes.token.Token;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("api/admin")
public class SaldoRequestVerify {
    
    @Autowired
    private JdbcTemplate db;

    @GetMapping("/unverified-saldo-request")
    public ResponseEntity<?> getUnverifiedSaldoRequest(HttpServletRequest request) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("admin")) {
                sql = "SELECT * FROM saldo_requests WHERE status = '-' ORDER BY created_at ASC";
                List<SaldoReqDetail> unverified_saldo_requests = db.query(sql, (rs, rowNum) -> new SaldoReqDetail(
                    rs.getString("username"),
                    rs.getString("type"),
                    rs.getFloat("jumlah"),
                    rs.getString("created_at"),
                    rs.getString("status"),
                    rs.getString("verified_at")
                ));
                return ResponseEntity.ok(unverified_saldo_requests);
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }

    @GetMapping("/accept-saldo-request/{username}/{created_at}")
    public ResponseEntity<?> acceptSaldoRequest(HttpServletRequest request, @PathVariable("username") String username, @PathVariable("created_at") String created_at) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("admin")) {
                sql = "SELECT jumlah FROM saldo_requests WHERE username = '" + username + "' AND created_at = '" + created_at + "'";
                try {
                    float jumlah = db.queryForObject(sql, Float.class);
                    sql = "SELECT type FROM saldo_requests WHERE username = '" + username + "' AND created_at = '" + created_at + "'";
                    String type = db.queryForObject(sql, String.class);
                    sql = "SELECT saldo FROM users WHERE username = '" + username + "'";
                    float saldo = db.queryForObject(sql, Float.class);
                    sql = "UPDATE saldo_requests SET status = 'accepted', verified_at = CURRENT_TIMESTAMP WHERE username = '" + username + "' AND created_at = '" + created_at + "'";
                    try {
                        db.update(sql);
                    } catch (Exception e) {}
                    float new_saldo = 0;
                    String message_type = "penambahan";
                    if (type.equals("tambah")) {
                        new_saldo = saldo + jumlah;
                    } else {
                        if (saldo > jumlah) {
                            new_saldo = saldo - jumlah;
                        }
                        message_type = "pengurangan";
                    }
                    sql = "UPDATE users SET saldo = " + new_saldo + " WHERE username = '" + username + "'";
                    try {
                        db.update(sql);
                    } catch (Exception e) {}
                    return ResponseEntity.ok(new Message("Request " + message_type + " saldo " + username + " sebesar " + jumlah + " berhasil diterima."));
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }

    @GetMapping("/reject-saldo-request/{username}/{created_at}")
    public ResponseEntity<?> rejectSaldoRequest(HttpServletRequest request, @PathVariable("username") String username, @PathVariable("created_at") String created_at) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("admin")) {
                sql = "SELECT jumlah FROM saldo_requests WHERE username = '" + username + "' AND created_at = '" + created_at + "'";
                try {
                    float jumlah = db.queryForObject(sql, Float.class);
                    sql = "SELECT type FROM saldo_requests WHERE username = '" + username + "' AND created_at = '" + created_at + "'";
                    String type = db.queryForObject(sql, String.class);
                    sql = "UPDATE saldo_requests SET status = 'rejected', verified_at = CURRENT_TIMESTAMP WHERE username = '" + username + "' AND created_at = '" + created_at + "'";
                    try {
                        db.update(sql);
                    } catch (Exception e) {}
                    String message_type = "penambahan";
                    if (type.equals("kurang")) {
                        message_type = "pengurangan";
                    }
                    return ResponseEntity.ok(new Message("Request " + message_type + " saldo " + username + " sebesar " + jumlah + " berhasil ditolak."));
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }
}
