import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;


/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Prathyush Prashanth Rao
 * Student number: 1102225
 *
 */

public class ShadowFlap extends AbstractGame {
    // Game objects
    private Background BACKGROUND;
    private Bird BIRD;
    private final Pipes PIPES;
    private final Messages MESSAGES;

    // Game variables
    private Integer score;
    private Integer level;
    private int frameCounter;
    private boolean gameOn;
    private boolean gameOver;
    private boolean gameWon;

    // Constructor
    public ShadowFlap() {
        super(1024, 768, "Flappy Bird");

        // Game variables
        score = 0;
        level = 0;
        frameCounter = 0;
        gameOn = false;
        gameOver = false;
        gameWon = false;

        // Load objects
        BACKGROUND = new Background(level);
        BIRD = new Bird(level);
        PIPES = new Pipes(level);
        MESSAGES = new Messages();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        // Background always displayed
        BACKGROUND.displayBackground();
        // Check pipe
        // Drawing.drawLine(new Point(0,500), new Point(1024, 500), 2, Colour.BLACK);

        // Start message
        if (!gameOn) {
            MESSAGES.getCentreMessage(Messages.START_MESSAGE);
            if (input.wasPressed(Keys.SPACE))
                gameOn = true;
        } else {
            // Game has started
            if (!gameOver) {
                // Start counting number of frames as game starts
                frameCounter += 1;

                // Draw pipes
                PIPES.drawPipes();

                // Draw bird
                BIRD.drawBird(frameCounter);

                // Draw Score message
                MESSAGES.getCurrentScore(score);

                // Apply gravity acceleration
                BIRD.gravity();

                // Allow bird to jump
                BIRD.pressedSpace(input.isDown(Keys.SPACE));

                // Check collision or game won
                checkGameOver();

                // Move pipes to the left
                PIPES.leftShift();
            } else {
                // Game over
                if (gameWon)
                    MESSAGES.getCentreMessage(Messages.WIN_MESSAGE);
                else
                    MESSAGES.getCentreMessage(Messages.GAME_OVER);
                // Score message
                MESSAGES.getFinalScore(score);
            }
        }

        // Escape key to exit game
        if (input.wasPressed(Keys.ESCAPE))
            Window.close();
    }

    // Method to check if the game won or lost
    public void checkGameOver() {
        // Collision with pipes
        if (PIPES.checkCollision(BIRD))
            gameOver = true;
        else if (BIRD.checkOutOfBounds())
            // Out of bounds
            gameOver = true;
        else if (PIPES.checkPass(BIRD)) {
            // Has passed the pipes successfully
            score = PIPES.getScore();
            if (score >= 10) {
                gameWon = true;
                gameOver = true;
            }
        }
    }
}
