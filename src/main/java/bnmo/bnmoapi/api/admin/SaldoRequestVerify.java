package bnmo.bnmoapi.api.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bnmo.bnmoapi.classes.message.Message;
import bnmo.bnmoapi.classes.role.Role;
import bnmo.bnmoapi.classes.saldo.SaldoReqDetail;
import bnmo.bnmoapi.classes.sql.saldo_requests.read.SaldoRequestDetailByUnverified;
import bnmo.bnmoapi.classes.sql.saldo_requests.read.SaldoRequestJumlahByUsernameAndCreatedAt;
import bnmo.bnmoapi.classes.sql.saldo_requests.read.SaldoRequestTypeByUsernameAndCreatedAt;
import bnmo.bnmoapi.classes.sql.saldo_requests.update.UpdateStatusByUsernameAndCreatedAt;
import bnmo.bnmoapi.classes.sql.users.read.UserRoleByToken;
import bnmo.bnmoapi.classes.sql.users.read.UserSaldoByUsername;
import bnmo.bnmoapi.classes.sql.users.update.UpdateSaldoByUsername;
import bnmo.bnmoapi.classes.token.Token;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://bnmo.herokuapp.com"}, allowCredentials = "true")
@RequestMapping("api/admin")
public class SaldoRequestVerify {
    
    @Autowired
    private JdbcTemplate db;

    @GetMapping("/unverified-saldo-request")
    public ResponseEntity<?> getUnverifiedSaldoRequest(HttpServletRequest request) {
        Token token = new Token(request);
        Role role = new Role(token);
        try {
            role.setPermission(db.queryForObject(new UserRoleByToken(token.value).query(), String.class));
            if (role.isAdmin()) {
                List<SaldoReqDetail> unverified_saldo_requests = db.query(new SaldoRequestDetailByUnverified().query(), (rs, rowNum) -> new SaldoReqDetail(
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
        Role role = new Role(token);
        try {
            role.setPermission(db.queryForObject(new UserRoleByToken(token.value).query(), String.class));
            if (role.isAdmin()) {
                try {
                    float jumlah = db.queryForObject(new SaldoRequestJumlahByUsernameAndCreatedAt(username, created_at).query(), Float.class);
                    String type = db.queryForObject(new SaldoRequestTypeByUsernameAndCreatedAt(username, created_at).query(), String.class);
                    float saldo = db.queryForObject(new UserSaldoByUsername(username).query(), Float.class);
                    db.update(new UpdateStatusByUsernameAndCreatedAt("accepted", username, created_at).query());
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
                    db.update(new UpdateSaldoByUsername(new_saldo, username).query());
                    return ResponseEntity.ok(new Message("Request " + message_type + " saldo " + username + " sebesar " + jumlah + " berhasil diterima."));
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return ResponseEntity.ok(new Message("Unauthorized"));
    }

    @GetMapping("/reject-saldo-request/{username}/{created_at}")
    public ResponseEntity<?> rejectSaldoRequest(HttpServletRequest request, @PathVariable("username") String username, @PathVariable("created_at") String created_at) {
        Token token = new Token(request);
        Role role = new Role(token);
        try {
            role.setPermission(db.queryForObject(new UserRoleByToken(token.value).query(), String.class));
            if (role.isAdmin()) {
                try {
                    float jumlah = db.queryForObject(new SaldoRequestJumlahByUsernameAndCreatedAt(username, created_at).query(), Float.class);
                    String type = db.queryForObject(new SaldoRequestTypeByUsernameAndCreatedAt(username, created_at).query(), String.class);
                    db.update(new UpdateStatusByUsernameAndCreatedAt("rejected", username, created_at).query());
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
