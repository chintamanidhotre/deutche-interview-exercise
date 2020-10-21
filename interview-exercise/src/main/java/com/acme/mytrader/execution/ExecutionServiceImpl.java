package com.acme.mytrader.execution;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

public class ExecutionServiceImpl extends Observable implements ExecutionService {

    private static final Logger LOGGER = Logger.getLogger(ExecutionServiceImpl.class.getName());

    public ExecutionServiceImpl(Observer observer) {
        this.addObserver(observer);
    }

    @Override
    public void buy(String security, double price, int volume) {
        executeOrder(new Execution(security, price, volume, Side.Buy));
    }

    @Override
    public void sell(String security, double price, int volume) {
        executeOrder(new Execution(security, price, volume, Side.Sell));
    }

    private void executeOrder(Execution execution) {
        LOGGER.info("Executing : " + execution);
        setChanged();
        notifyObservers(execution);
    }
}
