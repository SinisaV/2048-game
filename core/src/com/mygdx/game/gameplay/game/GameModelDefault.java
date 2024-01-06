package com.mygdx.game.gameplay.game;

import java.util.Random;

public class GameModelDefault implements GameModel {

    private final int rowSum;

    private final int colSum;

    private DataListener dataListener;

    private final int[][] data;

    private int currentScore;

    private GameState gameState = GameState.game;

    private final Random random;

    public GameModelDefault(int rowSum, int colSum, DataListener dataListener) {
        this.rowSum = rowSum;
        this.colSum = colSum;
        this.dataListener = dataListener;
        data = new int[rowSum][colSum];
        random = new Random();
    }

    @Override
    public void initData() {

        for (int row = 0; row < rowSum; row++) {
            for (int col = 0; col < colSum; col++) {
                data[row][col] = 0;
            }
        }

        currentScore = 0;
        gameState = GameState.game;

        randomGenerateNumber();
        randomGenerateNumber();
    }

    @Override
    public int[][] getData() {
        return data;
    }


    @Override
    public int getCurrentScore() {
        return currentScore;
    }

    @Override
    public void toUp() {
        if (gameState != GameState.game) {
            return;
        }

        boolean hasMove = false;

        for (int col = 0; col < colSum; col++) {
            for (int row = 0; row < rowSum; row++) {
                for (int tmpRow = row + 1; tmpRow < rowSum; tmpRow++) {
                    if (data[tmpRow][col] == 0) {
                        continue;
                    }
                    if (data[row][col] == 0) {
                        data[row][col] = data[tmpRow][col];
                        if(dataListener != null){
                            dataListener.onNumberMove(tmpRow,row,col,true);
                        }
                        hasMove = true;
                        data[tmpRow][col] = 0;
                        row--;
                    } else if (data[row][col] == data[tmpRow][col]) {
                        data[row][col] += data[tmpRow][col];
                        hasMove = true;
                        currentScore += data[row][col];
                        if (dataListener != null) {
                            dataListener.onNumberMove(tmpRow,row,col,true);
                            dataListener.onNumberMerge(row, col, data[row][col], currentScore);
                        }
                        data[tmpRow][col] = 0;
                    }
                    break;
                }
            }
        }

        if (hasMove) {

            randomGenerateNumber();

        }
    }

    @Override
    public void toDown() {

        if (gameState != GameState.game) {
            return;
        }

        boolean hasMove = false;

        for (int col = 0; col < colSum; col++) {
            for (int row = rowSum - 1; row > 0; row--) {
                for (int tmpRow = row - 1; tmpRow >= 0; tmpRow--) {
                    if (data[tmpRow][col] == 0) {
                        continue;
                    }
                    if (data[row][col] == 0) {
                        data[row][col] = data[tmpRow][col];
                        if(dataListener != null){
                            dataListener.onNumberMove(tmpRow,row,col,true);
                        }
                        hasMove = true;
                        data[tmpRow][col] = 0;
                        row++;
                    } else if (data[tmpRow][col] == data[row][col]) {
                        data[row][col] += data[tmpRow][col];
                        hasMove = true;
                        currentScore += data[row][col];
                        if (dataListener != null) {
                            dataListener.onNumberMove(tmpRow,row,col,true);
                            dataListener.onNumberMerge(row, col, data[row][col], currentScore);
                        }
                        data[tmpRow][col] = 0;
                    }
                    break;
                }
            }
        }

        if (hasMove) {
            randomGenerateNumber();
        }
    }

    @Override
    public void toLeft() {

        if (gameState != GameState.game) {
            return;
        }

        boolean hasMove = false;

        for (int row = 0; row < rowSum; row++) {
            for (int col = 0; col < colSum; col++) {
                for (int tmpCol = col + 1; tmpCol < colSum; tmpCol++) {
                    if (data[row][tmpCol] == 0) {
                        continue;
                    }
                    if (data[row][col] == 0) {
                        data[row][col] = data[row][tmpCol];
                        if(dataListener != null){
                            dataListener.onNumberMove(tmpCol,col,row,false);
                        }
                        hasMove = true;
                        data[row][tmpCol] = 0;
                        col--;
                    } else if (data[row][col] == data[row][tmpCol]) {
                        data[row][col] += data[row][col];
                        hasMove = true;
                        currentScore += data[row][col];
                        if (dataListener != null) {
                            dataListener.onNumberMove(tmpCol,col,row,false);
                            dataListener.onNumberMerge(row, col, data[row][col], currentScore);
                        }
                        data[row][tmpCol] = 0;
                    }
                    break;
                }
            }
        }

        if (hasMove) {
            randomGenerateNumber();
        }
    }

    @Override
    public void toRight() {

        if (gameState != GameState.game) {
            return;
        }

        boolean hasMove = false;

        for (int row = 0; row < rowSum; row++) {
            for (int col = colSum - 1; col >= 0; col--) {
                for (int tmpCol = col - 1; tmpCol >= 0; tmpCol--) {
                    if (data[row][tmpCol] == 0) {
                        continue;
                    }
                    if (data[row][col] == 0) {
                        data[row][col] = data[row][tmpCol];
                        if(dataListener != null){
                            dataListener.onNumberMove(tmpCol,col,row,false);
                        }
                        hasMove = true;
                        data[row][tmpCol] = 0;
                        col++;
                    } else if (data[row][col] == data[row][tmpCol]) {
                        data[row][col] += data[row][col];
                        hasMove = true;
                        currentScore += data[row][col];
                        if (dataListener != null) {
                            dataListener.onNumberMove(tmpCol,col,row,false);
                            dataListener.onNumberMerge(row, col, data[row][col], currentScore);
                        }
                        data[row][tmpCol] = 0;
                    }
                    break;
                }
            }
        }

        if (hasMove) {
            randomGenerateNumber();
        }

    }

    private void randomGenerateNumber() {

        int emptyCardsCount = 0;
        for (int row = 0; row < rowSum; row++) {
            for (int col = 0; col < colSum; col++) {
                if (data[row][col] == 0) {
                    emptyCardsCount++;
                }
            }
        }

        if (emptyCardsCount == 0) {
            if (dataListener != null) {
                dataListener.onGameOver(false);
            }
        }

        int newNumPosition = random.nextInt(emptyCardsCount);
        float newTwoProbability = 0.8f;
        int newNum = random.nextFloat() < newTwoProbability ? 2 : 4;
        int emptyCardPosition = 0;
        for (int row = 0; row < rowSum; row++) {
            for (int col = 0; col < colSum; col++) {
                if (data[row][col] != 0) {
                    continue;
                }
                if (emptyCardPosition == newNumPosition) {
                    data[row][col] = newNum;
                    if (dataListener != null) {
                        dataListener.onGenerateNumber(row, col, newNum);
                    }
                }
                emptyCardPosition++;
            }
        }
    }
}
