package com.acme.mytrader.price;

public interface PriceListener {
    int VOLUME_TO_EXECUTE = 100;
    void priceUpdate(String security, double price);
}
