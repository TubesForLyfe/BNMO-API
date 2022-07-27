package bnmo.bnmoapi.classes.sql.users.update;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UpdateSaldoByUsername implements SQLInterface {
    public float saldo;
    public String username;

    public UpdateSaldoByUsername(float saldo, String username) {
        this.saldo = saldo;
        this.username = username;
    }

    public String query() {
        return "UPDATE users SET saldo = " + this.saldo + " WHERE username = '" + this.username + "'";
    }
}
