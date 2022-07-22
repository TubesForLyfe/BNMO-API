package bnmo.bnmoapi.api.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
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

@RestController
@CrossOrigin
@RequestMapping("api/image")
public class Image {

    public static String IMAGE_ROOT_PATH = "/images/";

    @Autowired
    private JdbcTemplate db;

    @GetMapping(value = "/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("filename") String filename) throws IOException {
        return IOUtils.toByteArray(getClass().getResourceAsStream(IMAGE_ROOT_PATH + filename));
    }

    @PostMapping("/{username}/upload")
    public void uploadImage(@RequestParam MultipartFile file, @PathVariable("username") String username) {
        String filename = username + "-" + file.getOriginalFilename();
        try {
            File path = new File("src/main/resources/images/" + filename);
            OutputStream os = new FileOutputStream(path);
            os.write(file.getBytes());
            os.close();
            String sql = "UPDATE users SET image = '" + filename + "' WHERE username = '" + username + "'";
            try {
                db.update(sql);
            } catch (Exception e) {}
        } catch (Exception e) {}
    }
}
