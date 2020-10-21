package com.acme.mytrader.execution;

public class Execution {
    private String security;
    private double price;
    private int quantity;
    private Side side;

    public Execution(String security, double price, int quantity, Side side) {
        this.security = security;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public String getSecurity() {
        return security;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Side getSide() {
        return side;
    }

    public String toString() {
        return new StringBuffer().append("Security:") .append(security)
                                .append(", Side:") .append(side)
                                .append(", price:") .append(price)
                                .append(", Quantity:") .append(quantity)
                                .toString();
    }
}
