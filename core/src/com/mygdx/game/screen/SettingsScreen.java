package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.My2048Game;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.GridSize;
import com.mygdx.game.config.GameConfig;

public class SettingsScreen extends ScreenAdapter {
    private final My2048Game game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private CheckBox checkBoxMusic;

    private CheckBox checkBoxSound;

    private CheckBox checkBoxHighScore;

    public SettingsScreen(My2048Game game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createButtonUI());
        stage.addActor(createUI());
        stage.addActor(createCheckBoxUI());
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
    private Actor createUI() {
        Table table = new Table();
        table.defaults().pad(20);

        checkBoxMusic = new CheckBox("Game Music", skin);
        checkBoxMusic.setChecked(GameManager.INSTANCE.isMusicEnabled());
        checkBoxMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = checkBoxMusic.isChecked();
                GameManager.INSTANCE.setMusicEnabled(isChecked);
            }
        });

        checkBoxSound = new CheckBox("Game Sound", skin);
        checkBoxSound.setChecked(GameManager.INSTANCE.isSoundEnabled());
        checkBoxSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = checkBoxSound.isChecked();
                GameManager.INSTANCE.setSoundEnabled(isChecked);
            }
        });

        checkBoxHighScore = new CheckBox("Display High Score", skin);
        checkBoxHighScore.setChecked(GameManager.INSTANCE.isHighScoreEnabled());
        checkBoxHighScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = checkBoxHighScore.isChecked();
                GameManager.INSTANCE.setHighScoreEnabled(isChecked);
            }
        });

        table.add(checkBoxMusic).padBottom(20).align(Align.left).row();
        table.add(checkBoxSound).padBottom(20).align(Align.left).row();
        table.add(checkBoxHighScore).padBottom(20).align(Align.left).row();

        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createCheckBoxUI() {
        Table table = new Table();
        table.bottom().pad(160);

        Label gridSizeLabel = new Label("Grid Size", skin);
        gridSizeLabel.setFontScale(1.3f);
        // Create a list of grid sizes
        String[] gridSizeNames = new String[GridSize.values().length];
        for (int i = 0; i < GridSize.values().length; i++) {
            gridSizeNames[i] = GridSize.values()[i].name();
        }

        final SelectBox<String> gridSizeSelectBox = new SelectBox<>(skin);
        gridSizeSelectBox.setItems(gridSizeNames);
        gridSizeSelectBox.setSelectedIndex(0);

        int savedGridSize = GameManager.INSTANCE.getGridSize();
        GridSize selectedGridSize = null;

        for (GridSize gridSize : GridSize.values()) {
            if (gridSize.getSize() == savedGridSize) {
                selectedGridSize = gridSize;
                break;
            }
        }

        if (selectedGridSize != null) {
            gridSizeSelectBox.setSelected(selectedGridSize.name());
        }
        gridSizeSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedSize = gridSizeSelectBox.getSelected();
                GridSize chosenGridSize = GridSize.valueOf(selectedSize);
                int size = chosenGridSize.getSize();
                System.out.println("Selected Grid Size: " + size);
                GameManager.INSTANCE.setGridSize(size);
            }
        });

        table.add(gridSizeLabel).padRight(20);
        table.add(gridSizeSelectBox).width(200)
                .height(30);

        table.setFillParent(true);
        table.pack();

        return table;
    }
}
