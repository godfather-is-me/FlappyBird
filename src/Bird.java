import bagel.*;
import bagel.util.*;

public class Bird {
    // Local variables
    private final Image WING_UP;
    private final Image WING_DOWN;
    private final LifeBar LIFEBAR;

    private Point position;
    private double velocity;

    // Constants
    private final double ACCELERATION = 0.4;
    private final double TERMINAL_VELOCITY = 10.0;
    private final Point INITIAL_POSITION = new Point(200, 350);

    // Bird constructor
    public Bird(int level) {

        if (level == 0) {
            // Load Images
            WING_UP = new Image("res/level-0/birdWingUp.png");
            WING_DOWN = new Image("res/level-0/birdWingDown.png");
        } else {
            WING_UP = new Image("res/level-1/birdWingUp.png");
            WING_DOWN = new Image("res/level-1/birdWingDown.png");
        }
        // Load bird variables
        LIFEBAR = new LifeBar(level);
        position = INITIAL_POSITION;
        velocity = 0;
    }

    // Method to flap bird wings once every 10 frames
    public void drawBird(int frameCounter) {
        if ((frameCounter % 10) == 0)
            WING_UP.draw(position.x, position.y);
        else
            WING_DOWN.draw(position.x, position.y);

        // Bird's lives
        LIFEBAR.drawLifeBar();
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
