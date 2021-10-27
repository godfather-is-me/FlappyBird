import bagel.*;
import bagel.util.*;

/**
 * Contains all bird functionality used throughout the game
 */

public class Bird {
    // Local variables
    private final Image WING_UP;
    private final Image WING_DOWN;
    private final LifeBar LIFEBAR;
    private Weapon weapon;

    private Point position;
    private double velocity;
    private boolean hasPickedWeapon;
    private boolean hasShotWeapon;

    // Constants
    private final double WIDTH;
    private final double ACCELERATION = 0.4;
    private final double TERMINAL_VELOCITY = 10.0;
    private final Point INITIAL_POSITION = new Point(200, 350);

    /**
     * Creates a bird object based on the level of the game
     *
     * @param level Current level of the game
     */
    public Bird(Integer level) {
        String str_lvl = level.toString();

        WING_UP = new Image("res/level-" + str_lvl + "/birdWingUp.png");
        WING_DOWN = new Image("res/level-" + str_lvl+ "/birdWingDown.png");

        LIFEBAR = new LifeBar(level);
        WIDTH = WING_UP.getWidth();

        // Load bird variables
        velocity = 0;
        position = INITIAL_POSITION;
        hasPickedWeapon = false;
        hasShotWeapon = false;
    }

    /**
     * Renders bird on screen and flaps every 10 frames
     *
     * @param frameCounter Count the number of frames since bird has been spawned
     */
    public void drawBird(int frameCounter) {
        if ((frameCounter % 10) == 0)
            WING_UP.draw(position.x, position.y);
        else
            WING_DOWN.draw(position.x, position.y);

        // Apply gravity effect
        gravity();

        // Bird's lives
        LIFEBAR.drawLifeBar();

        // Draw picked up weapon at beak
        if (hasPickedWeapon) {
            if (!(weapon == null)) {
                weapon.updatePosition(position, WIDTH);
                weapon.drawObject();
            }
        } else if (hasShotWeapon) {
            if (!weapon.checkOutOfRange()) {
                weapon.updatePosition(position, WIDTH);
                weapon.drawObject();
                // Check for collision with pipe
            } else
                removeWeapon();
        }
    }

    /**
     * Add gravity like fall to the bird
     */
    public void gravity() {
        position = new Point(position.x, position.y + velocity);
        // Accelerate bird
        if ((velocity + ACCELERATION) <= TERMINAL_VELOCITY)
            velocity += ACCELERATION;
        else
            velocity = TERMINAL_VELOCITY;
    }

    /**
     * Add a jump to the bird for flight
     *
     * @param isPressed Key being pressed for jump
     */
    public void pressedSpace(boolean isPressed) {
        velocity = (isPressed) ? -6.0 : velocity;
    }

    /**
     * Remove weapon from bird after collision/out-of-range
     */
    public void removeWeapon() {
        weapon.setIsShot(false);
        hasShotWeapon = false;
        weapon = null;
    }

    /**
     * Check if bird has gone out of bounds
     *
     * @return Return true if bird is out bounds and has no lives left
     */
    public boolean checkOutOfBoundsAndLives() {
        // If the centre of the bird is out-of-bounds
        if (position.y < 0 || position.y > Window.getHeight()) {
            lifeLost();
            if (hasLives())
                respawn();
            else
                // Out of bounds and no lives left
                return true;
        }
        return false;
    }

    /**
     * Returns the current position of the bird
     *
     * @return Returns position of bird
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Returns the weapon of the bird
     *
     * @return Weapon currently held by bird
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Return value of hasPickedWeapon
     *
     * @return Returns true if bird has picked the weapon
     */
    public boolean getHasPickedWeapon() {
        return hasPickedWeapon;
    }

    /**
     * Return value of hasShotWeapon
     *
     * @return Returns true if bird has shot the weapon
     */
    public boolean getHasShotWeapon() {
        return hasShotWeapon;
    }

    /**
     * Returns bounding box of the bird
     *
     * @return Returns the rectangle of the bird
     */
    public Rectangle getBox() {
        return WING_UP.getBoundingBoxAt(position);
    }

    /**
     * Setter for hasShotWeapon, sets as true when called
     */
    public void setHasShotWeapon() {
        this.hasShotWeapon = true;
        this.hasPickedWeapon = false;
        weapon.shootWeapon();
    }

    /**
     * Setter to assign weapon to the bird
     *
     * @param weapon Weapon that has been picked by bird
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        hasPickedWeapon = true;
    }

    /**
     * Check if the bird has lives left
     *
     * @return Returns true if lives left
     */
    public boolean hasLives() {
        return LIFEBAR.hasLives();
    }

    /**
     * Update life bar (after collision)
     */
    public void lifeLost() {
        LIFEBAR.lifeLost();
    }

    /**
     * Respawn bird at initial position once out-of-bounds
     */
    public void respawn() {
        position = INITIAL_POSITION;
        velocity = 0;
    }
}
