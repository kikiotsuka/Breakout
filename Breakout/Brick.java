package Breakout;

import java.awt.Color;

public class Brick {
	
	//instantiate variabels
	//contains brick's x coordinate
	private int x;
	//contains brick's y coordinate
	private int y;
	//contains the hitcounter of bricks
	//0 - no cracks
	//1 - some cracks
	//2 a bunch of cracks
	//3 break
	private int hitCounter;
	
	//contains teh color of the brick
	//Colors wil be the colors of the rianbow
	//starting from the bottom to top,
	//violet, indigo, blue, green, yellow, orange, red
	//these will correspond to their numbers respectively
	//The reason why integers are used to represetn the colors rather than
	//Strings is because it is more faster and efficient to use a switch statement
	//switch statements do not allow strings, btu only numbers, and integers
	//hold less bytes anyways. Although there really is no need to be worrying about how much
	//memory this game will take because its small size will make it not matter
	//violet, indigo, blue, green, yellow, orange, red
	//0, 1, 2, 3, 4, 5, 6
	private int color;
	//width of brick
	final private int width = 50;
	//height of brick
	final private int height = 20;
	//constructor
	public Brick(int initX, int initY, int initColor) {
		x = initX;
		y = initY;
		hitCounter = 0;
		color = initColor;
	}
	public void hit() {
		hitCounter++;
	}
	//returns x coordinate of brick
	public int getX() {
		return x;
	}
	//returns y coordinate of brick
	public int getY() {
		return y;
	}
	//returns number of times the brick was hit
	public int getHitCounter() {
		return hitCounter;
	}
	//returns teh color RGB value of the brick
	//violet - (238, 130, 238)
	//indigo - (75, 0, 130)
	//blue - (0, 191, 255)
	//green - (50, 205, 50)
	//yellow - (255, 215, 0)
	//orange - (255, 165, 0)
	//red - (255, 0, 0)
	//violet, indigo, blue, green, yellow, orange, red
	//0     , 1     , 2   , 3    , 4     , 5     , 6
	public Color getColor() {
		switch(color) {
		case 0:
			return new Color(238, 130, 238);
		case 1:
			return new Color(75, 0, 130);
		case 2:
			return new Color(0, 191, 255);
		case 3:
			return new Color(50, 205, 50);
		case 4:
			return new Color(255, 215, 0);
		case 5:
			return new Color(255, 165, 0);
		case 6:
			return new Color(255, 0, 0);
		}
		return new Color(0, 0, 0);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
