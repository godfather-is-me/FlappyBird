import bagel.*;
import bagel.util.*;

/**
 * A class that wraps all methods for object that move from right to left on the screen
 */
public class RightToLeft {
    protected Point position;
    private final Image image;
    private final DrawOptions options;

    // Game variables
    protected boolean hasPassed;

    /**
     * Constructor for objects that move from right to left with no drawoptions (stand upright)
     *
     * @param image Image of the object
     * @param position Initial position of the object
     */
    public RightToLeft(Image image, Point position) {
        this.image = image;
        this.position = position;
        options = null;
    }

    /**
     * Constructor for objects that move from right to left with specified drawoption variation
     *
     * @param image Image of the object
     * @param position Initial position of the object
     * @param options The draw options applied to the object for rendering
     */
    public RightToLeft(Image image, Point position, DrawOptions options) {
        this.image = image;
        this.position = position;
        this.options = options;
    }

    /**
     * Check if the object has passed the bird on screen
     *
     * @param bird The bird in play
     * @return Returns true if object has passed the bird
     */
    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > getBox().right())
            hasPassed = true;
        return hasPassed;
    }

    /**
     * Return the bounding box around the image
     *
     * @return Returns Rectangle box of the image
     */
    public Rectangle getBox() {
        return image.getBoundingBoxAt(position);
    }

    /**
     * Returns hasPassed property of the object
     *
     * @return Returns hasPassed (True if object has passed the bird)
     */
    public boolean getHasPassed() {
        return hasPassed;
    }

    /**
     * Render the object on screen
     */
    public void drawObject() {
        if (!(options == null))
            image.draw(position.x, position.y, options);
        else
            image.draw(position.x, position.y);
    }

    /**
     * Shift the object from right to left at constant moveSpeed
     */
    public void leftShift() {
        position = new Point(position.x - GameManager.moveSpeed, position.y);
    }

    /**
     * Check if objects intersects/collides with another Rectangle
     *
     * @param box Bounding box of other object
     * @return Returns true if intersection exists
     */
    public boolean checkIntersection(Rectangle box) {
        return box.intersects(getBox());
    }

    /**
     * Check if object has gone out of window bounds
     *
     * @return Returns true if object has passed the window on the left side
     */
    public boolean checkWindowBounds() {
        if (hasPassed)
            return getBox().right() < 0;
        return false;
    }

    /**
     * Returns width of the image
     *
     * @return Returns the width
     */
    public double getWidth() {
        return image.getWidth();
    }

    /**
     * Returns the current position of the object
     *
     * @return Returns position of object =
     */
    public Point getPosition() {
        return position;
    }
}
