package bnmo.bnmoapi.classes.saldo;

public class SaldoTfDetail {
    public String from_username;
    public String to_username;
    public float jumlah;
    public String created_at;

    public SaldoTfDetail(String from_username, String to_username, float jumlah, String created_at) {
        this.from_username = from_username;
        this.to_username = to_username;
        this.jumlah = jumlah;
        this.created_at = created_at;
    }
}
