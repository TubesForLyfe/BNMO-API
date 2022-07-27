package bnmo.bnmoapi.api.customer;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import bnmo.bnmoapi.classes.message.Message;
import bnmo.bnmoapi.classes.saldo.SaldoTf;
import bnmo.bnmoapi.classes.saldo.SaldoTfDetail;
import bnmo.bnmoapi.classes.token.Token;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("api/customer")
public class SaldoTransfer {
    
    @Autowired
    private JdbcTemplate db;

    @PostMapping("/saldo-transfer")
    public ResponseEntity<?> transferSaldo(HttpServletRequest request, @RequestBody SaldoTf saldo) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "' AND verified = 'true'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("customer")) {
                sql = "SELECT username FROM users WHERE token = '" + token.value + "'";
                try {
                    String username = db.queryForObject(sql, String.class);
                    sql = "SELECT username FROM users WHERE username = '" + saldo.username + "' AND username <> '" + username + "' AND verified = 'true'";
                    try {
                        db.queryForObject(sql, String.class);
                        float IDR_value = 1;
                        if (!saldo.currency.equals("IDR")) {
                            String url = "http://127.0.0.1:8080/api/saldo-conversion/" + saldo.currency;
                            RestTemplate restTemplate = new RestTemplate();
                            IDR_value = restTemplate.getForObject(url, Float.class);
                        }
                        float transfer_saldo = saldo.amount * IDR_value;
                        sql = "SELECT saldo FROM users WHERE username = '" + username + "'";
                        try {
                            float current_saldo = db.queryForObject(sql, Float.class);
                            if (current_saldo < transfer_saldo) {
                                return ResponseEntity.ok(new Message("Saldo tidak cukup"));
                            } else {
                                sql = "INSERT INTO transaction VALUES ('" + username + "', '" + saldo.username + "', '" + transfer_saldo  + "')";
                                try {
                                    db.update(sql);
                                } catch (Exception e) {}
                                sql = "UPDATE users SET saldo = saldo - " + transfer_saldo + " WHERE username = '" + username + "'";
                                try {
                                    db.update(sql);
                                } catch (Exception e) {}
                                sql = "UPDATE users SET saldo = saldo + " + transfer_saldo + " WHERE username = '" + saldo.username + "'";
                                try {
                                    db.update(sql);
                                } catch (Exception e) {}
                                return ResponseEntity.ok("Transfer saldo ke rekening " + saldo.username + " berhasil.");
                            }
                        } catch (Exception e) {}
                    } catch (Exception e) {
                        return ResponseEntity.ok(new Message("Username tujuan tidak valid"));
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }

    @GetMapping("/saldo-transfer-history")
    public ResponseEntity<?> getRequestHistory(HttpServletRequest request) {
        Token token = new Token(request);
        String sql = "SELECT role FROM users WHERE token = '" + token.value + "' AND verified = 'true'";
        try {
            String role = db.queryForObject(sql, String.class);
            if (role.equals("customer")) {
                sql = "SELECT username FROM users WHERE token = '" + token.value + "'";
                try {
                    String username = db.queryForObject(sql, String.class);
                    sql = "SELECT * FROM transaction WHERE from_username = '" + username + "' OR to_username = '" + username + "' ORDER BY created_at DESC";
                    List<SaldoTfDetail> transferHistory = db.query(sql, (rs, rowNum) -> new SaldoTfDetail(
                        rs.getString("from_username"),
                        rs.getString("to_username"),
                        rs.getFloat("jumlah"),
                        rs.getString("created_at")
                    ));
                    return ResponseEntity.ok(transferHistory);
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }
}
