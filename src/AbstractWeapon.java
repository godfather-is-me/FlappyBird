import bagel.*;
import bagel.util.*;
import java.util.Random;

/**
 * Contains all standard methods used by the weapon.
 */

public abstract class AbstractWeapon implements Spawnable{
    // Variables
    private Point position;
    private int frameCounter;
    private double moveSpeed;
    private boolean isShot;
    private boolean isPicked;
    private boolean hasPassed;

    // Constants
    private final int RANGE;
    private final Image WEAPON;
    private final int SHOOT_SPEED = 5;
    private final double ADJUSTMENT = 0.6;

    // Enum
    protected enum WEAPON_TYPE {BOMB, ROCK}
    private final WEAPON_TYPE type;

    /**
     * The AbstractWeapon constructor used as a super constructor for
     * bombs and rocks to create a weapon object.
     *
     * @param range The range of the weapon when shot
     * @param weapon An Image object containing the weapon in context
     * @param type The specific type of weapon required, available from WEAPON_TYPE enum
     * @param pipeSet The pipe set after which this particular weapon has been spawned
     */
    public AbstractWeapon (int range, Image weapon, WEAPON_TYPE type, PipeSet pipeSet) {
        this.WEAPON = weapon;
        this.RANGE = range;
        this.type = type;

        moveSpeed = pipeSet.getMoveSpeed();
        frameCounter = 0;

        isShot = false;
        isPicked = false;
        hasPassed = false;
        position = setInitialPosition(pipeSet);
    }

    /**
     * Sets the initial position of the weapon at a random location between
     * pipe sets
     *
     * @param pipeSet The pipe set after which the weapon is initialised
     * @return The initialized random point for the weapon
     */
    public Point setInitialPosition(PipeSet pipeSet) {
        Random rand = new Random();

        // Constants from game manager
        int pipeSpawnTime = GameManager.PIPE_SPAWN_TIME[GameManager.timeScale];
        int upperBound = GameManager.Y_UPPER_BOUND;
        int lowerBound = GameManager.Y_LOWER_BOUND;
        double speed = GameManager.SPEED[GameManager.timeScale];

        // Distance to next pipe
        double distance = (pipeSpawnTime * speed) - (pipeSet.getWidth() + getWidth());
        int x = rand.nextInt((int) distance) + Window.getWidth() + (int) ((pipeSet.getWidth() + getWidth()) / 2);
        int y = rand.nextInt(upperBound - lowerBound) + lowerBound;

        return new Point(x, y);
    }

    /**
     * Checks whether the weapon has been picked up by bird
     *
     * @param bird The bird in play
     * @return Returns true if the bird has picked up the weapon
     */
    public boolean checkPickUp(Bird bird) {
        if (!isPicked)
            return getBox().intersects(bird.getBox());
        return false;
    }

    /**
     * Check if the weapon has passed the bird on screen
     *
     * @param bird The bird in play
     * @return Returns true if weapon has passed the bird
     */
    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > WEAPON.getBoundingBoxAt(position).right())
            hasPassed = true;
        return hasPassed;
    }

    /**
     * Check if weapon has left the window
     *
     * @return Returns true if weapon is outside window
     */
    public boolean checkWindowBounds() {
        if (hasPassed)
            return getBox().right() < 0;
        return false;
    }

    /**
     * Check if the fired weapon is out of range (in frames)
     *
     * @return Returns true if out-of-range
     */
    public boolean checkOutOfRange() {
        return frameCounter > RANGE;
    }

    /**
     * Check if weapon has destroyed pipe when shot
     *
     * @param pipeSet The pipe set the weapon has shot at
     * @return Returns true if pipe and weapon intersect
     */
    public boolean checkDestruction(PipeSet pipeSet) {
        if ((pipeSet.getLevel() == 0) || (pipeSet.getLevel() == 1 && type == WEAPON_TYPE.BOMB)){
            if (pipeSet.getTopRectangle().intersects(getBox()))
                return true;
            return pipeSet.getBotRectangle().intersects(getBox());
        }
        return false;
    }

    /**
     * Updates the position of the weapon when picked up bird or shot
     *
     * @param birdPosition The current position of the bird
     * @param width The width of the bird
     */
    public void updatePosition(Point birdPosition, double width) {
        double x, y;
        if (isPicked) {
            x = birdPosition.x + (width * ADJUSTMENT);
            y = birdPosition.y;
        } else if (isShot) {
            x = position.x + SHOOT_SPEED;
            y = position.y;
            frameCounter += 1;
        } else {
            x = position.x;
            y = position.y;
        }
        position = new Point(x, y);
    }

    /**
     * Draw weapon at its current position
     */
    public void drawWeapon() {
        WEAPON.draw(position.x, position.y);
    }

    /**
     * Update properties of weapon from picked to shot
     */
    public void shootWeapon() {
        isPicked = false;
        isShot = true;
    }

    /**
     * Move the weapon from right to left if it has not been picked
     */
    public void leftShift() {
        position = new Point(position.x - moveSpeed, position.y);
    }

    /**
     * Getter for bounding box of the weapon
     *
     * @return Return bounding box
     */
    // Method to get bounding box of weapon
    public Rectangle getBox() {
        return WEAPON.getBoundingBoxAt(position);
    }

    /**
     * Return the width of the weapon
     *
     * @return Returns the width
     */
    public double getWidth() {
        return WEAPON.getWidth();
    }

    /**
     * Return the weapon property hasPassed
     *
     * @return Returns true if weapon has passed
     */
    public boolean getHasPassed() {
        return hasPassed;
    }

    /**
     * Setter for weapon property isShot
     *
     * @param isShot Set true/false is weapon is shot or not
     */
    public void setIsShot(boolean isShot) {
        this.isShot = isShot;
    }

    /**
     * Setter for weapon property isPicked
     *
     * @param isPicked Set true/false is weapon is picked or not
     */
    public void setIsPicked(boolean isPicked) {
        this.isPicked = isPicked;
    }

    /**
     * Setter for current move speed of weapon
     *
     * @param moveSpeed Current speed as set by increase/decrease keys
     */
    public void setMoveSpeed(double moveSpeed){
        this.moveSpeed = moveSpeed;
    }
}
