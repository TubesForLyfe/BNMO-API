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
import bnmo.bnmoapi.classes.saldo.SaldoTf;
import bnmo.bnmoapi.classes.saldo.SaldoTfDetail;
import bnmo.bnmoapi.classes.sql.transaction.insert.TransactionInsert;
import bnmo.bnmoapi.classes.sql.transaction.read.TransactionDetailByUsername;
import bnmo.bnmoapi.classes.sql.users.read.ExistUsername;
import bnmo.bnmoapi.classes.sql.users.read.UserRoleByToken;
import bnmo.bnmoapi.classes.sql.users.read.UserSaldoByUsername;
import bnmo.bnmoapi.classes.sql.users.read.UserUsernameByToken;
import bnmo.bnmoapi.classes.sql.users.update.UpdateSaldoByUsername;
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
        try {
            String role = db.queryForObject(new UserRoleByToken(token.value).query(), String.class);
            if (role.equals("customer")) {
                try {
                    String username = db.queryForObject(new UserUsernameByToken(token.value).query(), String.class);
                    if (username.equals(saldo.username)) {
                        return ResponseEntity.ok(new Message("Username tujuan tidak valid"));
                    }
                    try {
                        db.queryForObject(new ExistUsername(saldo.username).query(), String.class);
                        float IDR_value = 1;
                        if (!saldo.currency.equals("IDR")) {
                            String url = "http://127.0.0.1:8080/api/saldo-conversion/" + saldo.currency;
                            RestTemplate restTemplate = new RestTemplate();
                            IDR_value = restTemplate.getForObject(url, Float.class);
                        }
                        float transfer_saldo = saldo.amount * IDR_value;
                        try {
                            float my_saldo = db.queryForObject(new UserSaldoByUsername(username).query(), Float.class);
                            if (my_saldo < transfer_saldo) {
                                return ResponseEntity.ok(new Message("Saldo tidak cukup"));
                            } else {
                                my_saldo = my_saldo - transfer_saldo;
                                float other_saldo = db.queryForObject(new UserSaldoByUsername(saldo.username).query(), Float.class) + transfer_saldo;

                                db.update(new TransactionInsert(username, saldo.username, transfer_saldo).query());
                                db.update(new UpdateSaldoByUsername(my_saldo, username).query());
                                db.update(new UpdateSaldoByUsername(other_saldo, saldo.username).query());
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
        try {
            String role = db.queryForObject(new UserRoleByToken(token.value).query(), String.class);
            if (role.equals("customer")) {
                try {
                    String username = db.queryForObject(new UserUsernameByToken(token.value).query(), String.class);
                    List<SaldoTfDetail> transferHistory = db.query(new TransactionDetailByUsername(username).query(), (rs, rowNum) -> new SaldoTfDetail(
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
