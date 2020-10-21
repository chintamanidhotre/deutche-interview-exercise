package com.acme.mytrader.price;

public class SecurityTrigger {
    private String security;
    private double buyPrice;
    private double sellPrice;

    public SecurityTrigger(String security, double buyPrice, double sellPrice) {
        this.security = security;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getSecurity() {
        return security;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
