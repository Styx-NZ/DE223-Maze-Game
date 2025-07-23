package com.arastudent.tid0093.mazegame.model;

import java.util.ArrayList;
import java.util.List;

public class Game implements ILevelHolder, IGoalHolder, ISquareHolder, IEyeballHolder, IMoving {
    private List<Level> levels;
    private Level currentLevel;
	private int currentLevelNumber;
    private Eyeball eyeball;

    public Game() {
        levels = new ArrayList<>();
    }

    //create a level with a specified height and width
    @Override
    public void addLevel(int height, int width) {
        currentLevel = new Level(height, width);
        //sets the level created as the current level
        levels.add(currentLevel);
    }

    @Override
    public int getLevelWidth() {
        return currentLevel.getWidth();
    }

    @Override
    public int getLevelHeight() {
        return currentLevel.getHeight();
    }

    @Override
    public void setLevel(int levelNumber) {
    	//check if the number given is valid
		if (levelNumber > levels.size() || levelNumber >= levels.size()) {
            throw new IllegalArgumentException("Invalid level number");
        }
        //set the current level to the one specified
		currentLevelNumber = levelNumber + 1;
        currentLevel = levels.get(levelNumber);
    }

	public int getCurrentLevel(){ return currentLevelNumber; }

    @Override
    public int getLevelCount() {
        return levels.size();
    }

    @Override
    public void addGoal(int row, int column) {
    	//add a goal to the current level
        currentLevel.addGoal(row, column);
    }

    @Override
    public int getGoalCount() {
    	//get a count of how many goals are on the current level
        return currentLevel.getGoalCount();
    }

    @Override
    public boolean hasGoalAt(int targetRow, int targetColumn) {
    	//check if there is a goal at a target location
        return currentLevel.hasGoalAt(targetRow, targetColumn);
    }

    @Override
    public int getCompletedGoalCount() {
    	//Gets the number of goals that have been completed
        return currentLevel.getCompletedGoalCount();
    }

    @Override
    public void addSquare(Square square, int row, int column) {
        //Check if row and column are within the range of the current level
        if (row < 0 || row >= currentLevel.getHeight() || column < 0 || column >= currentLevel.getWidth()) {
            throw new IllegalArgumentException("Square position is outside the level boundaries");
        }
        //Add a square to the specified location
        currentLevel.addSquare(square, row, column);
    }

	public Square getSquare (int row, int column){
		return currentLevel.getSquareAt(row, column);
	}

    @Override
    public Color getColorAt(int row, int column) {
        //Retrieve the colour of the square at the specified position
        Color square = currentLevel.getColorAt(row, column);
        //Return the colour of the square
        return square;
    }

    @Override
    public Shape getShapeAt(int row, int column) {
        //Retrieve the square at the square at the specified position
        Shape square = currentLevel.getShapeAt(row, column);
        //Return the shape
        return square;
    }

    //Eyeball can be managed from Game because it is only relevant to the current game/level
	@Override
	public void addEyeball(int row, int column, Direction direction) {
		//check if position is valid for the level
		if (row < 0 || row >= currentLevel.getHeight() || column < 0 || column >= currentLevel.getWidth()) {
            throw new IllegalArgumentException("Eyeball position is outside the level boundaries");
        }
		//add the eyeball
		this.eyeball = new Eyeball(row, column, direction);

	}

	@Override
	public int getEyeballRow() {
		return eyeball.getRow();
	}

	@Override
	public int getEyeballColumn() {return eyeball.getColumn();}

	@Override
	public Direction getEyeballDirection() {
		return eyeball.getDirection();
	}

	@Override
	public boolean canMoveTo(int destinationRow, int destinationColumn) {
		//check if position is valid and fits within the level
	    if (destinationRow < 0 || destinationRow >= currentLevel.getHeight() ||
	        destinationColumn < 0 || destinationColumn >= currentLevel.getWidth()) {
	        return false;
	    }

	    //if both the row and column change its a diagonal move - not allowed
	    if (destinationRow != eyeball.getRow() && destinationColumn != eyeball.getColumn()) {
        	return false;
        }
	    //if the direction is not ok return false
	    if (!this.isDirectionOK(destinationRow, destinationColumn)) {
	    	return false;
	    }

	    //retrieve the colour and shape at destination
	    Color destinationColor = getColorAt(destinationRow, destinationColumn);
	    Shape destinationShape = getShapeAt(destinationRow, destinationColumn);

	    //retrieve the colour and shape at current location
	    Color currentColor = getColorAt(eyeball.getRow(), eyeball.getColumn());
	    Shape currentShape = getShapeAt(eyeball.getRow(), eyeball.getColumn());

	    //if the colour or the shape is the same return true else false
	    return destinationColor == currentColor || destinationShape == currentShape;
	}

	@Override
	public Message MessageIfMovingTo(int destinationRow, int destinationColumn) {
		//if you can move to the destination OK else Different shape or color
		if (canMoveTo(destinationRow,destinationColumn)) {
			return Message.OK;
		} else {
			return Message.DIFFERENT_SHAPE_OR_COLOR;
		}
	}

	@Override
	public boolean isDirectionOK(int destinationRow, int destinationColumn) {
		//get current row column and direction
	    int currentRow = eyeball.getRow();
	    int currentColumn = eyeball.getColumn();
	    Direction direction = eyeball.getDirection();

	    //Check if the destination is within the range of the level
	    if (destinationRow < 0 || destinationRow >= currentLevel.getHeight() ||
	        destinationColumn < 0 || destinationColumn >= currentLevel.getWidth()) {
	        return false;
	    }

	    //Check if the move is diagonal
	    if (destinationRow != currentRow && destinationColumn != currentColumn) {
	        return false;
	    }

	    //Check if the move is backwards could use switch case
	    if (direction == Direction.UP && destinationRow > currentRow) {
	        return false;
	    } else if (direction == Direction.DOWN && destinationRow < currentRow) {
	        return false;
	    } else if (direction == Direction.LEFT && destinationColumn > currentColumn) {
	        return false;
	    } else if (direction == Direction.RIGHT && destinationColumn < currentColumn) {
	        return false;
	    }
	    return true;
	}

	@Override
	public Message checkDirectionMessage(int destinationRow, int destinationColumn) {
		//if moving diagonally
		if (destinationRow != eyeball.getRow() && destinationColumn != eyeball.getColumn()) {
        	return Message.MOVING_DIAGONALLY;
        }
		//if the direction is ok
		else if (isDirectionOK(destinationRow,destinationColumn)) {
			return Message.OK;
		}
		//else there is a backwards move
		else {
			return Message.BACKWARDS_MOVE;
		}
	}

	@Override
	public boolean hasBlankFreePathTo(int destinationRow, int destinationColumn) {
		//get current position
		int currentRow = eyeball.getRow();
		int currentColumn = eyeball.getColumn();

		//get the min and max row and column between current and destination
		int minRow = Math.min(currentRow, destinationRow);
	    int maxRow = Math.max(currentRow, destinationRow);
	    int minColumn = Math.min(currentColumn, destinationColumn);
	    int maxColumn = Math.max(currentColumn, destinationColumn);

	    //check if there is a blank square between the current row and the destination
	    for (int row = minRow; row <= maxRow; row++) {
	    	for (int column = minColumn; column <= maxColumn; column++) {
	    		if (getColorAt(row, column) == Color.BLANK) {
	    			//if there is a blank on the path return false
	    			return false;
	    		}
	    	}
	    }
	    //if there is no blank return true
	    return true;
	}

	@Override
	public Message checkMessageForBlankOnPathTo(int destinationRow, int destinationColumn) {
		// the message for moving over a blank path or not
		if (hasBlankFreePathTo(destinationRow, destinationColumn)) {
			return Message.OK;
		}else {
			return Message.MOVING_OVER_BLANK;
		}
	}

	@Override
	public void moveTo(int destinationRow, int destinationColumn) {
	    //Set the direction of the eyeball based on current position and destination position
		//if the row is decreasing you are moving up
	    if (destinationRow < eyeball.getRow()) {
	        eyeball.setDirection(Direction.UP);
	    }
	    //if row is increasing direction is down
	    else if (destinationRow > eyeball.getRow()) {
	        eyeball.setDirection(Direction.DOWN);
	    }
	    //if column is decreasing your going left
	    else if (destinationColumn < eyeball.getColumn()) {
	        eyeball.setDirection(Direction.LEFT);
	    }
	    //if the column is increasing your going right
	    else if (destinationColumn > eyeball.getColumn()) {
	        eyeball.setDirection(Direction.RIGHT);
	    }
	    //set completed goals to BLANK
	    currentLevel.setGoalsToBlank();

	    //set the new position
	    eyeball.setRow(destinationRow);
	    eyeball.setColumn(destinationColumn);

	    //if you land on a goal that goal is completed
	    if (hasGoalAt(destinationRow, destinationColumn)) {
	        currentLevel.completeGoal(destinationRow, destinationColumn);
	    }
	}

	public Square getSquareAt(int row, int column) {
		return currentLevel.getSquareAt(row, column);
	}
}