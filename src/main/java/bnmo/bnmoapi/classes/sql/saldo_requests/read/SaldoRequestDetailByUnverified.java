package bnmo.bnmoapi.classes.sql.saldo_requests.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class SaldoRequestDetailByUnverified implements SQLInterface {
    public SaldoRequestDetailByUnverified() {
        //
    }

    public String query() {
        return "SELECT * FROM saldo_requests WHERE status = '-' ORDER BY created_at ASC";
    }
}
