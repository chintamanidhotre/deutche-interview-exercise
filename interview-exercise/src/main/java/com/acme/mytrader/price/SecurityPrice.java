package com.acme.mytrader.price;

public class SecurityPrice {

    private String security;
    private double price;

    public SecurityPrice(String security, double price) {
        this.security = security;
        this.price = price;
    }

    public String getSecurity() {
        return security;
    }

    public double getPrice() {
        return price;
    }
}
