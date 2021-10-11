import bagel.*;
import bagel.util.*;

import java.lang.Math;

/**
 * Creates a set of pipes whenever called with all pipe related functionality
 */
public class PipeSet implements Spawnable{
    private final Image TOP;
    private final Image BOTTOM;
    private final Image TOP_FLAME;
    private final Image BOT_FLAME;
    private final DrawOptions OPTION;

    private Point topPosition;
    private Point botPosition;
    private Point topFlamePosition;
    private Point botFlamePosition;

    // Game variables
    private double moveSpeed;
    private boolean hasPassed;
    private boolean hasDrawnFlames;

    // Constants
    private final int LEVEL;
    private final int SPACING = 168;

    /**
     * Creates a new set of pipes spawned at the edge of the right border
     *
     * @param level The level of the game
     * @param centre A randomized centre location
     * @param moveSpeed The move speed of the pipe depending on timescale
     */
    public PipeSet(int level, int centre, double moveSpeed) {
        if (level == 0) {
            TOP = new Image("res/level/plasticPipe.png");
            BOTTOM = new Image("res/level/plasticPipe.png");
        } else {
            TOP = new Image("res/level-1/steelPipe.png");
            BOTTOM = new Image("res/level-1/steelPipe.png");
        }

        TOP_FLAME = new Image("res/level-1/flame.png");
        BOT_FLAME = new Image("res/level-1/flame.png");
        OPTION = new DrawOptions().setRotation(Math.PI);

        // Get window parameters
        int width = Window.getWidth();
        int height = Window.getHeight();

        // Load positions with spacing in y-axis, pipe rendered from centre
        topPosition = new Point(width, - (height / 2.0) + centre);
        botPosition = new Point(width, (height / 2.0) + centre + SPACING);

        // Get position of flames right out of the pipe
        topFlamePosition = new Point(width, centre + (TOP_FLAME.getHeight() / 2.0) - 1);
        botFlamePosition = new Point(width, centre + SPACING - (BOT_FLAME.getHeight() / 2.0) + 1);

        hasPassed = false;
        hasDrawnFlames = false;
        this.moveSpeed = moveSpeed;
        this.LEVEL = level;
    }

    /**
     * Draw top and bottom pipes
     */
    public void drawPipes() {
        TOP.draw(topPosition.x, topPosition.y);
        BOTTOM.draw(botPosition.x, botPosition.y, OPTION);
    }


    /**
     * Draw flames from pipes
     */
    public void drawFlames() {
        TOP_FLAME.draw(topFlamePosition.x, topFlamePosition.y);
        BOT_FLAME.draw(botFlamePosition.x, botFlamePosition.y, OPTION);
    }

    /**
     * Move pipes from right to left at constant speed
     */
    public void leftShift() {
        topPosition = new Point(topPosition.x - moveSpeed, topPosition.y);
        botPosition = new Point(botPosition.x - moveSpeed, botPosition.y);

        if (LEVEL == 1) {
            topFlamePosition = new Point(topFlamePosition.x - moveSpeed, topFlamePosition.y);
            botFlamePosition = new Point(botFlamePosition.x - moveSpeed, botFlamePosition.y);
        }
    }

    /**
     * Check if bird (and weapon) has collided with the pipe set
     *
     * @param bird The bird in play
     * @return Return true if bird has collided
     */
    public boolean birdWeaponCollision(Bird bird) {
        if (bird.getHasPickedWeapon())
            return checkCollision(bird.getWeapon().getBox());
        return checkCollision(bird.getBox());
    }

    /**
     * Check if pipe set is beyond window (out-of-bounds)
     *
     * @return Return true if out-of-bounds
     */
    public boolean checkWindowBounds() {
        if (hasPassed)
            return getBox().right() < 0;
        return false;
    }

    /**
     * Check collision between both top and bottom pipes
     *
     * @param box The box of the object required to check collision with (Bird)
     * @return Return true if collision has occurred
     */
    public boolean checkCollision(Rectangle box) {
        if (hasDrawnFlames) {
            if (box.intersects(TOP_FLAME.getBoundingBoxAt(topFlamePosition)))
                return true;
            else if (box.intersects(BOT_FLAME.getBoundingBoxAt(botFlamePosition)))
                return true;
        }
        return (box.intersects(getTopRectangle()) || box.intersects(getBotRectangle()));
    }

    /**
     * Check if the bird has passed the pipe (no collision)
     *
     * @param bird The bird in play
     * @return Return true if bird has passed succesfully
     */
    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > getBox().right()){
            hasPassed = true;
        }
        return hasPassed;
    }

    /**
     * Return rectangle of top pipe
     *
     * @return Returns rectangle of top pipe
     */
    public Rectangle getTopRectangle() {
        return TOP.getBoundingBoxAt(topPosition);
    }

    /**
     * Return rectangle of bottom pipe
     *
     * @return Returns rectangle of bottom pipe
     */
    public Rectangle getBotRectangle() {
        return BOTTOM.getBoundingBoxAt(botPosition);
    }

    /**
     * Return box of top pipe, used for checking passes
     *
     * @return Returns rectangle of top pipe
     */
    public Rectangle getBox() {
        return getTopRectangle();
    }

    /**
     * Return pipe property - hasPassed
     *
     * @return Returns hasPassed if bird has passed pipe
     */
    public boolean getHasPassed() {
        return hasPassed;
    }

    /**
     * Return pipe property - getLevel
     *
     * @return Returns level of pipe (plastic/steel)
     */
    public int getLevel() {
        return LEVEL;
    }

    /**
     * Return width of pipe
     *
     * @return Returns width
     */
    public double getWidth() {
        return TOP.getWidth();
    }

    /**
     * Return current speed of pipe set
     *
     * @return Returns moveSpeed
     */
    public double getMoveSpeed(){
        return moveSpeed;
    }

    /**
     * Return current position of top pipe
     *
     * @return Returns Point of top pipe
     */
    public Point getPosition() {
        return topPosition;
    }

    /**
     * Setter for current speed based on timescale control
     */
    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * Setter for hasDrawnFlames for flame visibility
     */
    public void setHasDrawnFlames(boolean hasDrawnFlames) {
        this.hasDrawnFlames = hasDrawnFlames;
    }
}
