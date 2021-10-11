import bagel.*;
import bagel.util.*;

public class RightToLeft {
    protected Point position;
    private final Image image;
    private final DrawOptions options;

    // Game variables
    protected boolean hasPassed;

    public RightToLeft(Image image, Point position) {
        this.image = image;
        this.position = position;
        options = null;
    }

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

    // Get bounding box for the given image
    public Rectangle getBox() {
        return image.getBoundingBoxAt(position);
    }

    // Get has passed
    public boolean getHasPassed() {
        return hasPassed;
    }

    // Draw object
    public void drawObject() {
        if (!(options == null))
            image.draw(position.x, position.y, options);
        else
            image.draw(position.x, position.y);
    }

    // Left shift
    public void leftShift() {
        position = new Point(position.x - GameManager.moveSpeed, position.y);
    }

    // Check for intersection or collision
    public boolean checkIntersection(Rectangle box) {
        return box.intersects(getBox());
    }

    // Check for out of bounds from window
    public boolean checkWindowBounds() {
        if (hasPassed)
            return getBox().right() < 0;
        return false;
    }

    // Get the width of image
    public double getWidth() {
        return image.getWidth();
    }

    // Get position
    public Point getPosition() {
        return position;
    }


}
