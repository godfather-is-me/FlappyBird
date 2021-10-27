import bagel.*;
import bagel.util.*;

import java.util.ArrayList;

/**
 * Contains all bird functionality used throughout the game
 */

public class Bird {
    // Local variables
    private final Image WING_UP;
    private final Image WING_DOWN;
    private final LifeBar LIFEBAR;
    private final ArrayList<Weapon> weapons;

    private Point position;
    private double velocity;
    private boolean hasPickedWeapon;

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
        hasPickedWeapon = false;
        position = INITIAL_POSITION;
        weapons = new ArrayList<>();
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

        // Draw weapons
        drawWeapons();
    }

    /**
     * Draw all weapons held and shot by bird from the weapons array
     */
    public void drawWeapons() {
        for (Weapon weapon: weapons) {
            if (weapon.getIsPicked())
                weapon.updatePosition(position, WIDTH);
            else if (weapon.getIsShot())
                if (!weapon.checkOutOfRange())
                    weapon.updatePosition();
                else {
                    removeWeapon(weapon);
                    break;
                }
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
    public void removeWeapon(Weapon weapon) {
        weapons.remove(weapon);
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
     * Checking for destruction from any of the shot weapons
     *
     * @param pipeSet The pipe in front of the bird in play
     * @return Returns true if pipe and shot weapon intersect
     */
    public boolean checkWeaponDestruction(PipeSet pipeSet) {
        for (Weapon weapon: weapons)
            if (weapon.checkDestruction(pipeSet)) {
                removeWeapon(weapon);
                return true;
            }
        return false;
    }

    /**
     * Resets bird properties for weapons and shoots the currently held weapon
     */
    public void shootTheWeapon() {
        this.hasPickedWeapon = false;
        Weapon temp = weapons.get(weapons.size() - 1);
        if (!temp.getIsShot())
            temp.shootWeapon();
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
     * Return value of hasPickedWeapon
     *
     * @return Returns true if bird has picked the weapon
     */
    public boolean getHasPickedWeapon() {
        return hasPickedWeapon;
    }

    /**
     * Returns the picked weapon that is part of the bird until shot
     *
     * @return Returns weapon held by bird
     */
    public Weapon getPickedWeapon() {
        return weapons.get(weapons.size() - 1);
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
     * Adds the weapon to the weapons array and changes bird properties
     *
     * @param weapon Weapon that has been picked by bird
     */
    public void setWeapon(Weapon weapon) {
        weapons.add(weapon);
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
