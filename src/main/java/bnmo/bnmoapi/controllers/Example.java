package bnmo.bnmoapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class Example {

    @Autowired
    private JdbcTemplate db;

	@GetMapping("")
	public ResponseEntity<String> get() {
        String sql = "INSERT INTO users (nama, username, password, image, role, token, verified) VALUES ('admin', 'admin', 'admin-bnmo', '-', 'admin', 'adminmahbebas', 'true')";
        
        try {
            int rows = db.update(sql);
            System.out.println(rows);
        } catch (Exception e) {
            return ResponseEntity.ok("Woi");
        }
		return ResponseEntity.ok("A new row has been inserted.");
	}
}