package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.Execution;
import com.acme.mytrader.execution.Side;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.SecurityPrice;
import com.acme.mytrader.price.SecurityTrigger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TradingStrategyTest {

    @Test
    public void testNoneMatchingExecution() throws InterruptedException{

        List<Execution> expectedExecutions = new ArrayList<>();

        SecurityTrigger securityTrigger = new SecurityTrigger("IBM", 50.0, 100.0);
        List<SecurityTrigger> securityTriggers = new ArrayList<>();
        securityTriggers.add(securityTrigger);

        BlockingQueue<SecurityPrice> prices = getSecurityPrices();
        executeTest(expectedExecutions, securityTriggers, prices);
    }

    @Test
    public void testMatchingExecution() throws InterruptedException{


        List<Execution> expectedExecutions = new ArrayList<>();
        Execution expectedExecution1 = new Execution("IBM", 54.23, PriceListener.VOLUME_TO_EXECUTE, Side.Buy);
        Execution expectedExecution2 = new Execution("IBM", 95.23, PriceListener.VOLUME_TO_EXECUTE, Side.Sell);
        expectedExecutions.add(expectedExecution1);
        expectedExecutions.add(expectedExecution2);

        SecurityTrigger securityTrigger = new SecurityTrigger("IBM", 55.0, 95.0);
        List<SecurityTrigger> securityTriggers = new ArrayList<>();
        securityTriggers.add(securityTrigger);

        BlockingQueue<SecurityPrice> prices = getSecurityPrices();

        executeTest(expectedExecutions, securityTriggers, prices);
    }

    private BlockingQueue<SecurityPrice> getSecurityPrices() {
        BlockingQueue<SecurityPrice> prices = new LinkedBlockingQueue<>();
        prices.offer(new SecurityPrice("IBM", 60.23));
        prices.offer(new SecurityPrice("IBM", 61.24));
        prices.offer(new SecurityPrice("IBM", 54.23));
        prices.offer(new SecurityPrice("IBM", 53.23));
        prices.offer(new SecurityPrice("IBM", 55.23));
        prices.offer(new SecurityPrice("IBM", 85.23));
        prices.offer(new SecurityPrice("IBM", 95.23));
        prices.offer(new SecurityPrice("IBM", 56.23));
        prices.offer(new SecurityPrice("IBM", 52.23));
        return prices;
    }

    private void executeTest(List<Execution> expectedExecutions, List<SecurityTrigger> securityTriggers, BlockingQueue<SecurityPrice> prices) throws InterruptedException {
        TradingStrategy tradingStrategy = new TradingStrategy(prices, securityTriggers);
        Thread.sleep(5000);
        final List<Execution> executions = tradingStrategy.getExecutions();

        assertNotNull(executions);
        assertEquals(expectedExecutions.size(), executions.size());

        for (int i = 0; i < expectedExecutions.size(); i++) {
            assertEquals(expectedExecutions.get(i).getSecurity(), executions.get(i).getSecurity());
            assertEquals(expectedExecutions.get(i).getPrice(), executions.get(i).getPrice(), 0.0d);
            assertEquals(expectedExecutions.get(i).getQuantity(), executions.get(i).getQuantity());
            assertEquals(expectedExecutions.get(i).getSide(), executions.get(i).getSide());

        }
    }


}
