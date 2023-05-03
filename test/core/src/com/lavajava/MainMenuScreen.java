package com.lavajava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

import javax.xml.crypto.AlgorithmMethod;

public class MainMenuScreen implements Screen
{
    public static MainMenuScreen instance;

    final LJGame game;
    OrthographicCamera cam;

    private Texture background, ground;
    private Texture[] cat_textures;
    private Music bird_music;

    private int time_stamp = 0;
    private final int ANIM_SPD = 3;
    private int cat_texture_width, cat_texture_height;

    Array<Cat> cat_crowd;
    private MainMenuScreen(final LJGame game)
    {
        this.game = game;

        // set up cam
        cam = new OrthographicCamera();
        cam.setToOrtho(false, LJGame.RES_X, LJGame.RES_Y);

        cat_crowd = new Array<Cat>();

        // load textures
        background = new Texture(Gdx.files.internal("Opening_Screen.png"));
        ground = new Texture(Gdx.files.internal("Opening_Ground.png"));
        cat_textures = new Texture[]{
            new Texture(Gdx.files.internal("Cat1.png")),
            new Texture(Gdx.files.internal("Cat2.png")),
            new Texture(Gdx.files.internal("Cat3.png"))
        };

        cat_texture_width = cat_textures[0].getWidth();
        cat_texture_height = cat_textures[0].getHeight();

        // load and start music
        bird_music = Gdx.audio.newMusic(Gdx.files.internal("Bird_Sounds.mp3"));
        bird_music.setLooping(true);
        bird_music.play();

        time_stamp = 0;
    }

    public static MainMenuScreen getInstance(LJGame game)
    {
        if(instance == null)
            instance = new MainMenuScreen(game);

        return instance;
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

        // draw ground
        game.batch.draw(ground, 0, (int) Math.min(0, -1 * LJGame.RES_Y + (ANIM_SPD * time_stamp)), LJGame.RES_X, LJGame.RES_Y);

        // draw background
        game.batch.draw(background, 0, (int) Math.max(0, LJGame.RES_Y - (ANIM_SPD * time_stamp)), LJGame.RES_X, LJGame.RES_Y);

        // draw cats
        for(Cat cat : cat_crowd)
        {
            // if you want access to the flipped property you have to use the super mega ultra
            // function call with all the parameters...
            game.batch.draw(
                    cat.texture,
                    cat.x,
                    cat.y,
                    Cat.WIDTH,
                    Cat.HEIGHT,
                    0,
                    0,
                    cat_texture_width,
                    cat_texture_height,
                    cat.spd > 0,
                    false);
        }

        // prompt user to tap screen after a time
        if(time_stamp * 1.5 > LJGame.RES_Y)
            game.font.draw(game.batch, "Tap Anywhere\nTo Play!", LJGame.RES_X * 2 / 3 + 75, LJGame.RES_Y / 2 + 100);

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
        ground.dispose();
        for(Texture t : cat_textures)
            t.dispose();
    }

    private void update(float delta)
    {
        // start game
        if(Gdx.input.isTouched())
        {
            if(time_stamp * ANIM_SPD < LJGame.RES_Y)
                time_stamp = LJGame.RES_Y / ANIM_SPD;

            if(time_stamp * ANIM_SPD > LJGame.RES_Y + 60)
            {
//                time_stamp = 0;
                game.setScreen(new GameScreen(game));
            }
        }

        // sliding animation finished, start spawning cats into crowd
        // attempt to spawn a cat twice a second
        // only allow 50 cats
        if(time_stamp * ANIM_SPD > LJGame.RES_Y && time_stamp % 120 == 0 && cat_crowd.size < 10)
        {
            // 30% chance
            if(MathUtils.random() < .3)
            {
                boolean dir = MathUtils.randomBoolean();

                cat_crowd.add(new Cat(
                        dir ? (0 - Cat.WIDTH) : (LJGame.RES_X),
                        MathUtils.random(175),
                        MathUtils.random(0.5f, 2.0f) * (dir ? 1 : -1),
                        cat_textures[MathUtils.random(cat_textures.length-1)]
                ));

                // sorta cats by their y coord, makes more sense graphically
                cat_crowd.sort();
            }
        }

        // update cat positions
        Iterator<Cat> iter = cat_crowd.iterator();
        while(iter.hasNext())
        {
            Cat cat = iter.next();

            cat.x += cat.spd;
            if(cat.x < 0 - (Cat.WIDTH)) cat.x = LJGame.RES_X + Cat.WIDTH;
            if(cat.x > LJGame.RES_X + (Cat.WIDTH)) cat.x = 0 - Cat.WIDTH;
        }

        // update time, changing animations and triggering events
        time_stamp++;
    }
    private class Cat implements Comparable<Cat>
    {
        float x, y, spd;
        static final int WIDTH = 200;
        static final int HEIGHT = 100;
        Texture texture;

        Cat(float x, float y, float spd, Texture texture)
        {
            this.x = x;
            this.y = y;
            this.spd = spd;
            this.texture = texture;
        }

        @Override
        public int compareTo(Cat cat)
        {
            float diff = cat.y - this.y;
            return (int) (diff < 0 ? Math.floor(diff) : Math.ceil(diff));
        }
    }
}