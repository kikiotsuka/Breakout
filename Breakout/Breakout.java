/*
 * Breakout By Mitsuru Otsuka
 * V 1.0.3
 */
package Breakout;

import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Breakout extends Applet implements Runnable, MouseListener,
		MouseMotionListener, KeyListener {
	static final long serialVersionUID = 32L;

	// Version number for displaying
	String version = "V 1.0.3";

	// Declare stuff for double buffering
	Image image;
	Graphics graphics;

	// instantiate thread
	Thread th;

	// instantiate objects
	// holds player score/lives
	Player player;
	// stores information about the bricks to break
	ArrayList<Brick> bricks;
	// stores paddle location, where the paddle is, size of paddle
	Paddle paddle;
	// stores location of ball, speed of ball
	Ball ball;

	// instantiate game state variables
	boolean isMenu;
	boolean isGame;
	boolean gameOver;
	boolean isInstructions;
	boolean brickAnimation;
	ArrayList<Integer> animationTracker;

	// instantiate images used for the game
	Image crack0Img;// no cracks
	Image crack1Img;// some cracks
	Image crack2Img;// a lot of cracks

	// instantiate images used for menu
	Image startImg;
	Image instructionsImg;
	Image backImg;
	Image instructionsTextImg;

	// x, y, width, height
	int start[] = { 160, 235, 180, 65 };
	int instructions[] = { 132, start[1] + 100, 235, 65 };
	int back[] = { 350, 535, 110, 45 };
	int instructionsText[] = { 50, 30, 400, 385 };
	final int highlightDistance = 15;
	Rectangle2D highlight;
	Rectangle2D grey;
	final Color greyColor = new Color(128, 128, 128);

	// menu background color
	Rectangle2D screenOverLay;
	int R;
	int G;
	int B;
	boolean rCheck;
	boolean gCheck;
	boolean bCheck;
	int colorTracker;
	String direction;
	final int red[] = { 255, 0, 0 };
	final int orange[] = { 255, 165, 0 };
	final int yellow[] = { 255, 215, 0 };
	final int green[] = { 50, 205, 50 };
	final int blue[] = { 0, 191, 255 };
	final int indigo[] = { 75, 0, 130 };
	final int violet[] = { 238, 130, 238 };
	final int colors[][] = { red, orange, yellow, green, blue, indigo, violet };

	// declare shapes
	Rectangle2D paddleRect;
	ArrayList<Rectangle2D> brickRect;
	Ellipse2D ballEllipse;
	Rectangle2D paddleLeft;
	Rectangle2D paddleRight;

	// contains how many bricks were broken
	int broken;
	// contains if difficulty was accessed
	// true means accessed
	// false means not accessed
	boolean difficulty1;
	boolean difficulty2;
	boolean difficulty3;

	// declare FPS speed
	int FPS;

	// keep track of the keys pressed
	boolean left;
	boolean right;

	public void init() {
		setSize(500, 600);
		setBackground(Color.black);
		screenOverLay = new Rectangle(0, 0, this.getWidth(), this.getHeight());
		reset();
		// add mouse and key listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		loadImages();
		setFocusable(true);
	}

	public void loadImages() {
		// initate game images
		crack0Img = getImage(getCodeBase(), "crack0.gif");
		crack1Img = getImage(getCodeBase(), "crack1.gif");
		crack2Img = getImage(getCodeBase(), "crack2.gif");
		// initiate menu images
		startImg = getImage(getCodeBase(), "start.png");
		instructionsImg = getImage(getCodeBase(), "instructions.png");
		backImg = getImage(getCodeBase(), "back.png");
		instructionsTextImg = getImage(getCodeBase(), "instructionsText.png");
	}

	// reset the game
	public void reset() {
		R = 255;
		G = 255;
		B = 255;
		brickRect = new ArrayList<Rectangle2D>();
		direction = "RIGHT";
		player = new Player();
		ball = new Ball();
		paddle = new Paddle();
		resetBricks();
		isMenu = true;
		isGame = false;
		gameOver = false;
		isInstructions = false;
		brickAnimation = false;
		resetAnimation();
		FPS = 20;
		difficulty1 = false;
		difficulty2 = false;
		difficulty3 = false;
		broken = 0;
		left = false;
		right = false;
	}

	public void resetAnimation() {
		animationTracker = new ArrayList<Integer>();
		animationTracker.add(0);
		animationTracker.add(139);
	}

	public void resetBricks() {
		bricks = new ArrayList<Brick>();
		int colorCounter[] = { 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, };
		int colorTracker = 0;
		// instantiate bricks
		for (int y = 0; y < 280; y += 20) {
			for (int x = 0; x < 500; x += 50) {
				bricks.add(new Brick(x, y, colorCounter[colorTracker]));
			}
			colorTracker += 1;
		}
	}

	// starts game thread
	public void start() {
		th = new Thread(this);
		th.start();
	}

	// game loop
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		// game loop
		while (true) {
			if (isMenu) {
				colorBackground();
			}
			if (isInstructions) {
				colorBackground();
			}
			if (isGame) {
				paddle.move();
				if (paddle.getHasBall()) {
					ball.moveWithPaddle(paddle.getX());
				} else {
					ball.move();
					ballcheckCollision();// TODO
					// check if out of bounds
					if (ball.y > 600) {
						ball.resetBall(paddle.getCenterX());
						paddle.resetBall();
						player.decreaseLives();
					}
					if (player.getLives() <= 0) {
						isGame = false;
						gameOver = true;
					}
					if (!difficulty1) {
						if (broken == 15) {
							paddle.increaseDifficulty(1);
							ball.increaseDifficulty();
							difficulty1 = true;
						}
					}
					if (!difficulty2) {
						if (broken == 30) {
							paddle.increaseDifficulty(2);
							difficulty2 = true;
						}
					}
					if (!difficulty3) {
						if (broken == 45) {
							paddle.increaseDifficulty(3);
							difficulty3 = true;
						}
					}
				}
				repaint();
			}
			if (brickAnimation) {
				if (animationTracker.size() < bricks.size()) {
					animationTracker.add(animationTracker.get(animationTracker
							.size() - 2) + 1);
					animationTracker.add(animationTracker.get(animationTracker
							.size() - 2) - 1);
				} else {
					brickAnimation = false;
					isGame = true;
				}
				repaint();
			}
			try {
				Thread.sleep(FPS);
			} catch (Exception e) {
			}
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			graphics = image.getGraphics();
		}
		graphics.setColor(getBackground());
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		graphics.setColor(getForeground());
		paint(graphics);
		g.drawImage(image, 0, 0, this);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// set size of screen
		setSize(500, 600);
		if (!isGame && !gameOver && !brickAnimation) {
			// display background image
			g2.setColor(new Color(R, G, B));
			g2.fill(screenOverLay);
		}
		// display menu
		if (isMenu) {
			g2.setColor(greyColor);
			try {
				g2.fill(grey);
			} catch (Exception e) {

			}
			// display images
			g2.drawImage(startImg, start[0], start[1], this);
			g2.drawImage(instructionsImg, instructions[0], instructions[1],
					this);
			g2.setColor(Color.black);
			g2.drawString(version, 5, 595);
		}
		if (isInstructions) {
			g2.setColor(greyColor);
			try {
				g2.fill(grey);
			} catch (Exception e) {
			}
			g2.drawImage(instructionsTextImg, instructionsText[0],
					instructionsText[1], this);
			g2.drawImage(backImg, back[0], back[1], this);
		}
		if (isGame) {
			// display bricks
			brickRect.clear();
			for (Brick x : bricks) {
				brickRect.add(new Rectangle(x.getX(), x.getY(), x.getWidth(), x
						.getHeight()));
			}
			for (int i = 0; i < bricks.size(); i++) {
				if (bricks.get(i).getHitCounter() == 3) {
					broken++;
					bricks.remove(i);
					continue;
				}
				g2.setColor(bricks.get(i).getColor());
				g2.fill(brickRect.get(i));
				g2.setColor(Color.black);
				g2.draw(brickRect.get(i));
				if (bricks.get(i).getHitCounter() == 0) {
					g2.drawImage(crack0Img, bricks.get(i).getX(), bricks.get(i)
							.getY(), this);
				} else if (bricks.get(i).getHitCounter() == 1) {
					g2.drawImage(crack1Img, bricks.get(i).getX(), bricks.get(i)
							.getY(), this);
				} else if (bricks.get(i).getHitCounter() == 2) {
					g2.drawImage(crack2Img, bricks.get(i).getX(), bricks.get(i)
							.getY(), this);
				}
			}
			// display paddle
			paddleRect = new Rectangle(paddle.getX() - paddle.getCenterX(),
					paddle.getY(), paddle.getWidth(), paddle.getHeight());
			g2.setColor(Color.white);
			g2.fill(paddleRect);
			paddleLeft = new Rectangle(paddle.getX() - paddle.getCenterX(),
					paddle.getY(), paddle.getSpecial(), paddle.getHeight());
			paddleRight = new Rectangle(paddle.getX() + paddle.getCenterX()
					- paddle.getSpecial(), paddle.getY(), paddle.getSpecial(),
					paddle.getHeight());
			g2.setColor(Color.yellow);
			g2.fill(paddleLeft);
			g2.fill(paddleRight);
			// display ball
			int tempRadius = ball.radius;
			ballEllipse = new Ellipse2D.Double(ball.x - tempRadius, ball.y
					- tempRadius, tempRadius * 2, tempRadius * 2);
			g2.setColor(Color.white);
			g2.fill(ballEllipse);
			// display player lives/score
			g2.setFont(new Font("SANS SERIF", Font.PLAIN, 12));
			g2.drawString("Player Lives: " + player.getLives(), 5, 580);
			g2.drawString("Player Score: " + player.getScore(), 5, 595);
		}
		if (brickAnimation) {
			for (int x : animationTracker) {
				Rectangle2D rect = new Rectangle(bricks.get(x).getX(), bricks
						.get(x).getY(), bricks.get(x).getWidth(), bricks.get(x)
						.getHeight());
				g2.setColor(bricks.get(x).getColor());
				g2.fill(rect);
				g2.setColor(Color.black);
				g2.draw(rect);
				g2.drawImage(crack0Img, bricks.get(x).getX(), bricks.get(x)
						.getY(), this);
			}
		}
		if (gameOver) {
			g2.setColor(greyColor);
			try {
				g2.fill(grey);
			} catch (Exception e) {

			}
			g2.setColor(Color.white);
			g2.setFont(new Font("SANS SERIF", Font.BOLD, 30));
			if (player.getScore() == 5600) {
				g2.drawString("YOU WON!!!", 175, 250);
			} else {
				g2.drawString("GAME OVER", 175, 250);
			}
			g2.drawString("Your Score: ", 150, 280);
			g2.drawString("" + player.getScore(), 340, 280);
			g2.drawImage(backImg, back[0], back[1], this);

		}
		if (isMenu || isInstructions) {
			g2.setColor(Color.black);
		} else {
			g2.setColor(Color.white);
		}
		g2.setFont(new Font("SANS SERIF", Font.PLAIN, 12));
		g2.drawString("Game Art : Stephanie Chen", 349, 580);
		g2.drawString("Game Programming : Mitsuru Otsuka", 295, 595);
	}

	public void colorBackground() {
		if (rCheck && gCheck && bCheck) {
			if (direction.equals("RIGHT")) {
				colorTracker++;
				if (colorTracker == colors.length) {
					direction = "LEFT";
					// compensate for the fact that it is out of bounds
					colorTracker--;
				}
			} else if (direction.equals("LEFT")) {
				colorTracker--;
				if (colorTracker < 0) {
					direction = "RIGHT";
					// compensate for the fact that it is out of bounds
					colorTracker++;
				}
			}
			rCheck = false;
			gCheck = false;
			bCheck = false;
		}
		if (!rCheck) {
			if (R < colors[colorTracker][0]) {
				R++;
			} else if (R > colors[colorTracker][0]) {
				R--;
			} else {
				rCheck = true;
			}
		}
		if (!gCheck) {
			if (G < colors[colorTracker][1]) {
				G++;
			} else if (G > colors[colorTracker][1]) {
				G--;
			} else {
				gCheck = true;
			}
		}
		if (!bCheck) {
			if (B < colors[colorTracker][2]) {
				B++;
			} else if (B > colors[colorTracker][2]) {
				B--;
			} else {
				bCheck = true;
			}
		}
		repaint();
	}

	public void ballcheckCollision() {
		int top = ball.y - ball.radius;
		int left = ball.x - ball.radius;
		int right = ball.x + ball.radius;
		// check contact against left/right wall
		if (ball.xVector < 0 && left < 0 || ball.xVector > 0 && right > 500) {
			ball.y -= ball.yVector;
			if (ball.x - ball.radius < 0) {
				ball.x = ball.radius;
			} else if (ball.x + ball.radius > 500) {
				ball.x = 500 - ball.radius;
			} else {
				ball.x -= ball.xVector;
			}
		}
		// check if contact against ceiling
		if (ball.yVector < 0 && top < 0) {
			ball.x -= ball.xVector;
			ball.y = ball.radius;
		}
		// check contact against bricks
		// TODO
		for (Brick x : bricks) {

		}
	}

	public void keyPressed(KeyEvent e) {
		if (isGame) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				paddle.changeDirectionRight();
				right = true;
				left = false;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				paddle.changeDirectionLeft();
				left = true;
				right = false;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		}
		if (!left && !right) {
			paddle.changeDirectionNull();
		}
	}

	public void keyTyped(KeyEvent e) {
		if (isGame) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				isGame = false;
				gameOver = true;
				repaint();
			}
			if (e.getKeyChar() == KeyEvent.VK_SPACE) {
				if (paddle.getHasBall()) {
					ball.launch();
					paddle.launchedBall();
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (isMenu) {
			// 0 - x
			// 1 - y
			// 2 - width
			// 3 - height

			// start button
			if ((x > start[0] && x < start[0] + start[2])
					&& (y > start[1] && y < start[1] + start[3])) {
				isMenu = false;
				brickAnimation = true;
				grey = null;
			}
			// instructions button
			else if ((x > instructions[0] && x < instructions[0]
					+ instructions[2])
					&& (y > instructions[1] && y < instructions[1]
							+ instructions[3])) {
				isMenu = false;
				isInstructions = true;
				grey = null;
			}
		}
		if (isInstructions) {
			if ((x > back[0] && x < back[0] + back[2])
					&& (y > back[1] && y < back[1] + back[3])) {
				isInstructions = false;
				isMenu = true;
				grey = null;
			}
			repaint();
		}
		if (gameOver) {
			if ((x > back[0] && x < back[0] + back[2])
					&& (y > back[1] && y < back[1] + back[3])) {
				gameOver = false;
				isMenu = true;
				grey = null;
				reset();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (isMenu) {
			// 0 - x
			// 1 - y
			// 2 - width
			// 3 - height

			// start button
			if ((x > start[0] && x < start[0] + start[2])
					&& (y > start[1] && y < start[1] + start[3])) {
				grey = new Rectangle(start[0] - highlightDistance, start[1]
						- highlightDistance, start[2] + highlightDistance * 2,
						start[3] + highlightDistance * 2);
			}
			// instructions button
			else if ((x > instructions[0] && x < instructions[0]
					+ instructions[2])
					&& (y > instructions[1] && y < instructions[1]
							+ instructions[3])) {
				grey = new Rectangle(instructions[0] - highlightDistance,
						instructions[1] - highlightDistance, instructions[2]
								+ highlightDistance * 2, instructions[3]
								+ highlightDistance * 2);
			} else {
				grey = null;
			}
			repaint();
		}
		if (isInstructions) {
			if ((x > back[0] && x < back[0] + back[2])
					&& (y > back[1] && y < back[1] + back[3])) {
				grey = new Rectangle(back[0] - highlightDistance, back[1]
						- highlightDistance, back[2] + highlightDistance * 2,
						back[3] + highlightDistance * 2);
			} else {
				grey = null;
			}
			repaint();
		}
		if (gameOver) {
			if ((x > back[0] && x < back[0] + back[2])
					&& (y > back[1] && y < back[1] + back[3])) {
				grey = new Rectangle(back[0] - highlightDistance, back[1]
						- highlightDistance, back[2] + highlightDistance * 2,
						back[3] + highlightDistance * 2);
			} else {
				grey = null;
			}
			repaint();
		}
	}

}
