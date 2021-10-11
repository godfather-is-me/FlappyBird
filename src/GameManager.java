import java.util.*;
import bagel.Window;

/**
 * Manages the game by controlling pipe and weapon arrays, changing levels,
 * and performing constant checks
 */

public class GameManager {
    // Static constants
    /**
     * The minimum timescale possible
     */
    private static final int MIN_TIMESCALE = 1;

    /**
     * The maximum timescale possible
     */
    private static final int MAX_TIMESCALE = 5;

    /**
     * The y-axis lower bound for randomized y values
     */
    public static final int Y_LOWER_BOUND = 100;

    /**
     * The y-axis upper bound for randomized y values
     */
    public static final int Y_UPPER_BOUND = 500;

    /**
     * An array of speed for each timescale, create w.r.t initial speed
     */
    public static final double[] SPEED = new double[MAX_TIMESCALE];
    /**
     * An array of time frames spawned for each timescale, created w.r.t. initial time frame
     */
    public static final int[] PIPE_SPAWN_TIME = new int[MAX_TIMESCALE];

    /**
     * The timescale of the game used by all classes
     */
    public static int timeScale;
    /**
     * The move speed for the current timescale
     */
    public static double moveSpeed;

    // Store weapons and game pipes in window as a queue
    private final Bird BIRD;
    private final Queue<Weapon> WEAPONS;
    private final Queue<PipeSet> GAME_PIPES;

    // Game variables
    private int score;
    private int frameCounter;
    private final int LEVEL;
    private PipeSet lastPipe;

    // Constants
    private final int INITIAL_SPAWN_RATE = 100;
    private final double INITIAL_SPEED = 3.0;
    private final double SPEED_FACTOR = 1.5;
    private final double ADJUSTMENT = 0.9;
    private final int[] LEVEL0_GAPS = {100, 300, 500};

    /**
     * Create a manager with the specified bird and level
     *
     * @param level Current level being played
     * @param bird Bird in play
     */
    public GameManager(int level, Bird bird) {
        // Load objects
        this.BIRD = bird;
        GAME_PIPES = new LinkedList<>();
        WEAPONS = new LinkedList<>();

        calculateTimeScales();

        // Game variables
        score = 0;
        timeScale = 0;
        lastPipe = null;
        this.LEVEL = level;
        moveSpeed = SPEED[timeScale];
    }

    /**
     * Draw all objects on screen
     */
    public void drawObjects() {
        frameCounter += 1;

        // Add weapons and pipes according to specs
        addObjects();

        // Remove pipes and weapons that are out of window
        checkPipeBounds();
        checkWeaponBounds();

        // Draw all pipes in queue
        for(PipeSet pipeSet : GAME_PIPES)
            pipeSet.drawObjects();

        // Draw all weapons in queue
        for (Weapon weapon: WEAPONS)
            weapon.drawObject();

        // Draw bird
        BIRD.drawBird(frameCounter);
    }

    /**
     * Spawn new objects and add them to the queue
     */
    public void addObjects() {
        // Add pipes to the queue every 100 frames
        if (GAME_PIPES.isEmpty())
            lastPipe = addPipeSet();
        else if ((frameCounter % PIPE_SPAWN_TIME[timeScale]) == 0)
            if (checkDistance())
                lastPipe = addPipeSet();
    }

    /**
     * Add new pipe sets to the queue
     *
     * @return the latest pipe added to the queue
     */
    public PipeSet addPipeSet() {
        Random rand = new Random();
        PipeSet tempPipe;
        // Choose only plastic pipes
        if (LEVEL == 0) {
            tempPipe = new PipeSet(LEVEL, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)]);
            GAME_PIPES.add(tempPipe);
        }
        else {
            int randPipe = rand.nextInt(2);
            int randY = rand.nextInt(Y_UPPER_BOUND - Y_LOWER_BOUND) + Y_LOWER_BOUND;
            tempPipe = new PipeSet(randPipe, randY);
            // Add weapon and pipe set
            GAME_PIPES.add(tempPipe);
            addWeapon(tempPipe);
        }

        return tempPipe;
    }

    /**
     * Add new weapon into the queue
     *
     * @param pipeSet the pipe set after which the weapon is initialized
     */
    public void addWeapon(PipeSet pipeSet) {
        Random rand = new Random();
        if (rand.nextInt(2) == 0)
            WEAPONS.add(new Rock(pipeSet));
        else
            WEAPONS.add(new Bomb(pipeSet));
    }

    /**
     * Move all objects from right to left on screen
     */
    public void leftShift() {
        for (PipeSet pipeSet: GAME_PIPES)
            pipeSet.leftShift();

        for (Weapon weapon: WEAPONS)
            weapon.leftShift();
    }

    /**
     * Check collision between bird and the pipe set that has not been passed
     *
     * @return Returns true if collision occurs and no lives left
     */
    public boolean checkCollisionAndLives() {
        for (PipeSet pipeSet: GAME_PIPES) {
            if (pipeSet.getHasPassed())
                continue;

            // At the next pipe which has not been passed
            if (pipeSet.birdWeaponCollision(BIRD)) {
                BIRD.lifeLost();
                if (BIRD.hasLives())
                    GAME_PIPES.remove(pipeSet);
                else
                    // Bird has collided and no lives left
                    return  true;
            }

            // Check if the weapon has collided with pipe when shot
            if (BIRD.getHasShotWeapon())
                if (BIRD.getWeapon().checkDestruction(pipeSet)) {
                    score += 1;
                    GAME_PIPES.remove(pipeSet);
                    BIRD.removeWeapon();
                }

            // The next pipe to be checked has not reached bird
            break;
        }
        return false;
    }

    /**
     * Check if bird has passed the pipe and update score
     *
     * @return Score of the bird
     */
    public int checkPass() {
        for (PipeSet pipeSet: GAME_PIPES) {
            if (pipeSet.getHasPassed())
                continue;
            if (pipeSet.checkBirdPass(BIRD))
                score += 1;
            break;
        }

        for (Weapon weapon: WEAPONS) {
            if (weapon.getHasPassed())
                continue;
            weapon.checkBirdPass(BIRD);
            break;
        }

        // Returns overall score, including pipe destruction
        return score;
    }

    /**
     * Pop the pipe that left the window (out-of-bounds)
     */
    public void checkPipeBounds() {
        if (!GAME_PIPES.isEmpty())
            if (GAME_PIPES.peek().checkWindowBounds())
                GAME_PIPES.remove();
    }

    /**
     * Pop the weapon that left the window (out-of-bounds)
     */
    public void checkWeaponBounds() {
        if (!WEAPONS.isEmpty())
            if (WEAPONS.peek().checkWindowBounds())
                WEAPONS.remove();
    }

    /**
     * A correction method to maintain distance between pipes with speed change
     *
     * @return Returns true if there is enough distance between pipes
     */
    public boolean checkDistance() {
        double distanceFromRight = Window.getWidth() - lastPipe.getX();
        double distanceBetweenPipes = SPEED[timeScale] * PIPE_SPAWN_TIME[timeScale];
        return (distanceFromRight >= (distanceBetweenPipes * ADJUSTMENT));
    }

    /**
     * Check if the weapon has been picked, changes properties of bird and weapon
     */
    public void pickWeapon() {
        if (!BIRD.getHasPickedWeapon())
            for (Weapon weapon : WEAPONS) {
                if (weapon.getHasPassed())
                    continue;

                if (weapon.checkPickUp(BIRD)) {
                    weapon.setIsPicked(true);
                    BIRD.setWeapon(weapon);
                    WEAPONS.remove(weapon);
                    break;
                }
            }
    }

    /**
     * Shoot weapon if key is pressed
     *
     * @param keyPress Key input by user
     */
    public void shootWeapon(boolean keyPress) {
        if (keyPress)
            if (BIRD.getHasPickedWeapon())
                BIRD.setHasShotWeapon();
    }

    /**
     * Initialize speed/spawn rate for all timescales
     */
    public void calculateTimeScales() {
        SPEED[0] = INITIAL_SPEED;
        PIPE_SPAWN_TIME[0] = INITIAL_SPAWN_RATE;

        for (int i = 1; i < MAX_TIMESCALE; ++i) {
            SPEED[i] = SPEED[i-1] * SPEED_FACTOR;
            PIPE_SPAWN_TIME[i] = (int) (PIPE_SPAWN_TIME[i-1] / SPEED_FACTOR);
        }
    }

    /**
     * Increase the speed if key is pressed
     *
     * @param isPressed Key input by user
     */
    public void speedUp(boolean isPressed) {
        if (isPressed)
            if (timeScale < (MAX_TIMESCALE - 1)) {
                timeScale += 1;
                setSpeed();
            }
    }

    /**
     * Decrease the speed if key is pressed
     *
     * @param isPressed Key input by user
     */
    public void slowDown(boolean isPressed) {
        if (isPressed)
            // Timescale set to be from 0-4 in code
            if (timeScale > (MIN_TIMESCALE - 1)) {
                timeScale -= 1;
                setSpeed();
            }
    }

    // Method to set speed for given timescale change
    private void setSpeed() {
        moveSpeed = SPEED[timeScale];
    }
}
