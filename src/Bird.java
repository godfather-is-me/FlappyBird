import bagel.*;
import bagel.util.*;

public class Bird {
    // Local variables
    private final Image WING_UP;
    private final Image WING_DOWN;
    private final LifeBar LIFEBAR;
    private AbstractWeapon weapon;

    private Point position;
    private double velocity;
    private boolean hasPickedWeapon;
    private boolean hasShotWeapon;

    // Constants
    private final double WIDTH;
    private final double ACCELERATION = 0.4;
    private final double TERMINAL_VELOCITY = 10.0;
    private final Point INITIAL_POSITION = new Point(200, 350);

    // Bird constructor
    public Bird(Integer level) {
        WING_UP = new Image("res/level-" + level.toString() + "/birdWingUp.png");
        WING_DOWN = new Image("res/level-" + level.toString() + "/birdWingDown.png");

        LIFEBAR = new LifeBar(level);
        WIDTH = WING_UP.getWidth();

        // Load bird variables
        velocity = 0;
        position = INITIAL_POSITION;
        hasPickedWeapon = false;
        hasShotWeapon = false;
    }

    // Method to flap bird wings once every 10 frames
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
            weapon.updatePosition(position, WIDTH);
            weapon.drawWeapon();
        } else if (hasShotWeapon) {
            if (!weapon.checkOutOfRange()) {
                weapon.updatePosition(position, WIDTH);
                weapon.drawWeapon();
                // Check for collision with pipe
            } else
                removeWeapon();
        }
    }

    // Method to remove weapon
    public void removeWeapon() {
        weapon.setIsShot(false);
        hasShotWeapon = false;
        weapon = null;
    }

    // Method to add gravity effect
    public void gravity() {
        position = new Point(position.x, position.y + velocity);
        // Accelerate bird
        if ((velocity + ACCELERATION) <= TERMINAL_VELOCITY)
            velocity += ACCELERATION;
        else
            velocity = TERMINAL_VELOCITY;
    }

    // Method to add space bar jump
    public void pressedSpace(boolean isPressed) {
        velocity = (isPressed) ? -6.0 : velocity;
    }

    // Method to check if bird is out of bounds
    public boolean checkOutOfBoundsAndLives() {
        // If the centre of the bird if it is out-of-bounds
        if (position.y < 0 || position.y > Window.getHeight()) {
            lifeLost();
            if (hasLives())
                // Respawn
                respawn();
            else
                // Out of bounds and no lives left
                return true;
        }
        return false;
    }

    // Method to get bounding box of the bird
    public Rectangle getBirdBoundingBox() {
        return WING_UP.getBoundingBoxAt(position);
    }

    // Method to get current position of the bird
    public Point getPosition() {
        return position;
    }

    // Method to get hasPickedWeapon
    public boolean getHasPickedWeapon() {
        return hasPickedWeapon;
    }

    // Method to get hasShotWeapon
    public boolean getHasShotWeapon() {
        return hasShotWeapon;
    }

    // Method to set hasShotWeapon
    public void setHasShotWeapon() {
        this.hasShotWeapon = true;
        this.hasPickedWeapon = false;
        weapon.shootWeapon();
    }

    // Method to set the weapon of the bird
    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
        hasPickedWeapon = true;
    }

    // Method to get the weapon of the bird
    public AbstractWeapon getWeapon() {
        return weapon;
    }

    // Method to update lifeBar after collision
    public void lifeLost() {
        LIFEBAR.lifeLost();
    }

    // Method to check if bird has lives left
    public boolean hasLives() {
        return LIFEBAR.hasLives();
    }

    // Method to respawn bird at initial position once out of bounds
    public void respawn() {
        position = INITIAL_POSITION;
        velocity = 0;
    }
}
