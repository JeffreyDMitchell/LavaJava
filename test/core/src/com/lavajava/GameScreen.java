package com.lavajava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen
{
    final LJGame game;

    private OrthographicCamera cam;

    private Texture background;
    private Texture[] drink_textures;
    private Texture[] milk_textures;
    private Texture[] syrup_textures;

    private final int DRINK_WIDTH = 300;
    private final int DRINK_HEIGHT = 200;
    private final int MILK_WIDTH = 150;
    private final int MILK_HEIGHT = 150;
    private final int SYRUP_WIDTH = 150;
    private final int SYRUP_HEIGHT = 150;
    private final int WIP_DRINK_X = 250;
    private final int WIP_DRINK_Y = LJGame.RES_Y / 2;

    private int time_stamp = 0;

    public GameScreen(final LJGame game)
    {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, LJGame.RES_X, LJGame.RES_Y);

        // load and prepare textures
        background = new Texture(Gdx.files.internal("Cafe_Background.png"));
        drink_textures = new Texture[] {
                new Texture(Gdx.files.internal("Coffee.png")),
                new Texture(Gdx.files.internal("Tea.png")),
                new Texture(Gdx.files.internal("Matcha.png"))
        };
        milk_textures = new Texture[] {
                new Texture(Gdx.files.internal("Cow.png")),
                new Texture(Gdx.files.internal("Almond.png")),
                new Texture(Gdx.files.internal("Oat.png"))
        };
        syrup_textures = new Texture[] {
                new Texture(Gdx.files.internal("Lavender_Syrup.png")),
                new Texture(Gdx.files.internal("Hazelnut_Syrup.png")),
                new Texture(Gdx.files.internal("Vanilla_Syrup.png"))
        };
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        update(delta);

        ScreenUtils.clear(LJGame.SKY_COLOR);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        // render here
        game.batch.draw(background, 0, 0, LJGame.RES_X, LJGame.RES_Y);

        drawDrink(null, WIP_DRINK_X, WIP_DRINK_Y);

        game.batch.end();
    }

    public void drawDrink(Drink drink, int x, int y)
    {
        // draw base drink
        game.batch.draw(
                drink_textures[(time_stamp / 100) % drink_textures.length],
                x - DRINK_WIDTH / 2,
                y - DRINK_HEIGHT / 2,
                DRINK_WIDTH,
                DRINK_HEIGHT);

        // draw milk icon
        // those nast random magic numbers are biases to center icon :(
        // maybe we can change things around to get rid of them
        game.batch.draw(
                milk_textures[(time_stamp / 50) % drink_textures.length],
                x - MILK_WIDTH / 2 - 30,
                y - MILK_HEIGHT / 2 - 25,
                MILK_WIDTH,
                MILK_HEIGHT);

        game.batch.draw(
                syrup_textures[(time_stamp / 25) % drink_textures.length],
                x - SYRUP_WIDTH / 2 - 30,
                y - SYRUP_HEIGHT / 2 + 100,
                SYRUP_WIDTH,
                SYRUP_HEIGHT);
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }

    void update(float delta)
    {
        time_stamp++;
    }
}
