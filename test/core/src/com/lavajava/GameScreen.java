package com.lavajava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

    private Texture background, chocolate_texture, marshmallow_texture, whip_texture;
    private Texture[] drink_textures, milk_textures, syrup_textures, customer_textures;

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

    private int time_stamp = 0;

    // TODO temp test code
    Customer customer;
    Drink wip_drink;


    public GameScreen(final LJGame game)
    {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, LJGame.RES_X, LJGame.RES_Y);

        // set up input
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));

        // load and prepare textures
        background = new Texture(Gdx.files.internal("Cafe_Background.png"));
        chocolate_texture = new Texture(Gdx.files.internal("Chocolate_Bar.png"));
        marshmallow_texture = new Texture(Gdx.files.internal("Marshmallow.png"));
        whip_texture = new Texture(Gdx.files.internal("Whipped_Cream.png"));

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

        // EXAMPLE BUTTON
//        button = new TextButton("Click Me!", skin);
//        button.setSize(200, 50);
//        button.setPosition(LJGame.RES_X / 2, LJGame.RES_Y / 2);
//        stage.addActor(button);

        setupToppingButton(chocolate_texture, 574, 0, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink = new Chocolate(wip_drink);
            }
        });

        setupToppingButton(whip_texture, 650, 90, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                wip_drink = new Whip(wip_drink);
            }
        });
        setupToppingButton(marshmallow_texture, 725, 0, new ClickListener()
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
        customer = new Customer(customer_textures[2], 5);
        wip_drink = DrinkFactory.NamedDrink("Vanilla Late");

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
    void setupToppingButton(Texture t, int x_pos, int y_pos, ClickListener cl)
    {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        TextureRegionDrawable trd = new TextureRegionDrawable(t);

        // magic numbers! wahooo! we love magic numbers!
        trd.setMinSize(150, 150);

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

        // draw customer
        game.batch.draw(
                customer.texture,
                CUST_X - CUST_WIDTH / 2,
                CUST_Y,
                CUST_WIDTH,
                CUST_HEIGHT);

        // draw WIP drink
        drawDrink(wip_drink, WIP_DRINK_X, WIP_DRINK_Y);

        // draw customer drink TEMP
        if(customer !=  null)
            drawDrink(customer.request, CUST_X, CUST_Y + 450 + (int) (Math.sin(time_stamp / 100.0) * 25));

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

    }

    @Override
    public void dispose()
    {

    }

    void update(float delta)
    {
        // screen behavior
        stage.act(delta);

        customer.texture = customer_textures[(time_stamp / 50) % 3];

        time_stamp++;
    }
}
