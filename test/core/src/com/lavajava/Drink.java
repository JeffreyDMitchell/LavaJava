package com.lavajava;

public class Drink
{
    Base base;
    Milk milk;
    Syrup syrup;

    double cost;

    public Drink() {}

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

