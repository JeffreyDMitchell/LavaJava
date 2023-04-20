package com.lavajava;

public class Topping extends Drink
{
    Drink root;
    Drink next;

    int cost;

    public Topping(Drink d)
    {
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