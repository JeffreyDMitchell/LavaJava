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

    public GameScreen(final LJGame game)
    {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, LJGame.RES_X, LJGame.RES_Y);
        background = new Texture(Gdx.files.internal("Cafe_Background.png"));
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
        game.batch.end();
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

    }
}
