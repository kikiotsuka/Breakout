package Breakout;
/*
 * TODO
 * add cases for difficulties
 * make it catch pwoerups that fall
 */


public class Paddle {
	
	//instantiate the variables
	//contains x coordinate of paddle
	private int x;
	//contains y coordinate of paddle
	//screen will be 500x600
	private final int y = 550;
	//contains if the paddle is currently holding the ball
	private boolean hasBall;
	//contains height of paddle
	private final int height = 15;
	//contains width of paddle
	private int width;
	//contians center x of paddle
	private int centerX;
	//contains direction of paddle
	private String direction;
	//contains speed of paddle
	final private int speed = 7;
	//special hit areas
	private int special;
	
	public Paddle() {
		x = 250; //center of screen
		hasBall = true;
		width = 100;
		special = 20;
		centerX = width / 2;
		direction = "";
	}
	public void move() {
		if (direction.equals("RIGHT")) {
			x += speed;
			if (x + centerX > 500) {
				x = 500 - centerX;
			}
		}
		else if (direction.equals("LEFT")) {
			x -= speed;
			if (x - centerX < 0) {
				x = centerX;
			}
		}
	}
	public void increaseDifficulty(int difficulty) {
		switch (difficulty) {
		case(1):
			width = 60;
			special = 15;
			centerX = width / 2;
			break;
		case(2):
			width = 50;
			special = 10;
			centerX = width / 2;
			break;
		case(3):
			width = 15;
			special = 5;
			centerX = width / 2;
			break;
		}
	}
	public void changeDirectionRight() {
		direction = "RIGHT";
	}
	public void changeDirectionLeft() {
		direction = "LEFT";
	}
	public void changeDirectionNull() {
		direction = "";
	}
	public void resetBall() {
		hasBall = true;
	}
	public void launchedBall() {
		hasBall = false;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public int getCenterX() {
		return centerX;
	}
	public int getSpecial() {
		return special;
	}
	public boolean getHasBall() {
		return hasBall;
	}
}
