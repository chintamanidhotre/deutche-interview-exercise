package com.acme.mytrader.price;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PriceSourceImpl implements PriceSource, Runnable {

    private static final Logger LOGGER = Logger.getLogger(PriceSourceImpl.class.getName());
    private List<PriceListener> priceListeners = Collections.synchronizedList(new ArrayList<>());
    private BlockingQueue<SecurityPrice> securityPrices;

    public PriceSourceImpl(BlockingQueue<SecurityPrice> securityPrices) {
        this.securityPrices = securityPrices;
    }

    @Override
    public void addPriceListener(PriceListener listener) {
        priceListeners.add(listener);
    }

    @Override
    public void removePriceListener(PriceListener listener) {
        priceListeners.remove(listener);
    }

    @Override
    public void run() {
        try {
            while(true) {
                notifyListeners(securityPrices.take());
            }
        } catch (InterruptedException e) {
            LOGGER.error("Exception occurred" , e);
        }
    }

    private void notifyListeners(SecurityPrice securityPrice) {
        synchronized (priceListeners) {
            for(PriceListener priceListener : priceListeners) {
                priceListener.priceUpdate(securityPrice.getSecurity(), securityPrice.getPrice());
            }
        }
    }
}
