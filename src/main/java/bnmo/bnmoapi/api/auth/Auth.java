package bnmo.bnmoapi.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bnmo.bnmoapi.classes.message.Message;
import bnmo.bnmoapi.classes.users.User;
import bnmo.bnmoapi.classes.users.UserLogin;
import bnmo.bnmoapi.classes.users.UserRegister;
import io.jsonwebtoken.*;

@RestController
@CrossOrigin
@RequestMapping("api")
public class Auth {

    @Autowired
    private JdbcTemplate db;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRegister user) {	
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hash_password = passwordEncoder.encode(user.password);
		String token = Jwts.builder().setSubject(user.username).compact();
		String sql = "INSERT INTO users (nama, username, password, token) VALUES ('" + user.nama + "', '" + user.username + "', '" + hash_password + "', '" + token + "')";
		try {
			db.update(sql);
			return ResponseEntity.ok("Berhasil dimasukkan ke database.");
		} catch (Exception e) {
			return ResponseEntity.ok(new Message("Username tidak valid."));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLogin user) {	
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String sql = "SELECT * FROM users WHERE username = '" + user.username + "'";
		try {
			User user_db = db.queryForObject(sql, (rs, rowNum) -> new User (
				rs.getString("nama"), 
				rs.getString("username"), 
				rs.getString("password"), 
				rs.getString("image"), 
				rs.getString("role"),
				rs.getString("token"), 
				rs.getString("verified"),
				rs.getFloat("saldo")
			));
			if (passwordEncoder.matches(user.password, user_db.password)) {
				if (user_db.role.equals("admin") || (user_db.role.equals("customer") && user_db.verified.equals("true"))) {
					return ResponseEntity.ok(user_db);
				} else {
					return ResponseEntity.ok(new Message("Login gagal. Akun Anda belum diverifikasi."));
				}
			} else {
				return ResponseEntity.ok(new Message("Username atau password tidak valid."));
			}
		} catch (Exception e) {
			return ResponseEntity.ok(new Message("Username atau password tidak valid."));
		}
	}
}
