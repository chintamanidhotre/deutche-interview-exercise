package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class PriceListenerImplTest {

    @Test
    public void testNoExecutionWhenPriceOutOfTriggerRange() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 65);
        Mockito.verifyZeroInteractions(executionService);
    }

    @Test
    public void testBuyExecutionWhenExactBuyPriceTriggerReached() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 55);
        Mockito.verify(executionService, Mockito.times(1)).buy("IBM", 55.0, PriceListener.VOLUME_TO_EXECUTE);
        Mockito.verifyNoMoreInteractions(executionService);
    }

    @Test
    public void testBuyExecutionWhenLowerBuyPriceTriggerReached() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 54.9);
        Mockito.verify(executionService, Mockito.times(1)).buy("IBM", 54.9, PriceListener.VOLUME_TO_EXECUTE);
        Mockito.verifyNoMoreInteractions(executionService);
    }

    @Test
    public void testSellExecutionWhenExactSellPriceTriggerReached() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 95);
        Mockito.verify(executionService, Mockito.times(1)).sell("IBM", 95.0, PriceListener.VOLUME_TO_EXECUTE);
        Mockito.verifyNoMoreInteractions(executionService);
    }

    @Test
    public void testSellExecutionWhenGreaterSellPriceTriggerReached() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 96);
        Mockito.verify(executionService, Mockito.times(1)).sell("IBM", 96.0, PriceListener.VOLUME_TO_EXECUTE);
        Mockito.verifyNoMoreInteractions(executionService);
    }

    @Test
    public void testNoMoreBuyExecutionsOnceBuyIsExecuted() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 55);
        Mockito.verify(executionService, Mockito.times(1)).buy("IBM", 55.0, PriceListener.VOLUME_TO_EXECUTE);
        Mockito.verifyNoMoreInteractions(executionService);

        priceListener.priceUpdate("IBM", 54);
        Mockito.verifyNoMoreInteractions(executionService);
    }

    @Test
    public void testNoMoreSellExecutionsOnceSellIsExecuted() {

        List<SecurityTrigger> securityTriggers = getSecurityTriggers();

        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        PriceListener priceListener = new PriceListenerImpl(executionService, securityTriggers);
        priceListener.priceUpdate("IBM", 96);
        Mockito.verify(executionService, Mockito.times(1)).sell("IBM", 96.0, PriceListener.VOLUME_TO_EXECUTE);
        Mockito.verifyNoMoreInteractions(executionService);

        priceListener.priceUpdate("IBM", 97);
        Mockito.verifyNoMoreInteractions(executionService);
    }

    private List<SecurityTrigger> getSecurityTriggers() {
        List<SecurityTrigger> securityTriggers = new ArrayList<>();
        SecurityTrigger securityTrigger1 = new SecurityTrigger("IBM", 55.0, 95.0);
        SecurityTrigger securityTrigger2 = new SecurityTrigger("VOD", 65.0, 75.0);
        securityTriggers.add(securityTrigger1);
        securityTriggers.add(securityTrigger2);
        return securityTriggers;
    }

}