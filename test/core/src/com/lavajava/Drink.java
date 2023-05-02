package com.lavajava;

public class Drink
{
    Base base;
    Milk milk;
    Syrup syrup;

    double cost;

//    public Drink()
//    {
//        // default drink will be a vanilla late with cow's milk
//        this.base  = Base.Caffeine;
//        this.milk = Milk.Cow;
//        this.syrup = Syrup.Vanilla;
//    }

    public Drink(Drink d)
    {
        this.base = d.base;
        this.milk = d.milk;
        this.syrup = d.syrup;
    }

    public Drink(Base base, Milk milk, Syrup syrup)
    {
        this.base = base;
        this.milk = milk;
        this.syrup = syrup;
    }

    public Drink getRoot()
    {
        return this;
    }

    public double getCost()
    {
        return cost;
    }
    static enum Base
    {
        Caffeine,
        Tea,
        Matcha
    }

    static enum Milk
    {
        Cow,
        Almond,
        Oat
    }

    static enum Syrup
    {
        Lavender,
        Hazelnut,
        Vanilla
    }
}

