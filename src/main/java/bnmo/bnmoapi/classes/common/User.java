package bnmo.bnmoapi.classes.common;

public class User {
    public String nama;
    public String username;
    public String password;
    public String image;
    public String role;
    public String token;
    public String verified;

    public User(String nama, String username, String password, String image, String role, String token, String verified) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.image = image;
        this.role = role;
        this.token = token;
        this.verified = verified;
    }
}
