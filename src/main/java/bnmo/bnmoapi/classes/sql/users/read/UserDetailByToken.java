package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserDetailByToken implements SQLInterface  {
    public String token;

    public UserDetailByToken(String token) {
        this.token = token;
    }

    public String query() {
        return "SELECT * FROM users WHERE token = '" + this.token + "'";
    }
}
