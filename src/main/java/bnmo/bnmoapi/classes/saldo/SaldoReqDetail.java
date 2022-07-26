package bnmo.bnmoapi.classes.saldo;

public class SaldoReqDetail {
    public String username;
    public String type;
    public float jumlah;
    public String created_at;
    public String status;
    public String verified_at;

    public SaldoReqDetail(String username, String type, float jumlah, String created_at, String status, String verified_at) {
        this.username = username;
        this.type = type;
        this.jumlah = jumlah;
        this.created_at = created_at;
        this.status = status;
        this.verified_at = verified_at;
    }
}
