package com.lavajava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import javax.xml.crypto.AlgorithmMethod;

public class MainMenuScreen implements Screen
{
    final LJGame game;
    OrthographicCamera cam;

    private Texture background;
    private Music bird_music;

    private double title_anim_y = LJGame.RES_Y;


    public MainMenuScreen(final LJGame game)
    {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, LJGame.RES_X, LJGame.RES_Y);
        // load background
        background = new Texture(Gdx.files.internal("Opening_Screen.png"));
        bird_music = Gdx.audio.newMusic(Gdx.files.internal("Bird_Sounds.mp3"));

        bird_music.setLooping(true);
        bird_music.play();
    }

    @Override
    public void show()
    {
        bird_music.play();
    }

    @Override
    public void render(float delta)
    {
        // update scene prior to rendering
        update(delta);
        // clear to black
        ScreenUtils.clear(LJGame.SKY_COLOR);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        // render graphics here
        game.batch.draw(background, 0, (int) title_anim_y, LJGame.RES_X, LJGame.RES_Y);
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
        bird_music.pause();
    }

    @Override
    public void dispose()
    {
        background.dispose();
        bird_music.dispose();
    }

    private void update(float delta)
    {
        title_anim_y = Math.max(0, title_anim_y - 1.5);

        if(Gdx.input.isTouched())
        {
            game.setScreen(new GameScreen(game));
        }
    }
}
