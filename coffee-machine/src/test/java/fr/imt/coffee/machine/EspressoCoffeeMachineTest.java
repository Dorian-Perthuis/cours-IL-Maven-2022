package fr.imt.coffee.machine;

import fr.imt.coffee.machine.exception.*;
import fr.imt.cours.storage.cupboard.coffee.type.CoffeeType;
import fr.imt.cours.storage.cupboard.container.*;
import fr.imt.cours.storage.cupboard.exception.CupNotEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EspressoCoffeeMachineTest {

    public EspressoCoffeeMachine espressoCoffeeMachineTest;

    @BeforeEach
    public void beforeTest(){
        espressoCoffeeMachineTest = new EspressoCoffeeMachine(
                0,10,
                0,10,  700);
    }

    @Test
    public void testPlugMachine(){
        Assertions.assertFalse(espressoCoffeeMachineTest.isPlugged());


        espressoCoffeeMachineTest.plugToElectricalPlug();

        Assertions.assertTrue(espressoCoffeeMachineTest.isPlugged());
    }

    @Test
    public void testMakeACoffeeMachnineNotPlugException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        Assertions.assertThrows(MachineNotPluggedException.class, ()->{
            espressoCoffeeMachineTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "You must plug your coffee machine.");
    }

    @Test
    public void testMakeACoffeLackOfWaterInTankException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.0);
        espressoCoffeeMachineTest.plugToElectricalPlug();

        Assertions.assertThrows(LackOfWaterInTankException.class, ()->{
            espressoCoffeeMachineTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "You must add more water in the water tank.");
    }

    @Test
    public void testMakeACoffeeCupNotEmptyException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        espressoCoffeeMachineTest.plugToElectricalPlug();

        //assertThrows( [Exception class expected], [lambda expression with the method that throws an exception], [exception message expected])
        //AssertThrows va permettre de venir tester la levée d'une exception, ici lorsque que le contenant passé en
        //paramètre n'est pas vide
        //On teste à la fois le type d'exception levée mais aussi le message de l'exception
        Assertions.assertThrows(CupNotEmptyException.class, ()->{
            espressoCoffeeMachineTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "The container given is not empty.");
    }

    @Test
    public void testMakeACoffeeCoffeeTypeCupDifferentOfCoffeeTypeTankException() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere {
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.0);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        espressoCoffeeMachineTest.addWaterInTank(5.0);
        espressoCoffeeMachineTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        espressoCoffeeMachineTest.plugToElectricalPlug();

        Assertions.assertThrows(CoffeeTypeCupDifferentOfCoffeeTypeTankException.class, ()->{
            espressoCoffeeMachineTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "The type of coffee to be made in the cup is different from that in the tank.");
    }

    @Test void testMakeACoffeeCup() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere, CoffeeTypeCupDifferentOfCoffeeTypeTankException, LackOfWaterInTankException, CupNotEmptyException, CannotMakeCremaWithSimpleCoffeeMachine, InterruptedException, MachineNotPluggedException {
        Cup cup = new Cup(0.15);
        CoffeeCup coffeeCupExpected = new CoffeeCup(0.15,CoffeeType.ARABICA);
        coffeeCupExpected.setEmpty(false);

        espressoCoffeeMachineTest.addWaterInTank(5.0);
        espressoCoffeeMachineTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        espressoCoffeeMachineTest.plugToElectricalPlug();

        CoffeeContainer coffeeCupActual = espressoCoffeeMachineTest.makeACoffee(cup,CoffeeType.ARABICA);
        Assertions.assertEquals(coffeeCupExpected.toString(),coffeeCupActual.toString());
    }

    @Test void testMakeACoffeeMug() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere, CoffeeTypeCupDifferentOfCoffeeTypeTankException, LackOfWaterInTankException, CupNotEmptyException, CannotMakeCremaWithSimpleCoffeeMachine, InterruptedException, MachineNotPluggedException {
        Mug mug = new Mug(0.30);
        CoffeeMug coffeeMugExpected = new CoffeeMug(0.30,CoffeeType.ARABICA);
        coffeeMugExpected.setEmpty(false);

        espressoCoffeeMachineTest.addWaterInTank(5.0);
        espressoCoffeeMachineTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        espressoCoffeeMachineTest.plugToElectricalPlug();

        CoffeeContainer coffeeMugActual = espressoCoffeeMachineTest.makeACoffee(mug,CoffeeType.ARABICA);
        Assertions.assertEquals(coffeeMugExpected.toString(),coffeeMugActual.toString());
    }
}
