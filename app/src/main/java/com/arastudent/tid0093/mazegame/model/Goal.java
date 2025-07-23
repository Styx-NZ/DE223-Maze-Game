package com.arastudent.tid0093.mazegame.model;

public class Goal {
 private int row;
 private int column;
 private boolean completed;

 public Goal(int row, int column) {
     this.row = row;
     this.column = column;
     this.completed = false;
 }

 public int getRow() {
     return row;
 }

 public int getColumn() {
     return column;
 }

 //check if the goal is completed
 public boolean isCompleted() {
     return completed;
 }
 
 //set the goal as completed
 public void setCompleted(boolean completed) {
	 this.completed = true;
 }
}