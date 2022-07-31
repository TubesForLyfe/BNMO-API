package bnmo.bnmoapi.api.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import bnmo.bnmoapi.classes.role.Role;
import bnmo.bnmoapi.classes.saldo.SaldoReq;
import bnmo.bnmoapi.classes.saldo.SaldoReqDetail;
import bnmo.bnmoapi.classes.sql.saldo_requests.insert.SaldoRequestInsert;
import bnmo.bnmoapi.classes.sql.saldo_requests.read.SaldoRequestDetailByUsername;
import bnmo.bnmoapi.classes.sql.users.read.UserRoleByToken;
import bnmo.bnmoapi.classes.sql.users.read.UserUsernameByToken;
import bnmo.bnmoapi.classes.token.Token;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://bnmo.herokuapp.com"}, allowCredentials = "true")
@RequestMapping("api/customer")
public class SaldoRequest {
    
    @Autowired
    private JdbcTemplate db;

    @PostMapping("/saldo-request")
    public ResponseEntity<?> requestSaldo(HttpServletRequest request, @RequestBody SaldoReq saldo) {
        Token token = new Token(request);
        Role role = new Role(token);
        try {
            role.setPermission(db.queryForObject(new UserRoleByToken(token.value).query(), String.class));
            if (role.isCustomer()) {
                try {
                    String username = db.queryForObject(new UserUsernameByToken(token.value).query(), String.class);
                    float IDR_value = 1;
                    if (!saldo.currency.equals("IDR")) {
                        String url = "http://127.0.0.1:8080/api/saldo-conversion/" + saldo.currency;
                        RestTemplate restTemplate = new RestTemplate();
                        IDR_value = restTemplate.getForObject(url, Float.class);
                    }
                    try {
                        db.update(new SaldoRequestInsert(username, saldo.type, saldo.amount * IDR_value).query());
                    } catch (Exception e) {}
                    return ResponseEntity.ok(new Message("Request saldo berhasil. Permintaan Anda akan segera diverifikasi."));
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }

    @GetMapping("/saldo-request-history")
    public ResponseEntity<?> getRequestHistory(HttpServletRequest request) {
        Token token = new Token(request);
        Role role = new Role(token);
        try {
            role.setPermission(db.queryForObject(new UserRoleByToken(token.value).query(), String.class));
            if (role.isCustomer()) {
                try {
                    String username = db.queryForObject(new UserUsernameByToken(token.value).query(), String.class);
                    List<SaldoReqDetail> requestHistory = db.query(new SaldoRequestDetailByUsername(username).query(), (rs, rowNum) -> new SaldoReqDetail(
                        rs.getString("username"),
                        rs.getString("type"),
                        rs.getFloat("jumlah"),
                        rs.getString("created_at"),
                        rs.getString("status"),
                        rs.getString("verified_at")
                    ));
                    return ResponseEntity.ok(requestHistory);
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }
}
