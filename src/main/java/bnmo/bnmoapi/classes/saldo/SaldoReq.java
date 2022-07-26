package bnmo.bnmoapi.classes.saldo;

public class SaldoReq {
    public String type;
    public float amount;
    public String currency;

    public SaldoReq(String type, float amount, String currency) {
        this.type = type;
        this.amount = amount;
        this.currency = currency;
    }
}
