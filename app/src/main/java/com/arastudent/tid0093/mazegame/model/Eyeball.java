package com.arastudent.tid0093.mazegame.model;

class Eyeball {
    private int row;
    private int column;
    private Direction direction;

    //stores the eyeballs position and direction
    public Eyeball(int row, int column, Direction direction) {
        this.row = row;
        this.column = column;
        this.direction = direction;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Direction getDirection() {
        return direction;
    }

	public void setRow(int destinationRow) {
		this.row = destinationRow;
	}

	public void setColumn(int destinationColumn) {
		this.column = destinationColumn;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
