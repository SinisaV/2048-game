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

    private CheckBox checkBoxMenuMusic;
    private CheckBox checkBoxGameMusic;

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

        checkBoxMenuMusic = new CheckBox("Menu Music", skin);
        checkBoxMenuMusic.setChecked(GameManager.INSTANCE.isMenuMusicEnabled());
        checkBoxMenuMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = checkBoxMenuMusic.isChecked();
                GameManager.INSTANCE.setMenuMusicEnabled(isChecked);
            }
        });

        checkBoxGameMusic = new CheckBox("Game Music", skin);
        checkBoxGameMusic.setChecked(GameManager.INSTANCE.isGameMusicEnabled());
        checkBoxGameMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isChecked = checkBoxGameMusic.isChecked();
                GameManager.INSTANCE.setGameMusicEnabled(isChecked);
            }
        });

        Label gridSizeLabel = new Label("Grid Size", skin);
        gridSizeLabel.setFontScale(1.5f);
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

        table.add(checkBoxMenuMusic).padBottom(20).row();
        table.add(checkBoxGameMusic).padBottom(20).row();
        table.add(gridSizeLabel).padBottom(20).row();
        table.add(gridSizeSelectBox).width(200)
                .height(30).padBottom(20).row();

        table.setFillParent(true);
        table.pack();

        return table;
    }
}
