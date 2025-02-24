package com.arastudent.tid0093.mazegame.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int height;
    private int width;
    private List<Goal> goals;
    private Square[][] squares;
    private List<Goal> completedGoals;

    public Level(int height, int width) {
        this.height = height;
        this.width = width;
        this.goals = new ArrayList<>();
        this.squares = new Square[height][width];
        this.completedGoals = new ArrayList<>();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void addGoal(int row, int column) {
    	//check if the position is within the level
    	if (row < 0 || row >= height || column < 0 || column >= width) {
            throw new IllegalArgumentException("Goal position is outside the level boundaries");
        }
    	//at a valid goal
        goals.add(new Goal(row, column));
    }

    public int getGoalCount() {
        return goals.size();
    }

    public boolean hasGoalAt(int targetRow, int targetColumn) {
    	//check if there is a goal at the target position
        for (Goal goal : goals) {
            if (goal.getRow() == targetRow && goal.getColumn() == targetColumn) {
                return true;
            }
        }
        return false;
    }

    public int getCompletedGoalCount() {
    	//return the amount of completed goals
    	return completedGoals.size();
    }
    
    public void addSquare(Square square, int row, int column) {
    	//check if the position is valid
        if (row < 0 || row >= height || column < 0 || column >= width) {
            throw new IllegalArgumentException("Square position is outside the level boundaries");
        }
        //add a square to the position
        squares[row][column] = square;
    }

    public Square getSquareAt(int row, int column) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            throw new IllegalArgumentException("Square position is outside the level boundaries");
        }
        return squares[row][column];
    }
    
    public Color getColorAt(int row, int column) {
    	//if there is a square there return the colour
        if (squares[row][column] != null) {
            return squares[row][column].getColor();
        }
        //if there is no square there
        return null;
    }

    public Shape getShapeAt(int row, int column) {
    	//if there is a square there return the shape
        if (squares[row][column] != null) {
            return squares[row][column].getShape();
        }
        //if there is no square there
        return null;
    }
    
    public void completeGoal(int row, int column) {
    	//go through all the goals if the goal is matchs the row and column
    	//set the goal to completed, remove the goal, add the goal to the completed list
        for (int i = 0; i < goals.size(); i++) {
        	Goal goal = goals.get(i);
            if (goal.getRow() == row && goal.getColumn() == column) {
                goal.setCompleted(true);
                this.removeGoal(row, column);
                this.completedGoals.add(goal);
                return;
            }
        }
    }
    
    public void removeGoal(int row, int column) {
    	//if the goal matchs the row and column remove it
        for (int i = 0; i < goals.size(); i++) {
            Goal goal = goals.get(i);
            if (goal.getRow() == row && goal.getColumn() == column) {
                goals.remove(i);
                return;
            }
        }
    }
    
    public void setGoalsToBlank() {
    	//set completed goals to BLANK as directed
    	for (Goal completedGoal : completedGoals) {
            if (completedGoal.isCompleted()) {
                squares[completedGoal.getRow()][completedGoal.getColumn()] = new BlankSquare();
            }
        }
    }
}