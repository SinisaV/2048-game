package com.mygdx.game.gameplay.game;

public interface GameModel {
    enum GameState {

        game

    }

    void initData();

    int[][] getData();

    int getCurrentScore();

    void toUp();

    void toDown();

    void toLeft();

    void toRight();

    interface DataListener {

        void onGenerateNumber(int row, int col, int num);

        void onNumberMove(int begin, int end, int col, boolean isUpOrDown);

        void onNumberMerge(int rowAfterMerge, int colAfterMerge, int numAfterMerge, int currentScoreAfterMerger);

        void onGameOver(boolean isWin);
    }

    class Builder {
        public static GameModel createDataModel(int rowSum, int colSum, DataListener dataListener) {
            return new GameModelDefault(rowSum, colSum, dataListener);
        }
    }
}
