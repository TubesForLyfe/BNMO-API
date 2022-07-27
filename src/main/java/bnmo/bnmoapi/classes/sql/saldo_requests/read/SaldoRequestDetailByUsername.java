package bnmo.bnmoapi.classes.sql.saldo_requests.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class SaldoRequestDetailByUsername implements SQLInterface {
    public String username;

    public SaldoRequestDetailByUsername(String username) {
        this.username = username;
    }

    public String query() {
        return "SELECT * FROM saldo_requests WHERE username = '" + this.username + "' ORDER BY status ASC, created_at DESC";
    }
}
