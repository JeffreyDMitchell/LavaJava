package com.lavajava;
import java.util.ArrayList;

// OO PATTERNS: DECORATOR
// Toppings are implemented using the decorator pattern... for better or for worse...
public class Topping extends Drink
{
    static ArrayList<Class<? extends Drink>> topping_list = new ArrayList<Class<? extends Drink>>(){{
        add(Chocolate.class);
        add(Marshmallow.class);
        add(Whip.class);
    }};

    Drink root;
    Drink next;

    int cost;

    public Topping(Drink d)
    {
        super(d);
        root = d.getRoot();
        next = d;
    }

    @Override
    public double getCost()
    {
        return this.cost + next.getCost();
    }
}

class Marshmallow extends Topping
{
    Marshmallow(Drink d)
    {
        super(d);
        this.cost = 1;
    }
}

class Chocolate extends Topping
{
    Chocolate(Drink d)
    {
        super(d);
        this.cost = 2;
    }
}

class Whip extends Topping
{
    Whip(Drink d)
    {
        super(d);
        this.cost = 1;
    }
}