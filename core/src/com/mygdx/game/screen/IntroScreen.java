package com.mygdx.game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.My2048Game;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;

public class IntroScreen extends ScreenAdapter {
    public static final float INTRO_DURATION_IN_SEC = 5f;
    public static final float INTRO_DURATION_IN_SEC3 = 3f;

    private final My2048Game game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;

    private float duration = 0f;

    private Stage stage;

    public IntroScreen(My2048Game game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // load assets
        assetManager.load(AssetDescriptors.UI_FONT);
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.MENU_MUSIC);
        assetManager.load(AssetDescriptors.GAME_MUSIC);
        assetManager.load(AssetDescriptors.MERGE_SOUND);
        assetManager.load(AssetDescriptors.MOVE_SOUND);
        assetManager.finishLoading();   // blocks until all assets are loaded

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        //stage.addActor(createAnimation());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(240 / 255f, 240 / 255f, 240 / 255f, 1);

        duration += delta;

        //if (duration >= INTRO_DURATION_IN_SEC) {
        game.setScreen(new MenuScreen(game));
        //}

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

    private Actor createAnimation() {
        final float targetSize = 100f;
        final Image image1 = new Image(gameplayAtlas.findRegion(RegionNames.BLOCK2));
        image1.setSize(targetSize, targetSize);
        final Image image2 = new Image(gameplayAtlas.findRegion(RegionNames.BLOCK4));
        image2.setSize(targetSize, targetSize);
        final Image image3 = new Image(gameplayAtlas.findRegion(RegionNames.BLOCK8));
        image3.setSize(targetSize, targetSize);
        final Image image4 = new Image(gameplayAtlas.findRegion(RegionNames.BLOCK16));
        image4.setSize(targetSize, targetSize);

        image1.setPosition(0, GameConfig.HUD_HEIGHT / 2 - image1.getHeight() / 2, Align.bottomLeft);
        image2.setPosition(GameConfig.HUD_WIDTH - image2.getWidth(), GameConfig.HUD_HEIGHT / 2 - image2.getHeight() / 2, Align.bottomRight);
        image3.setPosition(GameConfig.HUD_WIDTH / 2 - image3.getWidth() / 2, 0, Align.bottomLeft);
        image4.setPosition(GameConfig.HUD_WIDTH / 2 - image4.getWidth() / 2, GameConfig.HUD_HEIGHT - image4.getHeight(), Align.topLeft);

        float duration = INTRO_DURATION_IN_SEC3;

        image1.addAction(Actions.sequence(
                Actions.moveTo(GameConfig.HUD_WIDTH / 2 - image1.getWidth() / 2, GameConfig.HUD_HEIGHT / 2 - image1.getHeight() / 2, duration)
        ));

        image2.addAction(Actions.sequence(
                Actions.moveTo(GameConfig.HUD_WIDTH / 2 - image2.getWidth() / 2, GameConfig.HUD_HEIGHT / 2 - image2.getHeight() / 2, duration)
        ));

        image3.addAction(Actions.sequence(
                Actions.moveTo(GameConfig.HUD_WIDTH / 2 - image3.getWidth() / 2, GameConfig.HUD_HEIGHT / 2 - image3.getHeight() / 2, duration)
        ));

        image4.addAction(Actions.sequence(
                Actions.moveTo(GameConfig.HUD_WIDTH / 2 - image4.getWidth() / 2, GameConfig.HUD_HEIGHT / 2 - image4.getHeight() / 2, duration),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        checkCollisionAndDisplayFinalImage(image1, image2);
                    }
                })
        ));

        stage.addActor(image1);
        stage.addActor(image2);
        stage.addActor(image3);
        stage.addActor(image4);

        return image1;
    }


    private void checkCollisionAndDisplayFinalImage(Image image1, Image image2) {
        float image1X = image1.getX();
        float image1Y = image1.getY();
        float image1Width = image1.getWidth();
        float image1Height = image1.getHeight();

        float image2X = image2.getX();
        float image2Y = image2.getY();
        float image2Width = image2.getWidth();
        float image2Height = image2.getHeight();

        if (image1X < image2X + image2Width &&
                image1X + image1Width > image2X &&
                image1Y < image2Y + image2Height &&
                image1Y + image1Height > image2Y) {
            // Collision occurred
            final Image thirdImage = createNewImageAtCenter();
            stage.addActor(thirdImage);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    thirdImage.remove();
                }
            }, 2f);
        }
    }

    private Image createNewImageAtCenter() {
        final float targetSize = 100f;
        Image newImage = new Image(gameplayAtlas.findRegion(RegionNames.BLOCK2048));
        newImage.setSize(targetSize, targetSize);
        newImage.setPosition(GameConfig.HUD_WIDTH / 2 - newImage.getWidth() / 2, GameConfig.HUD_HEIGHT / 2 - newImage.getHeight() / 2);
        return newImage;
    }

}