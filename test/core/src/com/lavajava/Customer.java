package com.lavajava;

import com.badlogic.gdx.Gdx;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;

import java.io.IOException;

public class Customer
{
    Texture texture;
    Drink request;

    public Customer(Texture texture, int topping_ct)
    {
        this.texture = texture;
        request = DrinkFactory.RandomDrinkToppings(topping_ct);
    }
}