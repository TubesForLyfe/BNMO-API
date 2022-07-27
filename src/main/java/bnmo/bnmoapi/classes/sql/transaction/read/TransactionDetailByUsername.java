package bnmo.bnmoapi.classes.sql.transaction.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class TransactionDetailByUsername implements SQLInterface {
    public String username;

    public TransactionDetailByUsername(String username) {
        this.username = username;
    }
    
    public String query() {
        return "SELECT * FROM transaction WHERE from_username = '" + this.username + "' OR to_username = '" + this.username + "' ORDER BY created_at DESC";
    }
}
