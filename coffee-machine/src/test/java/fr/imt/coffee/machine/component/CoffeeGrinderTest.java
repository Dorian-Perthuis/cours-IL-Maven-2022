package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class CoffeeGrinderTest {

    @Test
    public void testWaterPumping() throws InterruptedException {

        int grindingTimeExpected = 2;
        CoffeeGrinder coffeeGrinder = new CoffeeGrinder(grindingTimeExpected);
        BeanTank beanTank = Mockito.mock(BeanTank.class);
        double grindingTimeActual = coffeeGrinder.grindCoffee(beanTank);

        Assertions.assertEquals(grindingTimeExpected, grindingTimeActual);
    }
}
