package bnmo.bnmoapi.classes.saldo;

public class SaldoTf {
    public String username;
    public float amount;
    public String currency;

    public SaldoTf(String username, float amount, String currency) {
        this.username = username;
        this.amount = amount;
        this.currency = currency;
    }
}
