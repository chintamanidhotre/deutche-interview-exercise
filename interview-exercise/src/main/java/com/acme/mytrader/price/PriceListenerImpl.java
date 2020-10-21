package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

public class PriceListenerImpl implements PriceListener {

    private static final Logger LOGGER = Logger.getLogger(PriceListenerImpl.class.getName());
    private final ExecutionService executionService;

    private ConcurrentHashMap<String, ExecutionTriggerLevels> securityExecutionLevelMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Lock> securityExecutionLockMap = new ConcurrentHashMap<>();

    private class ExecutionTriggerLevels {
        private final double buyPrice;
        private final double sellPrice;
        private final AtomicBoolean buyTriggered;
        private final AtomicBoolean sellTriggered;

        public ExecutionTriggerLevels(double buyPrice, double sellPrice) {
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            buyTriggered = new AtomicBoolean(false);
            sellTriggered = new AtomicBoolean(false);
        }
    }

    public PriceListenerImpl(ExecutionService executionService, List<SecurityTrigger> securityTriggers) {
        this.executionService = executionService;
        for(SecurityTrigger securityTrigger : securityTriggers) {
            ExecutionTriggerLevels executionTriggerLevels = new ExecutionTriggerLevels(securityTrigger.getBuyPrice(), securityTrigger.getSellPrice());
            final String security = securityTrigger.getSecurity();
            securityExecutionLevelMap.put(security, executionTriggerLevels);
            securityExecutionLockMap.putIfAbsent(security, new ReentrantLock());

        }
    }

    @Override
    public void priceUpdate(String security, double price) {

        LOGGER.info("Received price for - " + security + " : " + price);

        final ExecutionTriggerLevels executionTriggerLevels = securityExecutionLevelMap.get(security);
        Lock lock = securityExecutionLockMap.get(security);
        lock.lock();
        try {
            if(price <= executionTriggerLevels.buyPrice && !executionTriggerLevels.buyTriggered.get()) {
                executionService.buy(security, price, VOLUME_TO_EXECUTE);
                executionTriggerLevels.buyTriggered.compareAndSet(false, true);
            }
            if(price >= executionTriggerLevels.sellPrice && !executionTriggerLevels.sellTriggered.get()) {
                executionService.sell(security, price, VOLUME_TO_EXECUTE);
                executionTriggerLevels.sellTriggered.compareAndSet(false, true);
            }
        } finally {
            lock.unlock();
        }
    }
}
