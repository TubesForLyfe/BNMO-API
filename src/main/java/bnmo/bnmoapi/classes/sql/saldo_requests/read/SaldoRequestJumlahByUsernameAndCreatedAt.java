package bnmo.bnmoapi.classes.sql.saldo_requests.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class SaldoRequestJumlahByUsernameAndCreatedAt implements SQLInterface {
    public String username;
    public String created_at;

    public SaldoRequestJumlahByUsernameAndCreatedAt(String username, String created_at) {
        this.username = username;
        this.created_at = created_at;
    }

    public String query() {
        return "SELECT jumlah FROM saldo_requests WHERE username = '" + this.username + "' AND created_at = '" + this.created_at + "'";
    }
}
