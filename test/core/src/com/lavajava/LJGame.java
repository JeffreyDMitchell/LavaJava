package com.lavajava;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class LJGame extends Game
{
	public static final int RES_X = 2340;
	public static final int RES_Y = 1080;
	public static final Color SKY_COLOR  = new Color(0.734f, 0.84f, 0.95f, 1);

	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create()
	{
		// setup batch, will be used to encapsulate jobs to render. contrivance of OpenGL
		batch = new SpriteBatch();

		// will enable us to draw text to screen
		font = new BitmapFont();
		font.getData().setScale(7, 7);

		// init game to main menu screen
		this.setScreen(MainMenuScreen.getInstance(this));
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}
}
