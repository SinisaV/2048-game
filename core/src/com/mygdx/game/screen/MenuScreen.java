package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.My2048Game;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.config.GameConfig;

public class MenuScreen extends ScreenAdapter {

    private final My2048Game game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private final Music menuMusic;

    private final BitmapFont customFont;

    public MenuScreen(My2048Game game) {
        this.game = game;
        assetManager = game.getAssetManager();
        customFont = game.getCustomFont();

        menuMusic = assetManager.get(AssetDescriptors.MENU_MUSIC);
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        if (GameManager.INSTANCE.isMusicEnabled()) {
            menuMusic.play();
        }
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createUI());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(240 / 255f, 240 / 255f, 240 / 255f, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (menuMusic != null) {
            menuMusic.stop();
            menuMusic.dispose();
        }
    }

    private Actor createUI() {
        Table table = new Table();
        table.defaults().pad(20);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        Label titleLabel = new Label("2048 GAME", titleLabelStyle);

        table.add(titleLabel).top().colspan(3).padTop(100).row();


        TextButton.TextButtonStyle defaultTextButtonStyle = new TextButton.TextButtonStyle();
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);

        buttonTable.add(playButton).padBottom(15).expandX().fill().row();
        buttonTable.add(leaderboardButton).padBottom(15).fillX().row();
        buttonTable.add(settingsButton).padBottom(15).fillX().row();
        buttonTable.add(quitButton).fillX();

        buttonTable.center();

        table.add(buttonTable).expand().fill();
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
