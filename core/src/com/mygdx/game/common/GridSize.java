package com.mygdx.game.common;

public enum GridSize {
    SIZE_THREExTHREE(3),
    SIZE_FOURxFOUR(4),
    SIZE_FIVExFIVE(5);

    private final int size;

    GridSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
