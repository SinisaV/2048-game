package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.My2048Game;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.Score;
import com.mygdx.game.gameplay.stage.GameStage;

public class GameScreen extends ScreenAdapter {
    private final My2048Game game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;
    private final BitmapFont customFont;
    private final Music gameMusic;
    private int score = 0;

    private String playerName = "";


    public GameScreen(My2048Game game) {
        this.game = game;
        assetManager = game.getAssetManager();
        customFont = game.getCustomFont();

        gameMusic = assetManager.get(AssetDescriptors.GAME_MUSIC);
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);

        if (GameManager.INSTANCE.isMusicEnabled()) {
            gameMusic.play();
        }
    }

    @Override
    public void show() {
        viewport = new FitViewport(game.getWorldWidth(), game.getWorldHeight());
        stage = new GameStage(game, viewport, this);

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createButtonUI());
        stage.addActor(createScoreUI());
        //stage.addActor(createUI());

        Dialog playerInputDialog = createPlayerInputDialog();
        playerInputDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(240 / 255f, 240 / 255f, 240 / 255f, 1);
        stage.act();
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameManager gameManager = GameManager.INSTANCE;
            Score newScore = new Score(playerName, score);
            gameManager.saveScore(newScore);
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void hide() {
        dispose();

        if (GameManager.INSTANCE.isMusicEnabled()) {
            gameMusic.stop();
            gameMusic.dispose();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor createButtonUI() {

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

    private Actor createScoreUI() {
        Table table = new Table();
        table.top().padTop(100);

        Label.LabelStyle playerLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        customFont.getData().setScale(0.5f);

        Label playerLabel = new Label("Player: ", playerLabelStyle);
        Label playerValueLabel = new Label(playerName, playerLabelStyle);
        playerValueLabel.setName("playerLabel");

        table.add(playerLabel).left();
        table.add(playerValueLabel).right();

        Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
        customFont.getData().setScale(0.5f);

        Label scoreLabel = new Label("Score: ", scoreLabelStyle);
        Label scoreValueLabel = new Label(String.valueOf(score), scoreLabelStyle);
        scoreValueLabel.setName("scoreLabel");

        table.add(scoreLabel).left().padLeft(50);
        table.add(scoreValueLabel).right();

        if (GameManager.INSTANCE.isHighScoreEnabled()) {
            Label.LabelStyle highScoreLabelStyle = new Label.LabelStyle(customFont, new Color(0.45f, 0.45f, 0.45f, 1));
            customFont.getData().setScale(0.5f);

            Label highScoreLabel = new Label("High Score: ", highScoreLabelStyle);
            Label highScoreValueLabel = new Label(String.valueOf(GameManager.INSTANCE.getHighScore()), highScoreLabelStyle);

            table.add(highScoreLabel).left().padLeft(50);
            table.add(highScoreValueLabel).right();
        }

        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createUI() {
        Table gameTable = new Table();
        gameTable.center();

        final int ROWS = GameManager.INSTANCE.getGridSize();
        final int COLS = GameManager.INSTANCE.getGridSize();
        final int BLOCK_SIZE = 72;
        final float LINE_WIDTH = 2f;

        Color gridColor = new Color(0.7f, 0.7f, 0.7f, 1);

        int randomRow = MathUtils.random(ROWS - 1);
        int randomCol = MathUtils.random(COLS - 1);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Table block = new Table();
                block.setWidth(BLOCK_SIZE);
                block.setHeight(BLOCK_SIZE);

                // If this cell matches the random position, add the image
                if (row == randomRow && col == randomCol) {
                    Image image = new Image(gameplayAtlas.findRegion(RegionNames.BLOCK2));
                    block.add(image).width(BLOCK_SIZE).height(BLOCK_SIZE);
                }

                block.padBottom(row == ROWS - 1 ? 0 : LINE_WIDTH / 2);
                block.padRight(col == COLS - 1 ? 0 : LINE_WIDTH / 2);
                block.setBackground(new TextureRegionDrawable(createColorTexture(gridColor)));

                gameTable.add(block).width(BLOCK_SIZE).height(BLOCK_SIZE).pad(LINE_WIDTH / 2);
            }
            gameTable.row();
        }

        gameTable.pack();
        gameTable.setFillParent(true);

        return gameTable;
    }

    private Texture createColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Dialog createPlayerInputDialog() {
        final Dialog nameDialog = new Dialog("Enter Your Name", skin);
        final TextField playerNameField = new TextField("", skin);
        TextButton confirmButton = new TextButton("Confirm", skin);

        nameDialog.getContentTable().add(playerNameField).pad(20).row();
        nameDialog.getButtonTable().add(confirmButton).pad(20);

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String enteredName = playerNameField.getText();
                if (!enteredName.isEmpty()) {
                    playerName = enteredName;
                    updatePlayerLabel();
                    System.out.println("Entered name: " + playerName);
                }

                nameDialog.hide();
            }
        });

        nameDialog.setModal(true);

        return nameDialog;
    }

    private void updatePlayerLabel() {
        if (stage != null) {
            // Find the player label and update its text
            Label playerValueLabel = stage.getRoot().findActor("playerLabel");
            if (playerValueLabel != null) {
                playerValueLabel.setText(playerName);
            }
        }
    }

    public void updateScore(int newScore) {
        score = newScore;
        updateScoreLabel();
    }

    private void updateScoreLabel() {
        if (stage != null) {
            Label scoreValueLabel = stage.getRoot().findActor("scoreLabel");
            if (scoreValueLabel != null) {
                scoreValueLabel.setText(String.valueOf(score));
            }
        }
    }

    public String getPlayerName() {
        return playerName;
    }
}
