package bnmo.bnmoapi.classes.sql.users.insert;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserInsert implements SQLInterface {
    public String nama;
    public String username;
    public String password;
    public String token;

    public UserInsert(String nama, String username, String password, String token) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String query() {
        return "INSERT INTO users (nama, username, password, token) VALUES ('" + this.nama + "', '" + this.username + "', '" + this.password + "', '" + this.token + "')";
    }
}
