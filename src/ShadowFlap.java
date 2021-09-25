import bagel.*;

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
    private Pipes PIPES;
    private final Messages MESSAGES;

    // Constants
    private final int LEVEL0_SCORE = 2;
    private final int LEVEL1_SCORE = 5;

    // Game variables
    private Integer score;
    private Integer level;
    private int frameCounter;
    private boolean gameOn;
    private boolean gameOver;
    private boolean gameWon;
    private boolean loadedObjects;

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
        loadedObjects = true;

        // Load objects
        BACKGROUND = new Background(level);
        BIRD = new Bird(level);
        PIPES = new Pipes(level, BIRD);
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

        // Start counting number of frames as game starts
        frameCounter += 1;

        // Start message
        if (!gameOn) {
            if (!loadedObjects)
                // Change objects pre-game during level transitions
                levelUp();
            else {
                MESSAGES.getCentreMessage(Messages.START_MESSAGE);
                if (level == 1)
                    MESSAGES.getShootMessage();
                if (input.wasPressed(Keys.SPACE)){
                    frameCounter = 0;
                    gameOn = true;
                }
            }
        } else {
            // Game has started
            if (!gameOver) {
                // Draw pipes
                PIPES.drawPipes();

                // Draw bird with its lives
                BIRD.drawBird(frameCounter);

                // Draw Score message
                MESSAGES.getCurrentScore(score);

                // Apply gravity acceleration
                BIRD.gravity();

                // Allow bird to jump
                BIRD.pressedSpace(input.wasPressed(Keys.SPACE));

                // Increase speed with L key
                PIPES.speedUp(input.wasPressed(Keys.L));

                // Decrease speed with K key
                PIPES.slowDown(input.wasPressed(Keys.K));

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
        // Collision with pipes/Out of bounds and no lives left
        if (PIPES.checkCollisionAndLives() || BIRD.checkOutOfBoundsAndLives())
            gameOver = true;
        // Has passed the pipes successfully
        else if (PIPES.checkPass()) {
            score = PIPES.getScore();
            if (level == 0) {
                if (score >= LEVEL0_SCORE) {
                    level = 1;
                    frameCounter = 0;
                    gameOn = false;
                    loadedObjects = false;
                }
            } else {
                if (score >= LEVEL1_SCORE) {
                    gameWon = true;
                    gameOver = true;
                }
            }
        }
    }

    // Method to transition from level 0 to level 1
    public void levelUp() {
        if (frameCounter < 20)
            MESSAGES.getCentreMessage(Messages.LEVEL_UP);
        else {
            score = 0;
            // Load objects
            BIRD = new Bird(level);
            PIPES = new Pipes(level, BIRD);
            BACKGROUND = new Background(level);
            loadedObjects = true;
        }
    }
}
