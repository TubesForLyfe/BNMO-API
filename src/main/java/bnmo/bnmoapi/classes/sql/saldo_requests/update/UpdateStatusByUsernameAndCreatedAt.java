package bnmo.bnmoapi.classes.sql.saldo_requests.update;

public class UpdateStatusByUsernameAndCreatedAt {
    public String status;
    public String username;
    public String created_at;

    public UpdateStatusByUsernameAndCreatedAt(String status, String username, String created_at) {
        this.status = status;
        this.username = username;
        this.created_at = created_at;
    }

    public String query() {
        return "UPDATE saldo_requests SET status = '" + this.status + "', verified_at = CURRENT_TIMESTAMP WHERE username = '" + this.username + "' AND created_at = '" + this.created_at + "'";
    }
}
