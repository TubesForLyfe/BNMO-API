package bnmo.bnmoapi.classes.sql.saldo_requests.insert;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class SaldoRequestInsert implements SQLInterface {
    public String username;
    public String type;
    public float amount;

    public SaldoRequestInsert(String username, String type, float amount) {
        this.username = username;
        this.type = type;
        this.amount = amount;
    }

    public String query() {
        return "INSERT INTO saldo_requests VALUES ('" + this.username + "', '" + this.type + "', '" + this.amount + "')";
    }
}
