package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserRoleByToken implements SQLInterface {
    public String token;

    public UserRoleByToken(String token) {
        this.token = token;
    }

    public String query() {
        return "SELECT role FROM users WHERE token = '" + this.token + "' AND verified = 'true'";
    }
}
