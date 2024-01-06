package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.screen.IntroScreen;

public class My2048Game extends Game {

	private AssetManager assetManager;
	SpriteBatch batch;
	private BitmapFont customFont;

	private TextureAtlas atlas;

	private float worldWidth;
	private float worldHeight;
	
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

		assetManager.load(AssetDescriptors.GAMEPLAY);
		assetManager.finishLoading();
		atlas = assetManager.get(AssetDescriptors.GAMEPLAY);

		worldWidth = 1420;
		worldHeight = Gdx.graphics.getHeight() * worldWidth / Gdx.graphics.getWidth();

		/*GameManager gameManager = GameManager.INSTANCE;

		for (int i = 1; i <= 10; i++) {
			String playerName = "Player" + i;
			int score = i * 100; // Assigning scores (e.g., Player1: 100, Player2: 200, etc.)
			Score newScore = new Score(playerName, score);
			gameManager.saveScore(newScore);
		}*/

		String path = Gdx.files.local("scores.json").file().getAbsolutePath();
		Gdx.app.log("FilePath", "Scores JSON file path: " + path);

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

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public float getWorldWidth() {
		return worldWidth;
	}

	public float getWorldHeight() {
		return worldHeight;
	}
}
