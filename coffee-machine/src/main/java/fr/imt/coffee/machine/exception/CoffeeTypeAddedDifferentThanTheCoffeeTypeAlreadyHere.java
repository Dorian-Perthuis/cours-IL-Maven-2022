package fr.imt.coffee.machine.exception;

public class CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere extends Exception{
    public CoffeeTypeAddedDifferentThanTheCoffeeTypeAlreadyHere(String message) {
        super(message);
    }
}
