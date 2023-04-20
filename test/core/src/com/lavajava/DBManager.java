package com.lavajava;

import com.badlogic.gdx.Gdx;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;

import java.io.IOException;

public class DBManager
{
    String ip;
    int port;
    OkHttpClient http_client;
    Gson gson;

    String auth_token;

    static DBManager instance;

    private DBManager()
    {
        http_client = new OkHttpClient();
        gson = new Gson();
    }

    static DBManager getInstance()
    {
        if(instance == null)
            instance = new DBManager();

        return instance;
    }

    boolean configure(String ip, int port)
    {
        this.ip = ip;
        this.port = port;

        return true;
    }

    boolean connect()
    {
        // TODO have some kind of dummy endpoint just to check if server is live
        return true;
    }

    boolean login()
    {

        User user = new User();
        user.username = "fred";
        user.password = "pass123";

        String json_str = gson.toJson(user);
        MediaType JSON = MediaType.parse("application/json; chatset=utf-8");
//        RequestBody req_body = RequestBody.create(json_str, JSON);

        RequestBody req_body = RequestBody.create(JSON, json_str);
        Request req = new Request.Builder()
                .url(String.format("http://%s:%d/login/", this,ip, this.port))
                .post(req_body)
                .build();

        try
        {
            Response res = http_client.newCall(req).execute();

            if(res.isSuccessful())
            {
                Gdx.app.log("DBManager", res.body().string());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    boolean getScore(String username)
    {
        // TODO
        return true;
    }

    boolean getScore()
    {
        // TODO
        return true;
    }

    boolean sendScore(int score)
    {
        // TODO
        return true;
    }
}

class User
{
    String username;
    String password;
}
