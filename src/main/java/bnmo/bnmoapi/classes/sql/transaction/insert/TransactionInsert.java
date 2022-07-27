package bnmo.bnmoapi.classes.sql.transaction.insert;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class TransactionInsert implements SQLInterface {
    public String from_username;
    public String to_username;
    public float amount;

    public TransactionInsert(String from_username, String to_username, float amount) {
        this.from_username = from_username;
        this.to_username = to_username;
        this.amount = amount;
    }

    public String query() {
        return "INSERT INTO transaction VALUES ('" + this.from_username+ "', '" + this.to_username + "', '" + this.amount  + "')";
    }
}
