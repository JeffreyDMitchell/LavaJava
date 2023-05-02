package com.lavajava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class DrinkFactory
{

    public static Drink RandomDrink()
    {
        return new Drink(
                Drink.Base.values()[MathUtils.random(Drink.Base.values().length - 1)],
                Drink.Milk.values()[MathUtils.random(Drink.Milk.values().length - 1)],
                Drink.Syrup.values()[MathUtils.random(Drink.Syrup.values().length - 1)]
                );
    }

    public static Drink RandomDrinkToppings(int num_toppings)
    {
        Drink drink = RandomDrink();

        for(int i = 0; i < num_toppings; i++)
        {
            try
            {
                drink = Topping.topping_list.get(MathUtils.random(Topping.topping_list.size() - 1)).getDeclaredConstructor(Drink.class).newInstance(drink);
            }
            catch(Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }

        return drink;
    }

    public static Drink NamedDrink(String name)
    {
        switch(name)
        {
            case "Vanilla Latte":
                return new Drink(
                        Drink.Base.Caffeine,
                        Drink.Milk.Cow,
                        Drink.Syrup.Vanilla
                );

            default:
                return new Drink(
                        Drink.Base.Caffeine,
                        Drink.Milk.Cow,
                        Drink.Syrup.Vanilla
                );
        }
    }

}
