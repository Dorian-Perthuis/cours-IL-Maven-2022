package fr.imt.coffee.machine;

import fr.imt.coffee.machine.exception.*;
import fr.imt.cours.storage.cupboard.coffee.type.CoffeeType;
import fr.imt.cours.storage.cupboard.container.*;
import fr.imt.cours.storage.cupboard.exception.CupNotEmptyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoffeeMachineUnitTest {
    public CoffeeMachine coffeeMachineUnderTest;

    /**
     * @BeforeEach est une annotation permettant d'exécuter la méthode annotée avant chaque test unitaire
     * Ici avant chaque test on initialise la machine à café
     */
    @BeforeEach
    public void beforeTest(){
        coffeeMachineUnderTest = new CoffeeMachine(
                0,10,
                0,10,  700);
    }

    /**
     * On vient tester si la machine ne se met pas en défaut
     */
    @Test
    public void testMachineFailureTrue(){
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir contrôler la valeur retournée
        //ici on veut qu'elle retourne 1.0
        //when : permet de définir quand sur quelle méthode établir le stub
        //thenReturn : va permettre de contrôler la valeur retournée par le stub
        Mockito.when(randomMock.nextGaussian()).thenReturn(1.0);
        //On injecte ensuite le mock créé dans la machine à café
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        //On vérifie que le booleen outOfOrder est bien à faux avant d'appeler la méthode
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));

        //on appelle la méthode qui met la machine en défaut
        //On a mocké l'objet random donc la valeur retournée par nextGaussian() sera 1
        //La machine doit donc se mettre en défaut
        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertTrue(coffeeMachineUnderTest.isOutOfOrder());
        assertThat(true, is(coffeeMachineUnderTest.isOutOfOrder()));

    }

    /**
     * On vient tester si la machine se met en défaut
     */
    @Test
    public void testMachineFailureFalse(){
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir contrôler la valeur retournée
        //ici on veut qu'elle retourne 0.6
        //when : permet de définir quand sur quelle méthode établir le stub
        //thenReturn : va permettre de contrôler la valeur retournée par le stub
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        //On injecte ensuite le mock créé dans la machine à café
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        //On vérifie que le booleen outOfOrder est bien à faux avant d'appeler la méthode
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));

        //on appelle la méthode qui met la machine en défaut
        //On a mocker l'objet random donc la valeur retournée par nextGaussian() sera 0.6
        //La machine doit donc NE PAS se mettre en défaut
        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));
    }

    /**
     * On test que la machine se branche correctement au réseau électrique
     */
    @Test
    public void testPlugMachine(){
        Assertions.assertFalse(coffeeMachineUnderTest.isPlugged());


        coffeeMachineUnderTest.plugToElectricalPlug();

        Assertions.assertTrue(coffeeMachineUnderTest.isPlugged());
    }
    @Test
    public void testMakeACoffeeMachnineNotPlugException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        Assertions.assertThrows(MachineNotPluggedException.class, ()->{
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "You must plug your coffee machine.");
    }
    @Test
    public void testMakeACoffeLackOfWaterInTankException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.0);
        coffeeMachineUnderTest.plugToElectricalPlug();

        Assertions.assertThrows(LackOfWaterInTankException.class, ()->{
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "You must add more water in the water tank.");
    }

    /**
     * On test qu'une exception est bien levée lorsque que le cup passé en paramètre retourne qu'il n'est pas vide
     * Tout comme le test sur la mise en défaut afin d'avoir un comportement isolé et indépendant de la machine
     * on vient ici mocker un objet Cup afin d'en maitriser complétement son comportement
     * On ne compte pas sur "le bon fonctionnement de la méthode"
     */
    @Test
    public void testMakeACoffeeCupNotEmptyException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        coffeeMachineUnderTest.plugToElectricalPlug();

        //assertThrows( [Exception class expected], [lambda expression with the method that throws an exception], [exception message expected])
        //AssertThrows va permettre de venir tester la levée d'une exception, ici lorsque que le contenant passé en
        //paramètre n'est pas vide
        //On teste à la fois le type d'exception levée mais aussi le message de l'exception
        Assertions.assertThrows(CupNotEmptyException.class, ()->{
                coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
            }, "The container given is not empty.");
    }

    @Test
    public void testMakeACoffeeCoffeeTypeCupDifferentOfCoffeeTypeTankException() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere {
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.0);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        coffeeMachineUnderTest.addWaterInTank(5.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        coffeeMachineUnderTest.plugToElectricalPlug();

        Assertions.assertThrows(CoffeeTypeCupDifferentOfCoffeeTypeTankException.class, ()->{
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
        }, "The type of coffee to be made in the cup is different from that in the tank.");
    }

    @Test
    public void testMakeACoffeeCannotMakeCremaWithSimpleCoffeeMachine() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere {
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.0);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        coffeeMachineUnderTest.addWaterInTank(5.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        coffeeMachineUnderTest.plugToElectricalPlug();

        Assertions.assertThrows(CannotMakeCremaWithSimpleCoffeeMachine.class, ()->{
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA_CREMA);
        }, "You cannot make an espresso with a CoffeeMachine, please use EspressoCoffeeMachine");

    }

    @Test
    public void testAddWaterInTank(){
        double waterVolume = 5;

        coffeeMachineUnderTest.addWaterInTank(waterVolume);

        Assertions.assertEquals(waterVolume,coffeeMachineUnderTest.getWaterTank().getActualVolume());
    }

    @Test
    public void testAddWaterInTankWaterTankFull(){
        double waterVolume = 15;

        coffeeMachineUnderTest.addWaterInTank(waterVolume);

        Assertions.assertEquals(coffeeMachineUnderTest.getWaterTank().getMaxVolume(),coffeeMachineUnderTest.getWaterTank().getActualVolume());
    }

    @Test
    public void testAddCoffeeInBeanTankWrongBeanException() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere {

        double coffeeVolume = 3;
        coffeeMachineUnderTest.addCoffeeInBeanTank(coffeeVolume,CoffeeType.MOKA);

        //CoffeeTypeCupDifferentOfCoffeeTypeTankException
        Assertions.assertThrows(CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere.class, ()->{
            coffeeMachineUnderTest.addCoffeeInBeanTank(coffeeVolume, CoffeeType.ARABICA);
        }, "Wrong coffee than the coffee already in the tank");
    }

    @Test void testMakeACoffeeCup() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere, CoffeeTypeCupDifferentOfCoffeeTypeTankException, LackOfWaterInTankException, CupNotEmptyException, CannotMakeCremaWithSimpleCoffeeMachine, InterruptedException, MachineNotPluggedException {
        Cup cup = new Cup(0.15);
        CoffeeCup coffeeCupExpected = new CoffeeCup(0.15,CoffeeType.ARABICA);
        coffeeCupExpected.setEmpty(false);

        coffeeMachineUnderTest.addWaterInTank(5.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        coffeeMachineUnderTest.plugToElectricalPlug();

        CoffeeContainer coffeeCupActual = coffeeMachineUnderTest.makeACoffee(cup,CoffeeType.ARABICA);
        Assertions.assertEquals(coffeeCupExpected.toString(),coffeeCupActual.toString());
    }

    @Test void testMakeACoffeeMug() throws CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere, CoffeeTypeCupDifferentOfCoffeeTypeTankException, LackOfWaterInTankException, CupNotEmptyException, CannotMakeCremaWithSimpleCoffeeMachine, InterruptedException, MachineNotPluggedException {
        Mug mug = new Mug(0.30);
        CoffeeMug coffeeMugExpected = new CoffeeMug(0.30,CoffeeType.ARABICA);
        coffeeMugExpected.setEmpty(false);

        coffeeMachineUnderTest.addWaterInTank(5.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(5.0, CoffeeType.ARABICA);
        coffeeMachineUnderTest.plugToElectricalPlug();

        CoffeeContainer coffeeMugActual = coffeeMachineUnderTest.makeACoffee(mug,CoffeeType.ARABICA);
        Assertions.assertEquals(coffeeMugExpected.toString(),coffeeMugActual.toString());
    }

    @AfterEach
    public void afterTest(){

    }
}
