package fr.imt.cours.storage.cupboard.container;

import fr.imt.cours.storage.cupboard.coffee.type.CoffeeType;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CoffeeCup extends CoffeeContainer{
    public CoffeeCup(double capacity, CoffeeType coffeeType) {
        super(capacity, coffeeType);
    }

    public CoffeeCup(Container container, CoffeeType coffeeType) {
        super(container, coffeeType);
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Container type : Cup";
    }
}
