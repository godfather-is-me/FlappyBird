import bagel.*;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 *
 * @author Prathyush Prashanth Rao
 * Student number: 1102225
 */

public class ShadowFlap extends AbstractGame {
    // Constants
    private final int LEVEL0_SCORE = 10;
    private final int LEVEL1_SCORE = 30;

    // Game objects
    private final Messages MESSAGES;
    private Bird BIRD;
    private GameManager MANAGER;
    private Background BACKGROUND;

    // Game variables
    private Integer score;
    private Integer level;
    private int frameCounter;
    private boolean gameOn;
    private boolean gameOver;
    private boolean gameWon;
    private boolean loadedObjects;

    /**
     * Constructor of the game engine used to create all the base objects of flappy bird
     */
    public ShadowFlap() {
        super(1024, 768, "Flappy Bird");

        // Game variables
        score = 0;
        level = 0;
        frameCounter = 0;
        gameOn = false;
        gameWon = false;
        gameOver = false;
        loadedObjects = true;

        BIRD = new Bird(level);
        MESSAGES = new Messages();
        BACKGROUND = new Background(level);
        MANAGER = new GameManager(level, BIRD);
    }

    /**
     * The entry point for the program.
     * @param args Command line arguments
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
                if (input.wasPressed(Keys.SPACE)) {
                    frameCounter = 0;
                    gameOn = true;
                }
            }
        } else {
            // Game has started
            if (!gameOver) {
                // Draw pipes
                MANAGER.drawObjects();

                // Draw Score message
                MESSAGES.getCurrentScore(score);

                // Allow bird to jump
                BIRD.pressedSpace(input.wasPressed(Keys.SPACE));

                // Increase speed with L key
                MANAGER.speedUp(input.wasPressed(Keys.L));

                // Decrease speed with K key
                MANAGER.slowDown(input.wasPressed(Keys.K));

                // Check if any weapon has been picked up
                MANAGER.pickWeapon();

                // Check if 'S' has been pressed to shoot
                MANAGER.shootWeapon(input.wasPressed(Keys.S));

                // Check collision or game won
                checkGameOver();

                // Move objects to the left
                MANAGER.leftShift();
            } else {
                // Game over
                if (gameWon)
                    MESSAGES.getCentreMessage(Messages.WIN_MESSAGE);
                else {
                    MESSAGES.getCentreMessage(Messages.GAME_OVER);
                    MESSAGES.getFinalScore(score);
                }
            }
        }

        // Escape key to exit game
        if (input.wasPressed(Keys.ESCAPE))
            Window.close();
    }

    /**
     * Check if game is over and the outcome
     */
    public void checkGameOver() {
        // Collision with pipes/Out of bounds and no lives left
        if (MANAGER.checkCollisionAndLives() || BIRD.checkOutOfBoundsAndLives())
            gameOver = true;

        // Has passed the pipes successfully
        score = MANAGER.checkPass();
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

    /**
     * Transition from level 0 to level 1 (reinitialize objects)
     */
    public void levelUp() {
        if (frameCounter < 20)
            MESSAGES.getCentreMessage(Messages.LEVEL_UP);
        else {
            score = 0;
            // Load objects
            BIRD = new Bird(level);
            MANAGER = new GameManager(level, BIRD);
            BACKGROUND = new Background(level);
            loadedObjects = true;
        }
    }
}
