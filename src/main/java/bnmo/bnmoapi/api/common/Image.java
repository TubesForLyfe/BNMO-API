package bnmo.bnmoapi.api.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bnmo.bnmoapi.classes.role.Role;
import bnmo.bnmoapi.classes.sql.users.read.UserRoleByToken;
import bnmo.bnmoapi.classes.sql.users.read.UserUsernameByImage;
import bnmo.bnmoapi.classes.sql.users.read.UserUsernameByToken;
import bnmo.bnmoapi.classes.sql.users.update.UpdateImageByUsername;
import bnmo.bnmoapi.classes.token.Token;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("api/image")
public class Image {

    public static String IMAGE_ROOT_PATH = "/images/";

    @Autowired
    private JdbcTemplate db;

    @GetMapping(value = "/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(HttpServletRequest request, @PathVariable("filename") String filename) throws IOException {
        Token token = new Token(request);
        Role role = new Role(token);
        try {
            role.setPermission(db.queryForObject(new UserRoleByToken(token.value).query(), String.class));
            if (role.isCustomer()) {
                String username_token = db.queryForObject(new UserUsernameByToken(token.value).query(), String.class);
                String username_image = db.queryForObject(new UserUsernameByImage(filename).query(), String.class);
                if (!username_token.equals(username_image)) {
                    return null;
                }
            }
            return FileUtils.readFileToByteArray(new File("customer-photos/" + filename));
        } catch (Exception e) {}
        return null;
    }

    @PostMapping("/{username}/upload")
    public void uploadImage(@RequestParam MultipartFile file, @PathVariable("username") String username) {
        try {
            Path uploadPath = Paths.get("customer-photos");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (Exception e) {}
        
        String filename = username + "-" + file.getOriginalFilename();
        try {
            File path = new File("customer-photos/" + filename);
            OutputStream os = new FileOutputStream(path);
            os.write(file.getBytes());
            os.close();
            try {
                db.update(new UpdateImageByUsername(filename, username).query());
            } catch (Exception e) {}
        } catch (Exception e) {}
    }
}
