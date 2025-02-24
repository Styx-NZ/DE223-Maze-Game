package com.arastudent.tid0093.mazegame.model;

//current position (of the eyeball)
public class Position {
	private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
