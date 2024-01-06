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

public class GameOverScreen extends ScreenAdapter {
    private final My2048Game game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;

    private final BitmapFont customFont;

    private final String playerName;
    private final int score;

    private final String winOrLose;

    public GameOverScreen(My2048Game game, int score, String playerName, String winOrLose) {
        this.game = game;
        assetManager = game.getAssetManager();
        customFont = game.getCustomFont();
        this.score = score;
        this.playerName = playerName;
        this.winOrLose = winOrLose;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);

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
    }

    private Actor createUI() {
        Table table = new Table();
        table.defaults().pad(20);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        Label titleLabel;
        if (winOrLose.equals("You Win!")) {
            titleLabel = new Label("YOU WIN", titleLabelStyle);
        }
        else {
            titleLabel = new Label("GAME OVER", titleLabelStyle);
        }
        table.add(titleLabel).top().colspan(3).padTop(100).row();

        Label.LabelStyle playerLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        Label playerLabel = new Label("Player: " + playerName, playerLabelStyle);

        table.add(playerLabel).top().colspan(3).padTop(60).row();

        Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        Label scoreLabel = new Label("Score: " + score, scoreLabelStyle);

        table.add(scoreLabel).top().colspan(3).padTop(20).row();

        TextButton settingsButton = new TextButton("Main Menu", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
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
