package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class ExistUsername implements SQLInterface {
    public String username;

    public ExistUsername(String username) {
        this.username = username;
    }

    public String query() {
        return "SELECT username FROM users WHERE username = '" + this.username + "' AND verified = 'true'";
    }
}
