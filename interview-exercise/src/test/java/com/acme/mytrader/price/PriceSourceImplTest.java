package com.acme.mytrader.price;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PriceSourceImplTest {

    @Test
    public void testPriceListenersAreNotifiedWhenPriceUpdateReceived() {

        BlockingQueue<SecurityPrice> priceSourceQueue = new LinkedBlockingQueue<>();
        PriceSourceImpl priceSource = new PriceSourceImpl(priceSourceQueue);

        PriceListener priceListener = Mockito.mock(PriceListener.class);
        priceSource.addPriceListener(priceListener);

        new Thread(priceSource).start();
        priceSourceQueue.offer(new SecurityPrice("IBM", 59.68));

        Mockito.verify(priceListener, Mockito.times(1)).priceUpdate("IBM", 59.68);
        Mockito.verifyZeroInteractions(priceListener);

    }

}