import bagel.*;
import bagel.util.*;

public class Bird {
    // Local variables
    private final Image WING_UP;
    private final Image WING_DOWN;

    private Point position;
    private double velocity;

    // Constants
    private final double ACCELERATION = 0.4;
    private final double TERMINAL_VELOCITY = 10.0;

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
        position = new Point(200, 350);
        velocity = 0;
    }

    // Method to flap bird wings once every 10 frames
    public void drawBird(int frameCounter) {
        if ((frameCounter % 10) == 0)
            WING_UP.draw(position.x, position.y);
        else
            WING_DOWN.draw(position.x, position.y);
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

    // Method to check collision with pipes
    public boolean checkCollision(Rectangle pipe) {
        // Both wing positions have the same bounding box for a given position
        Rectangle birdBox = WING_UP.getBoundingBoxAt(position);
        return birdBox.intersects(pipe);
    }

    // Method to check if bird is out of bounds
    public boolean checkOutBounds() {
        // If the centre of the bird if it is out-of-bounds
        return position.y < 0 || position.y > Window.getHeight();
    }

    // Method to check if bird has surpassed x value pipes
    public boolean checkWin(Rectangle pipe) {
        Rectangle birdBox = WING_UP.getBoundingBoxAt(position);
        return birdBox.left() > pipe.right();
    }

    // Method to respawn bird at initial position once out of bounds
}
