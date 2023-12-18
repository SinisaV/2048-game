package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
import com.mygdx.game.common.Score;
import com.mygdx.game.config.GameConfig;

public class LeaderboardScreen extends ScreenAdapter {

    private final My2048Game game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private final BitmapFont customFont;

    public LeaderboardScreen(My2048Game game) {
        this.game = game;
        assetManager = game.getAssetManager();
        customFont = game.getCustomFont();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createUI());
        stage.addActor(createLeaderboard());
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

        TextButton menuButton = new TextButton("Back", skin);
        menuButton.addListener(new ClickListener() {
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

        Table buttonTable = new Table(skin);
        buttonTable.top().pad(20);

        buttonTable.add(menuButton).left().expandX();
        buttonTable.add(quitButton).right().expandX();

        table.add(buttonTable).expand().fill();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createLeaderboard() {
        Table mainTable = new Table();
        mainTable.center().top().padTop(100);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        customFont.getData().setScale(0.5f);

        Label titleLabel = new Label("Leaderboard", titleLabelStyle);
        mainTable.add(titleLabel).colspan(2).padBottom(30);
        mainTable.row();

        Table leaderboardTable = new Table();
        leaderboardTable.defaults().height(30);

        Label playerNameLabel = new Label("Name", skin);
        playerNameLabel.setFontScale(1.5f);
        playerNameLabel.setColor(Color.BLACK);
        leaderboardTable.add(playerNameLabel).padRight(50);

        Label myScoreLabel = new Label("Score", skin);
        myScoreLabel.setFontScale(1.5f);
        myScoreLabel.setColor(Color.BLACK);
        leaderboardTable.add(myScoreLabel);
        leaderboardTable.row().padBottom(20);

        String[] playerNames = {"Player1", "Player2", "Player3", "Player4", "Player5",
                "Player6", "Player7", "Player8", "Player9", "Player10",
                "Player11", "Player12", "Player13", "Player14", "Player15",
                "Player16", "Player17", "Player18", "Player19", "Player20"};

        int[] scores = {100, 80, 120, 70, 80, 90, 100, 110, 120, 130,
                85, 75, 95, 105, 115, 88, 92, 100, 120, 130};

        leaderboardTable.row().padTop(20);

        for (Score score : GameManager.INSTANCE.sortScores()) {
            Label nameLabel = new Label(score.getPlayerName(), skin);
            nameLabel.setColor(Color.BLACK);
            leaderboardTable.add(nameLabel).padRight(50);

            Label scoreLabel = new Label(String.valueOf(score.getScore()), skin);
            scoreLabel.setColor(Color.BLACK);
            leaderboardTable.add(scoreLabel);
            leaderboardTable.row().padBottom(20);
        }

        ScrollPane scrollPane = new ScrollPane(leaderboardTable, skin);
        scrollPane.setFadeScrollBars(false);

        Table containerTable = new Table();
        containerTable.add(scrollPane).expand().fill();

        mainTable.add(containerTable).width(600).height(400).expand().fill();

        mainTable.pack();
        mainTable.setFillParent(true);

        return mainTable;
    }
}
