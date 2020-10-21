package com.acme.mytrader.execution;

import com.acme.mytrader.price.PriceListener;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Observer;

public class ExecutionServiceImplTest {

    @Test
    public void testObserversAreNotifiedWhenBuyExecution() {

        Observer observer = Mockito.mock(Observer.class);
        ExecutionService executionService = new ExecutionServiceImpl(observer);

        executionService.buy("IBM", 55.0, PriceListener.VOLUME_TO_EXECUTE);

        Mockito.verify(observer, Mockito.times(1)).update(Mockito.any(), Mockito.any());
        Mockito.verifyNoMoreInteractions(observer);
    }

    @Test
    public void testObserversAreNotifiedWhenSellExecution() {

        Observer observer = Mockito.mock(Observer.class);
        ExecutionService executionService = new ExecutionServiceImpl(observer);

        executionService.sell("IBM", 55.0, PriceListener.VOLUME_TO_EXECUTE);

        Mockito.verify(observer, Mockito.times(1)).update(Mockito.any(), Mockito.any());
        Mockito.verifyNoMoreInteractions(observer);
    }

}