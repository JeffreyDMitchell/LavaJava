package com.lavajava;
import java.util.ArrayList;

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

    public boolean equals(Drink d)
    {
        Drink a = this;
        Drink b = d;
        boolean like_toppings = true;

        // collect toppings from each drink
        ArrayList<Class<? extends Drink>> a_toppings = new ArrayList<Class<? extends Drink>>();
        ArrayList<Class<? extends Drink>> b_toppings = new ArrayList<Class<? extends Drink>>();

        while(a instanceof Topping)
        {
            a_toppings.add(a.getClass());
            a = ((Topping) a).next;
        }

        while(b instanceof Topping)
        {
            b_toppings.add(b.getClass());
            b = ((Topping) b).next;
        }

        // remove toppings one by one, making sure that each has a partner in the other collection
        while(a_toppings.size() > 0)
            like_toppings &= b_toppings.remove(a_toppings.remove(0));

        // ensure that b_toppings is not a superset of a_toppings
        like_toppings &= b_toppings.size() == 0;

        // return true if toppings are the same, in addition to the basics
        return like_toppings &&
                this.base == d.base &&
                this.milk == d.milk &&
                this.syrup == d.syrup;
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

