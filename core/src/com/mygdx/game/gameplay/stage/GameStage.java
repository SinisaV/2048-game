package com.mygdx.game.gameplay.stage;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.My2048Game;
import com.mygdx.game.gameplay.actor.GridGroup;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.Score;
import com.mygdx.game.gameplay.stage.custom.CustomStage;
import com.mygdx.game.screen.GameOverScreen;
import com.mygdx.game.screen.GameScreen;

public class GameStage extends CustomStage {
    private GridGroup gridGroup;

    private GameScreen gameScreen;

    private final My2048Game game;

    public GameStage(My2048Game mainGame, Viewport viewport, GameScreen gameScreen) {
        super(mainGame, viewport);
        this.gameScreen = gameScreen;
        this.game = mainGame;
        init();
    }

    private void init() {
        gridGroup = new GridGroup(getGame2048(), GameManager.INSTANCE);
        gridGroup.setPosition(
                getWidth() / 2 - gridGroup.getWidth() / 2,
                getHeight() / 2 - gridGroup.getHeight() / 2
        );
        addActor(gridGroup);
    }

    private int calculateCurrentScore() {

        int maxNumber = 0;
        int[][] data = gridGroup.getCoreModel().getData();
        for (int row = 0; row < gridGroup.getCARD_ROWS(); row++) {
            for (int col = 0; col < gridGroup.CARD_COLS; col++) {
                maxNumber = Math.max(maxNumber, data[row][col]);
            }
        }
        return maxNumber;
    }

    private boolean isGridFull() {
        int[][] data = gridGroup.getCoreModel().getData();
        for (int row = 0; row < gridGroup.getCARD_ROWS(); row++) {
            for (int col = 0; col < gridGroup.CARD_COLS; col++) {
                if (data[row][col] == 0) {
                    // There's an empty cell, so the grid is not full
                    return false;
                }
            }
        }
        // No empty cells found, grid is full
        return true;
    }

    private boolean canMerge() {
        int[][] data = gridGroup.getCoreModel().getData();
        for (int row = 0; row < gridGroup.getCARD_ROWS(); row++) {
            for (int col = 0; col < gridGroup.CARD_COLS; col++) {
                int currentValue = data[row][col];

                // Check if the neighboring cells can be merged
                if (row > 0 && currentValue == data[row - 1][col]) {
                    return true; // Merging possible with the cell above
                }
                if (row < gridGroup.getCARD_ROWS() - 1 && currentValue == data[row + 1][col]) {
                    return true; // Merging possible with the cell below
                }
                if (col > 0 && currentValue == data[row][col - 1]) {
                    return true; // Merging possible with the cell on the left
                }
                if (col < gridGroup.CARD_COLS - 1 && currentValue == data[row][col + 1]) {
                    return true; // Merging possible with the cell on the right
                }
            }
        }
        // No merges possible
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        int currentScore = calculateCurrentScore();
        gameScreen.updateScore(currentScore); // Update score in GameScreen

        if (isGameOver()) {
            Score score = new Score(gameScreen.getPlayerName(), currentScore);
            GameManager.INSTANCE.saveScore(score);
            game.setScreen(new GameOverScreen(game, currentScore, gameScreen.getPlayerName()));
        }
    }

    public boolean isGameOver() {
        return !canMerge() || isGridFull();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
