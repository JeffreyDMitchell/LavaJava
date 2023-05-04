package com.lavajava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

public class GameScreen implements Screen
{
    final LJGame game;

    private Stage stage;
    private Skin skin;

    private OrthographicCamera cam;

    private GlyphLayout gl;

    private Texture background, chocolate_texture, marshmallow_texture, whip_texture, register_texture;
    private Texture[] drink_textures, milk_textures, syrup_textures, customer_textures;

    private Music bg_music;
    private Sound sale_sound, fail_sound;

    // nasty graphical constants
    private final int DRINK_WIDTH = 300;
    private final int DRINK_HEIGHT = 150;
    private final int MILK_WIDTH = 75;
    private final int MILK_HEIGHT = 75;
    private final int SYRUP_WIDTH = 100;
    private final int SYRUP_HEIGHT = 100;
    private final int TOPPING_SIZE = 100;
    private final int WIP_DRINK_X = 750;
    private final int WIP_DRINK_Y = 375;
    private final int CUST_X = 1950;
    private final int CUST_Y = 318;
    private final int CUST_WIDTH = 300;
    private final int CUST_HEIGHT = 400;
    private final float CUST_RESPAWN = 1;
//    private final int COUNTDOWN_X;

    private int score = 0;
    private int time_stamp = 0;
    private float countdown;
    private float cust_respawn_timer;
    boolean active = true;

    // TODO temp test code
    Customer customer;
    Drink wip_drink;


    public GameScreen(final LJGame game)
    {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, LJGame.RES_X, LJGame.RES_Y);

        gl = new GlyphLayout();

        // set up input
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));

        // OO PATTERNS: FLYWEIGHT
        // while I'll admit that the following code could certainly stand to be cleaned up
        // using a fixed pool of resources (textures, sounds, etc.) and passing references to these
        // resources for characters / drinks to make use of is a huge resource saving.

        // load and prepare textures
        background = new Texture(Gdx.files.internal("Cafe_Background.png"));
        chocolate_texture = new Texture(Gdx.files.internal("Chocolate_Bar.png"));
        marshmallow_texture = new Texture(Gdx.files.internal("Marshmallow.png"));
        whip_texture = new Texture(Gdx.files.internal("Whipped_Cream.png"));
        register_texture = new Texture(Gdx.files.internal("Cash_Register.png"));

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
        customer_textures = new Texture[]{
                new Texture(Gdx.files.internal("Frog.png")),
                new Texture(Gdx.files.internal("Dog.png")),
                new Texture(Gdx.files.internal("Bunny.png"))
        };

        // load and prepare audio
        bg_music = Gdx.audio.newMusic(Gdx.files.internal("bg_music.mp3"));
        sale_sound = Gdx.audio.newSound(Gdx.files.internal("sale.mp3"));
        fail_sound = Gdx.audio.newSound(Gdx.files.internal("fail.mp3"));

        bg_music.setVolume(.1f);
        bg_music.play();

        // EXAMPLE BUTTON
//        button = new TextButton("Click Me!", skin);
//        button.setSize(200, 50);
//        button.setPosition(LJGame.RES_X / 2, LJGame.RES_Y / 2);
//        stage.addActor(button);

        setupImageButton(register_texture, 1350, 275, 300, 200, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(customer == null) return;

                if(wip_drink.equals(customer.request))
                {
                    score++;
                    countdown += 5;
                    cust_respawn_timer = CUST_RESPAWN;
                    customer = null;
                    sale_sound.setVolume(sale_sound.play(), 0.1f);

                    wip_drink = DrinkFactory.NamedDrink("Vanilla Latte");
                }
                else
                {
                    score = Math.max(0, score-1);
                    fail_sound.setVolume(fail_sound.play(), 0.1f);
                }
            }
        });

        setupImageButton(chocolate_texture, 574, 0, 150, 150, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink = new Chocolate(wip_drink);
            }
        });

        setupImageButton(whip_texture, 650, 90, 150, 150, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink = new Whip(wip_drink);
            }
        });
        setupImageButton(marshmallow_texture, 725, 0, 150, 150, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink = new Marshmallow(wip_drink);
            }
        });

        setupTextButton("TOSS", 500, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink = DrinkFactory.NamedDrink("Vanilla Latte");
            }
        });
        setupTextButton("Base", 350, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink.base = Drink.Base.values()[(wip_drink.base.ordinal() + 1) % Drink.Base.values().length];
            }
        });

        setupTextButton("Milk", 200, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink.milk = Drink.Milk.values()[(wip_drink.milk.ordinal() + 1) % Drink.Milk.values().length];
            }
        });
        setupTextButton("Syrup", 50, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink.syrup = Drink.Syrup.values()[(wip_drink.syrup.ordinal() + 1) % Drink.Syrup.values().length];
            }
        });

//        wip_drink = new Marshmallow(new Whip(new Chocolate(DrinkFactory.RandomDrink())));
//        customer = new Customer(customer_textures[2], 5);
        wip_drink = DrinkFactory.NamedDrink("Vanilla Late");

        countdown = 20;
        cust_respawn_timer = 1;

    }

    // kinda of a garbage, inflexible little function, but it cuts down on code repetition considerably
    void setupTextButton(String text, int y_pos, ClickListener cl)
    {
        TextButton button = new TextButton(text, skin);
        button.setSize(200, 100);
        button.setPosition(100, y_pos);
        button.addListener(cl);

        stage.addActor(button);
    }

    // also garbage :) but you should have seen it before
    void setupImageButton(Texture t, int x_pos, int y_pos, int width, int height, ClickListener cl)
    {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        TextureRegionDrawable trd = new TextureRegionDrawable(t);

        // magic numbers! wahooo! we love magic numbers!
        trd.setMinSize(width, height);

        style.imageUp = trd;
        style.imageDown = trd.tint(new Color(0.8f,0.8f,0.8f,1));

        ImageButton img_button = new ImageButton(style);
        img_button.setPosition(x_pos, y_pos);

        img_button.addListener(cl);

        stage.addActor(img_button);
    }

    @Override
    public void show()
    {
        bg_music.play();
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

        if(customer != null)
        {
            // draw customer
            game.batch.draw(
                    customer.texture,
                    CUST_X - CUST_WIDTH / 2,
                    CUST_Y,
                    CUST_WIDTH,
                    CUST_HEIGHT);

            // draw customer drink
            drawDrink(customer.request, CUST_X, CUST_Y + 450 + (int) (Math.sin(time_stamp / 100.0) * 25));
        }

        // draw WIP drink
        drawDrink(wip_drink, WIP_DRINK_X, WIP_DRINK_Y);

        // draw countdown
        gl.setText(game.font, String.format("%.1f", countdown));
        game.font.draw(game.batch, String.format("%.1f", Math.max(0, countdown)), LJGame.RES_X / 2 - (gl.width / 2),LJGame.RES_Y);

        // draw score
        game.font.draw(game.batch, "Score: " + score, 0, LJGame.RES_Y);

        // draw game over text if finished
        if(!active)
        {
            gl.setText(game.font, "Game Over!");
            game.font.draw(game.batch, "Game Over!", LJGame.RES_X / 2 - gl.width / 2, LJGame.RES_Y / 2 + gl.height / 2);
        }

        game.batch.end();

        // draw input layer
        stage.draw();
    }

    public void drawDrink(Drink drink, int x, int y)
    {
        Drink root = drink.getRoot();

        // draw base drink
        game.batch.draw(
                drink_textures[root.base.ordinal()],
                x - DRINK_WIDTH / 2,
                y - DRINK_HEIGHT / 2,
                DRINK_WIDTH,
                DRINK_HEIGHT);

        // draw milk icon
        // those nast random magic numbers are biases to center icon :(
        // maybe we can change things around to get rid of them
        game.batch.draw(
                milk_textures[root.milk.ordinal()],
                x - MILK_WIDTH / 2,
                y - MILK_HEIGHT / 2 - 25,
                MILK_WIDTH,
                MILK_HEIGHT);

        game.batch.draw(
                syrup_textures[root.syrup.ordinal()],
                x - SYRUP_WIDTH / 2,
                // raised by 50 pixels to appear above drink
                y - SYRUP_HEIGHT / 2 + 75,
                SYRUP_WIDTH,
                SYRUP_HEIGHT);

        // draw toppings
        // yay more magic numbers
        int topping_y = y + 150;
        int topping_x = x + 100;
        int x_shift = 0;
        while(drink instanceof Topping)
        {
            Texture t;

            Class topping_class = drink.getClass();

            if(topping_class == Chocolate.class)
                t = chocolate_texture;
            else if(topping_class == Marshmallow.class)
                t = marshmallow_texture;
            else
                // assumed whip
                t = whip_texture;

            game.batch.draw(
                    t,
                    topping_x - (TOPPING_SIZE * x_shift) - TOPPING_SIZE / 2,
                    topping_y - TOPPING_SIZE / 2,
                    TOPPING_SIZE,
                    TOPPING_SIZE
            );

            if(++x_shift == 3)
            {
                x_shift = 0;
                topping_y += TOPPING_SIZE;
            }

            drink = ((Topping) drink).next;
        }
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
        bg_music.pause();
        dispose();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        skin.dispose();
        background.dispose();
        chocolate_texture.dispose();
        marshmallow_texture.dispose();
        whip_texture.dispose();
        register_texture.dispose();

        for(Texture t : drink_textures)
            t.dispose();
        for(Texture t : milk_textures)
            t.dispose();
        for(Texture t : syrup_textures)
            t.dispose();
        for(Texture t : customer_textures)
            t.dispose();

        bg_music.dispose();
        sale_sound.dispose();
        fail_sound.dispose();
    }

    void update(float delta)
    {
        if(countdown <= 0)
            active = false;

        if(!active && Gdx.input.isTouched())
        {
            game.setScreen(MainMenuScreen.getInstance(game));
        }

        // screen behavior
        stage.act(delta);

//        customer.texture = customer_textures[(time_stamp / 50) % 3];

        if(customer == null && cust_respawn_timer <= 0)
        {
            customer = new Customer(customer_textures[MathUtils.random(customer_textures.length-1)], (int) (5 * Math.log10(score + 1)));
        }

        countdown -= delta;
        cust_respawn_timer -= delta;

        time_stamp++;
    }
}
