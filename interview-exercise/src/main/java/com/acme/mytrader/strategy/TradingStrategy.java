package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.Execution;
import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.execution.ExecutionServiceImpl;
import com.acme.mytrader.price.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy implements Observer {

    private static final Logger LOGGER = Logger.getLogger(TradingStrategy.class.getName());
    private List<Execution> executionList = new ArrayList<>();

    public TradingStrategy(BlockingQueue<SecurityPrice> pricesQueue, List<SecurityTrigger> securityTriggers) {
        final ExecutionService executionService = new ExecutionServiceImpl(this) ;
        final PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        final PriceSourceImpl priceSource = new PriceSourceImpl(pricesQueue);
        priceSource.addPriceListener(priceListener);
        new Thread(priceSource).start();
    }

    @Override
    public void update(Observable o, Object arg) {
        final Execution execution = (Execution) arg;
        LOGGER.info("************ Executed security : " + execution);
        executionList.add(execution);
    }

    public List<Execution> getExecutions() {
        return executionList;
    }
}
