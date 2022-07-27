package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserSaldoByUsername implements SQLInterface {
    public String username;

    public UserSaldoByUsername(String username) {
        this.username = username;
    }

    public String query() {
        return "SELECT saldo FROM users WHERE username = '" + this.username + "'";
    }
}
