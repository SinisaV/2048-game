package com.mygdx.game.gameplay.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.My2048Game;
import com.mygdx.game.gameplay.actor.custom.CustomGroup;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.gameplay.game.GameModel;

public class GridGroup extends CustomGroup {
    private int CARD_ROWS = 5;
    private int CARD_COLS = 5;

    private GameManager gameManager;

    private Image myBgImage;

    private final CardGroup[][] myBgCards = new CardGroup[CARD_ROWS][CARD_COLS];

    private final CardGroup[][] cards = new CardGroup[CARD_ROWS][CARD_COLS];

    private GameModel gameModel;

    private Sound mergeSound;
    private Sound moveSound;

    public GridGroup(My2048Game mainGame, GameManager gameManager) {
        super(mainGame);
        this.gameManager = gameManager;
        init();
    }

    private void init() {
        CARD_ROWS = GameManager.INSTANCE.getGridSize();
        CARD_COLS = GameManager.INSTANCE.getGridSize();
        myBgImage = new Image(getGame2048().getAssetManager().get(AssetDescriptors.GAMEPLAY).findRegion(RegionNames.GRID_BACKGROUND));
        addActor(myBgImage);

        setSize(myBgImage.getWidth(), myBgImage.getHeight());

        for (int row = 0; row < CARD_ROWS; row++) {
            for (int col = 0; col < CARD_COLS; col++) {
                myBgCards[row][col] = new CardGroup(getGame2048());
                addActor(myBgCards[row][col]);
                cards[row][col] = new CardGroup(getGame2048());
                cards[row][col].setOrigin(Align.center);
                addActor(cards[row][col]);
            }
        }

        float cardWidth = myBgCards[0][0].getWidth();
        float cardHeight = myBgCards[0][0].getHeight();

        float horizontalInterval = (getWidth() - CARD_COLS * cardWidth) / (CARD_COLS + 1);
        float verticalInterval = (getHeight() - CARD_ROWS * cardHeight) / (CARD_ROWS + 1);

        float cardY;
        for (int row = 0; row < CARD_ROWS; row++) {
            cardY = getHeight() - (verticalInterval + cardHeight) * (row + 1);
            for (int col = 0; col < CARD_COLS; col++) {
                myBgCards[row][col].setPosition(
                        horizontalInterval + (cardWidth + horizontalInterval) * col,
                        cardY
                );
                cards[row][col].setPosition(
                        horizontalInterval + (cardWidth + horizontalInterval) * col,
                        cardY
                );
            }
        }

        addListener(new InputListenerImpl());

        gameModel = GameModel.Builder.createDataModel(CARD_ROWS, CARD_COLS, new DataListenerImpl());
        gameModel.initData();
        addDataToCardGroup();

        mergeSound = getGame2048().getAssetManager().get(AssetDescriptors.MERGE_SOUND);
        moveSound = getGame2048().getAssetManager().get(AssetDescriptors.MOVE_SOUND);
    }

    public GameModel getCoreModel() {
        return gameModel; // Assuming coreModel is a member variable in GridGroup
    }

    private void addDataToCardGroup() {
        int[][] data = gameModel.getData();
        for (int row = 0; row < CARD_ROWS; row++) {
            for (int col = 0; col < CARD_COLS; col++) {
                cards[row][col].setNumber(data[row][col]);
            }
        }
    }

    public void toUp() {
        gameModel.toUp();
        if (gameManager.isSoundEnabled()) {
            moveSound.play();
        }
    }

    public void toDown() {
        gameModel.toDown();
        if (gameManager.isSoundEnabled()) {
            moveSound.play();
        }
    }

    public void toLeft() {
        gameModel.toLeft();
        if (gameManager.isSoundEnabled()) {
            moveSound.play();
        }
    }

    public void toRight() {
        gameModel.toRight();
        if (gameManager.isSoundEnabled()) {
            moveSound.play();
        }
    }

    private class InputListenerImpl extends InputListener {

        private float touchDownX;
        private float touchDownY;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            touchDownX = x;
            touchDownY = y;
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            float moveX = x - touchDownX;
            float moveY = y - touchDownY;
            if (Math.abs(moveX) >= 20 && Math.abs(moveX) > Math.abs(moveY)) {
                if (moveX > 0) {
                    toRight();
                } else {
                    toLeft();
                }
            } else if (Math.abs(moveY) >= 20 && Math.abs(moveY) > Math.abs(moveX)) {
                if (moveY > 0) {
                    toUp();
                } else {
                    toDown();
                }
            }
        }
    }

    private class DataListenerImpl implements GameModel.DataListener {

        @Override
        public void onGenerateNumber(int row, int col, int num) {
            cards[row][col].setScale(0.2f);
            ScaleToAction scaleTo = Actions.scaleTo(1.0f, 1.0f, 0.2f);
            cards[row][col].addAction(scaleTo);
        }

        @Override
        public void onNumberMove(final int begin, final int end, final int fixed,boolean isUpOrDown) {

            final int beginRow;
            final int beginCol;
            final float beginX;
            final float beginY;
            float endX;
            float endY;

            if(isUpOrDown){
                beginRow=begin;
                beginCol=fixed;
                beginX = cards[begin][fixed].getX();
                beginY = cards[begin][fixed].getY();
                endX = myBgCards[end][fixed].getX();
                endY = myBgCards[end][fixed].getY();
            }else{
                beginRow=fixed;
                beginCol=begin;
                beginX=cards[fixed][begin].getX();
                beginY=cards[fixed][begin].getY();
                endX = myBgCards[fixed][end].getX();
                endY = myBgCards[fixed][end].getY();
            }

            SequenceAction moveToAction = Actions.sequence(
                    Actions.moveTo(endX, endY, 0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            cards[beginRow][beginCol].setPosition(beginX, beginY);
                            addDataToCardGroup();
                        }
                    })
            );
            cards[beginRow][beginCol].toFront();
            cards[beginRow][beginCol].addAction(moveToAction);
        }

        @Override
        public void onNumberMerge(int rowAfterMerge, int colAfterMerge, int numAfterMerge, int currentScoreAfterMerger) {
            cards[rowAfterMerge][colAfterMerge].setScale(0.8f);
            SequenceAction sequenceAction = Actions.sequence(
                    Actions.scaleTo(1.2f, 1.2f, 0.1f),
                    Actions.scaleTo(1.0f, 1.0f, 0.1f)
            );
            cards[rowAfterMerge][colAfterMerge].addAction(sequenceAction);

            if (gameManager.isSoundEnabled()) {
                mergeSound.play();
            }
        }

        @Override
        public void onGameOver(boolean isWin) {
        }
    }

    public int getCARD_COLS() {
        return CARD_COLS;
    }

    public int getCARD_ROWS() {
        return CARD_ROWS;
    }
}
