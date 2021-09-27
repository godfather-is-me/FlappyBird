import bagel.*;
import bagel.util.*;

import java.util.Random;

public abstract class AbstractWeapon {
    // Variables
    protected Point position;
    protected int shootSpeed;
    protected int moveSpeed;
    protected boolean isShot;
    protected boolean isPicked;
    protected boolean hasPassed;
    protected int currentDistance;

    // Constants
    protected final Image WEAPON;
    protected final int RANGE;
    protected final int WEAPON_TYPE;        // 0 for stone, 1 for bomb

    // Constructor
    public AbstractWeapon (int range, Image weapon, int type, PipeSet pipe) {
        this.WEAPON = weapon;
        this.RANGE = range;
        this.WEAPON_TYPE = type;

        moveSpeed = 3;
        shootSpeed = 5;
        currentDistance = 0;

        isShot = false;
        isPicked = false;
        hasPassed = false;

        position = setInitialPosition(pipe);
    }

    // Method to set initial position of the weapon at random coordinates
    public Point setInitialPosition(PipeSet pipe) {
        Random rand = new Random();

        // Constants from game manager
        int pipeSpawnLength = GameManager.PIPE_SPAWN_LENGTH;
        int upperBound = GameManager.Y_UPPER_BOUND;
        int lowerBound = GameManager.Y_LOWER_BOUND;
        double speed = GameManager.speed;

        // Distance to next pipe
        double distance = (pipeSpawnLength * speed) - (pipe.getWidth() + getWidth());
        int x = rand.nextInt((int) distance) + Window.getWidth() + (int) (pipe.getWidth() / 2);
        int y = rand.nextInt(upperBound - lowerBound) + lowerBound;

        pipe.setHasWeaponAfter(true);
        return new Point(x, y);
    }

    // Method to check if weapon and bird intersect
    public boolean checkPickUp(Bird bird) {
        if (!isPicked)
            return getBox().intersects(bird.getBirdBoundingBox());
        return false;
    }

    // Method to check if weapon has passed the bird's y-coordinate
    public boolean checkPass(Bird bird) {
        if (bird.getPosition().x > WEAPON.getBoundingBoxAt(position).right())
            hasPassed = true;
        return hasPassed;
    }

    // Method to update position of weapon with bird
    public void updatePosition(Point birdPosition, double width) {
        double x, y;
        if (isPicked) {
            x = birdPosition.x + (width * 0.6);
            y = birdPosition.y;
        } else if (isShot) {
            x = position.x + shootSpeed;
            y = position.y;
            currentDistance += shootSpeed;
        } else {
            x = position.x;
            y = position.y;
        }
        position = new Point(x, y);
    }

    // Method to check if weapon fired is out of range
    public boolean checkOutOfRange() {
        if (isShot)
            if (currentDistance <= RANGE)
                return false;
        return true;
    }

    // Method to check if weapon and pipes intersect when shot
    public boolean checkDestruction(PipeSet pipe) {
        if (isShot) {
            // Check for plastic pipe
            if ((pipe.getLevel() == 0) || (pipe.getLevel() == 1 && WEAPON_TYPE == 1)){
                // Intersection of pipe and weapon
                if (pipe.getTopRectangle().intersects(WEAPON.getBoundingBoxAt(position)))
                    return true;
                return pipe.getBotRectangle().intersects(WEAPON.getBoundingBoxAt(position));
            }
        }
        return false;
    }

    // Method to draw weapon for the given position
    public void drawWeapon() {
        WEAPON.draw(position.x, position.y);
    }

    // Method to shoot weapon (change position every frame by speed)
    public void shootWeapon() {
        if (isPicked) {
            isPicked = false;
            isShot = true;
        }
    }

    // Method to move shift weapon to the left with every frame
    public void leftShift() {
        if (!isShot)
            position = new Point(position.x - moveSpeed, position.y);
    }

    // Method to get bounding box of weapon
    public Rectangle getBox() {
        return WEAPON.getBoundingBoxAt(position);
    }

    // Method to get width of the weapon
    public double getWidth() {
        return WEAPON.getWidth();
    }

    // Method to get position
    public Point getPosition() {
        return position;
    }

    // Method to set position, used initially
    public void setPosition(Point initialPosition) {
        position = initialPosition;
    }

    // Method to get hasPassed
    public boolean getHasPassed() {
        return hasPassed;
    }

    // Method to set isPicked
    public void setIsPicked(boolean isPicked) {
        this.isPicked = isPicked;
    }

    // Method to get isPicked
    public boolean getIsPicked() {
        return isPicked;
    }

    // Method to set isShot
    public void setIsShot(boolean isShot) {
        this.isShot = isShot;
    }
}
