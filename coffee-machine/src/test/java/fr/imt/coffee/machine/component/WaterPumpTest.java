package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class WaterPumpTest {

    double computeWaterPumpingTime(double waterVolume, double pumpingCapacity){
        return (waterVolume / pumpingCapacity) * 1000 * 2;
    }

    /**
     * Test permettant de vérifier le fonctionnement de la résistance électrique dans un cas nominal avec JUnit
     * @throws InterruptedException
     */
    @Test
    public void testWaterPumping() throws InterruptedException {
        double pumpingCapacity = 700;
        double waterVolume = 0.15;

        double pumpingTimeExpected = computeWaterPumpingTime(waterVolume, pumpingCapacity);

        WaterTank waterTank = Mockito.mock(WaterTank.class);
        WaterPump waterPump = new WaterPump(pumpingCapacity);
        double pumpingTimeActual = waterPump.pumpWater(waterVolume,waterTank);

        Assertions.assertEquals(pumpingTimeExpected, pumpingTimeActual);
    }
}
