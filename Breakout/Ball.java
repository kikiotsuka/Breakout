package Breakout;

/*
 * TODO
 * implement speed of ball
 * implement powerups
 */

public class Ball {
	//instantiate variables
	//contains x location of ball
	int x;
	//contains y location of ball
	int y;
	//contains x vector of ball
	int xVector;
	//contains y vector of ball
	int yVector;
	//contains min constant x
	private int minX;
	//contains min constant y
	private int minY;
	//contains radius of ball	
	int radius;

	public Ball() {
		radius = 10;
		minX = 5;
		minY = 5;
		x = 250;
		y = 550 - radius;
	}
	public void moveWithPaddle(int moveX, int moveY) {
		x = moveX;
		y = moveY;
	}
	public void move() {
		x += xVector;
		y += yVector;
	}
	public int randLeft() {
		return -((int) (minX + Math.random() * (int) (Math.random() * minX)));
	}
	public int randRight() {
		return (int) (minX + Math.random() * (int) (Math.random() * minX));
	}
	public void moveWithPaddle(int paddleX) {
		x = paddleX;
	}
	//launches ball off of player's paddle once player hits space
	public void launch() {
		xVector = (int) (minX + Math.random() * (int) (Math.random() * minX));
		int random = (int) (Math.random() * 2);
		if (random == 0) {
			xVector *= -1;
		}
		yVector = (int) -(minY + Math.random() * (int) (Math.random() * minY));
	}
	//returns true if the balls are out of bounds
	public void resetBall(int newLocation) {
		x = newLocation;
		y = 550 - radius;
	}
	public void increaseDifficulty() {
		minX = 6;
		minY = 6;
	}
	public int getMinX() {
		return minX;
	}
	public int getMinY() {
		return minY;
	}
}
