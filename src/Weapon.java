import bagel.*;
import bagel.util.*;
import java.util.Random;

/**
 * Contains all standard methods used by the weapon.
 */

public class Weapon extends RightToLeft{
    // Constants
    private final int RANGE;
    private final int SHOOT_SPEED = 5;
    private final double ADJUSTMENT = 0.6;

    // Game Variables
    private int frameCounter;
    private boolean isShot;
    private boolean isPicked;

    // Enum
    protected enum WEAPON_TYPE {BOMB, ROCK}
    private final WEAPON_TYPE type;
    private final int BOMB_RANGE = 50;
    private final int ROCK_RANGE = 25;

    /**
     * The Weapon constructor used as a super constructor for
     * bombs and rocks to create a weapon object.
     *
     * @param weapon An Image object containing the weapon in context
     * @param type The specific type of weapon required, available from WEAPON_TYPE enum
     * @param pipeSet The pipe set after which this particular weapon has been spawned
     */
    public Weapon (Image weapon, WEAPON_TYPE type, PipeSet pipeSet) {
        super(weapon, new Point(0,0));
        position = setInitialPosition(pipeSet);

        this.type = type;
        if (type == WEAPON_TYPE.BOMB)
            RANGE = BOMB_RANGE;
        else
            RANGE = ROCK_RANGE;

        frameCounter = 0;
        isShot = false;
        isPicked = false;
        hasPassed = false;
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
        double speed = GameManager.moveSpeed;

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
            return checkIntersection(bird.getBox());
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
        if ((pipeSet.getLEVEL() == 0) || (pipeSet.getLEVEL() == 1 && type == WEAPON_TYPE.BOMB))
            return  pipeSet.checkCollision(getBox(), false);
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
     * Update properties of weapon from picked to shot
     */
    public void shootWeapon() {
        isPicked = false;
        isShot = true;
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
}
