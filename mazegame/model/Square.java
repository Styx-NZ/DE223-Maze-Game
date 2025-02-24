package com.arastudent.tid0093.mazegame.model;

//the square
public class Square {
	private Color color;
    private Shape shape;

    public Square(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }
}
