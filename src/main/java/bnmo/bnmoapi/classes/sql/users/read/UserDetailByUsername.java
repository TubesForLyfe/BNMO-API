package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserDetailByUsername implements SQLInterface {
    public String username;

    public UserDetailByUsername(String username) {
        this.username = username;
    }

    public String query() {
        return "SELECT * FROM users WHERE username = '" + this.username + "'";
    }
}
