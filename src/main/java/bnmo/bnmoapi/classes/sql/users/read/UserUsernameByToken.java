package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserUsernameByToken implements SQLInterface {
    public String token;

    public UserUsernameByToken(String token) {
        this.token = token;
    }

    public String query() {
        return "SELECT username FROM users WHERE token = '" + this.token + "'";
    }
}
