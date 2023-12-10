package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Logger;

import com.mygdx.game.screen.IntroScreen;

public class My2048Game extends Game {

	private AssetManager assetManager;
	SpriteBatch batch;
	private BitmapFont customFont;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 64; // Set the font size, adjust as needed
		customFont = generator.generateFont(parameter);
		generator.dispose(); // Dispose the generator after generating the font


		batch = new SpriteBatch();

		setScreen(new IntroScreen(this));
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
		batch.dispose();
		customFont.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getCustomFont() {
		return customFont;
	}
}
