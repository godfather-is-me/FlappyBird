import bagel.*;
import bagel.util.*;

import java.util.Random;

public abstract class AbstractWeapon implements Spawnable{
    // Variables
    private Point position;
    private int moveSpeed;
    private int frameCounter;
    private boolean isShot;
    private boolean isPicked;
    private boolean hasPassed;

    // Constants
    private final int RANGE;
    private final int SHOOT_SPEED = 5;
    private final Image WEAPON;

    // Enum
    protected enum WEAPON_TYPE {BOMB, ROCK}
    private final WEAPON_TYPE type;

    // Constructor
    public AbstractWeapon (int range, Image weapon, WEAPON_TYPE type, PipeSet pipeSet) {
        this.WEAPON = weapon;
        this.RANGE = range;
        this.type = type;

        moveSpeed = 3;
        frameCounter = 0;

        isShot = false;
        isPicked = false;
        hasPassed = false;
        position = setInitialPosition(pipeSet);
    }

    // Method to set initial position of the weapon at random coordinates
    public Point setInitialPosition(PipeSet pipeSet) {
        Random rand = new Random();

        // Constants from game manager
        int pipeSpawnLength = GameManager.PIPE_SPAWN_LENGTH;
        int upperBound = GameManager.Y_UPPER_BOUND;
        int lowerBound = GameManager.Y_LOWER_BOUND;
        double speed = GameManager.speed;

        // Distance to next pipe
        double distance = (pipeSpawnLength * speed) - (pipeSet.getWidth() + getWidth());
        int x = rand.nextInt((int) distance) + Window.getWidth() + (int) ((pipeSet.getWidth() + getWidth()) / 2);
        int y = rand.nextInt(upperBound - lowerBound) + lowerBound;

        return new Point(x, y);
    }

    // Method to check if weapon and bird intersect
    public boolean checkPickUp(Bird bird) {
        if (!isPicked)
            return getBox().intersects(bird.getBox());
        return false;
    }

    // Method to check if weapon has passed the bird's y-coordinate
    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > WEAPON.getBoundingBoxAt(position).right())
            hasPassed = true;
        return hasPassed;
    }

    // Method to check if weapon is out of bounds
    public boolean checkWindowBounds() {
        if (hasPassed)
            return getBox().right() < 0;
        return false;
    }

    // Method to update position of weapon with bird
    public void updatePosition(Point birdPosition, double width) {
        double x, y;
        if (isPicked) {
            x = birdPosition.x + (width * 0.6);
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

    // Method to check if weapon fired is out of range
    public boolean checkOutOfRange() {
        return frameCounter > RANGE;
    }

    // Method to check if weapon and pipes intersect when shot
    public boolean checkDestruction(PipeSet pipeSet) {
        if ((pipeSet.getLevel() == 0) || (pipeSet.getLevel() == 1 && type == WEAPON_TYPE.BOMB)){
            if (pipeSet.getTopRectangle().intersects(getBox()))
                return true;
            return pipeSet.getBotRectangle().intersects(getBox());
        }
        return false;
    }

    // Method to draw weapon for the given position
    public void drawWeapon() {
        WEAPON.draw(position.x, position.y);
    }

    // Method to shoot weapon (change position every frame by speed)
    public void shootWeapon() {
        isPicked = false;
        isShot = true;
    }

    // Method to move shift weapon to the left with every frame
    public void leftShift() {
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

    // Method to get hasPassed
    public boolean getHasPassed() {
        return hasPassed;
    }

    // Method to set isPicked
    public void setIsPicked(boolean isPicked) {
        this.isPicked = isPicked;
    }

    // Method to set isShot
    public void setIsShot(boolean isShot) {
        this.isShot = isShot;
    }
}
