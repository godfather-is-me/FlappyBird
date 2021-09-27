import bagel.*;
import bagel.util.*;

public abstract class AbstractWeapon {
    // Variables
    protected Point position;
    protected int shootSpeed;
    protected int moveSpeed;
    protected boolean isShot;
    protected boolean isPicked;

    // Constants
    protected final Image WEAPON;
    protected final int RANGE;
    protected final int WEAPON_TYPE;        // 0 for stone, 1 for bomb


    // Constructor
    public AbstractWeapon (int range, Image weapon, int type) {
        this.WEAPON = weapon;
        this.RANGE = range;
        this.WEAPON_TYPE = type;

        moveSpeed = 3;
        shootSpeed = 5;
    }

    // Method to check if weapon and bird intersect
    public boolean checkPickUp(Bird bird) {
        if (!isPicked)
            return getBox().intersects(bird.getBirdBoundingBox());
        return false;
    }

    // Method to check if weapon and pipes/flames intersect when bird carries
            // ------ Implemented in pipe set

    // Method to update position of weapon with bird
    public void updatePosition(Bird bird) {
        double x, y;
        if (isPicked) {
            x = bird.getPosition().x + (bird.getWidth() / 2.0);
            y = bird.getPosition().y;
        } else if (isShot) {
            x = position.x + shootSpeed;
            y = position.y;
        } else {
            // Function should not be called if isPicked or isShot not true
            x = 0;
            y = 0;
        }
        position = new Point(x, y);
    }

    // Method to check if weapon and pipes intersect when shot
    public boolean checkDestruction(PipeSet pipe) {
        if (isShot) {
            // Check for plastic pipe
            if ((pipe.getLevel() == 0) || (pipe.getLevel() == 1 && WEAPON_TYPE == 1)){
                // Intersection of pipe and weapon
                if (pipe.getTopRectangle().intersects(WEAPON.getBoundingBoxAt(position)))
                    return true;
                return pipe.getTopRectangle().intersects(WEAPON.getBoundingBoxAt(position));
            }
        }
        return false;
    }

    // Method to delete weapon if range exceeded
            // To implement in Weapons queue

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
        if (isShot)
            position = new Point(position.x + 5, position.y);
    }

    // Method to move shift weapon to the left with every frame
    public void leftShift() {
        if (!isShot)
            position = new Point(position.x - moveSpeed, position.y);
    }

    // Method to randomize location of weapon between pipes
            // Implement with Weapons queue as AddWeapon and helper functions

    // Method to randomize between weapons (in Weapons Queue)
            // Implement with Weapons queue as (no weapons/rock/bomb)

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
}
