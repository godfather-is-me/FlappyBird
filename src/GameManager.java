import java.util.*;
import bagel.Window;

/**
 * Manages the game by controlling pipe and weapon arrays, changing levels,
 * and performing constant checks
 */

public class GameManager {
    // Constants
    public static final int MAX_TIMESCALE = 5;
    public static final int MIN_TIMESCALE = 1;
    public static final int FLAME_SPAWN_LENGTH = 20;
    public static final int Y_LOWER_BOUND = 100;
    public static final int Y_UPPER_BOUND = 500;

    public static final int[] PIPE_SPAWN_TIME = new int[MAX_TIMESCALE];
    public static final double[] SPEED = new double[MAX_TIMESCALE];

    // Non-final static
    public static int timeScale;

    // Store all pipes from current window in a Queue
    private final Queue<PipeSet> GAME_PIPES;
    private final Queue<AbstractWeapon> WEAPONS;
    private final Bird BIRD;

    // Game variables
    private int score;
    private int frameCounter;
    private int flameDuration;
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

        // Game variables
        score = 0;
        timeScale = 0;
        flameDuration = 0;
        lastPipe = null;
        this.LEVEL = level;
        calculateTimeScales();
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
        for(PipeSet pipeSet : GAME_PIPES) {
            pipeSet.drawPipes();

            // Draw flames
            if (pipeSet.getLevel() == 1) {
                if ((frameCounter % FLAME_SPAWN_LENGTH) == 0)
                    flameDuration = frameCounter + 3;
                if (frameCounter < flameDuration) {
                    pipeSet.drawFlames();
                    pipeSet.setHasDrawnFlames(true);
                } else
                    pipeSet.setHasDrawnFlames(false);
            }
        }

        // Draw all weapons in queue
        for (AbstractWeapon weapon: WEAPONS)
            weapon.drawWeapon();

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
            tempPipe = new PipeSet(LEVEL, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)], SPEED[timeScale]);
            GAME_PIPES.add(tempPipe);
        }
        else {
            int randPipe = rand.nextInt(2);
            int randY = rand.nextInt(Y_UPPER_BOUND - Y_LOWER_BOUND) + Y_LOWER_BOUND;
            tempPipe = new PipeSet(randPipe, randY, SPEED[timeScale]);
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

        for (AbstractWeapon weapon: WEAPONS)
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

        for (AbstractWeapon weapon: WEAPONS) {
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
        double distanceFromRight = Window.getWidth() - lastPipe.getPosition().x;
        double distanceBetweenPipes = SPEED[timeScale] * PIPE_SPAWN_TIME[timeScale];
        return (distanceFromRight >= (distanceBetweenPipes * ADJUSTMENT));
    }

    /**
     * Check if the weapon has been picked, changes properties of bird and weapon
     */
    public void pickWeapon() {
        if (!BIRD.getHasPickedWeapon())
            for (AbstractWeapon weapon : WEAPONS) {
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

    // Method to set speed for any configuration
    private void setSpeed() {
        for (PipeSet pipeSet: GAME_PIPES)
            pipeSet.setMoveSpeed(SPEED[timeScale]);
        for (AbstractWeapon weapon: WEAPONS)
            weapon.setMoveSpeed(SPEED[timeScale]);
    }

}
