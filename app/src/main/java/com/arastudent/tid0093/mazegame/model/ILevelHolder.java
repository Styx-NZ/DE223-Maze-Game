package com.arastudent.tid0093.mazegame.model;

public interface ILevelHolder {
	public void addLevel(int height, int width);
	public int getLevelWidth();
	public int getLevelHeight();
	public void setLevel(int levelNumber);
	public int getLevelCount();
}
