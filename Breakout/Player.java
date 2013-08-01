package Breakout;

public class Player {
	
	//instantiate player's lives and scores
	private int lives;
	private int score;
	
	//constructor
	public Player() {
		lives = 3;
		score = 0;
	}
	//increase score
	public void increaseScore(int increase) {
		score += increase;
	}
	public void decreaseLives() {
		lives--;
	}
	//return the player's lives
	public int getLives() {
		return lives;
	}
	//return the player's score
	public int getScore() {
		return score;
	}
}
