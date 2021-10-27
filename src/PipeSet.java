import bagel.*;
import bagel.util.*;

import java.lang.Math;
import java.util.ArrayList;

/**
 * Class to create a set of pipes and flames from the RightToLeft class
 */

public class PipeSet {
    // Constants
    private final ArrayList<RightToLeft> PIPES;
    private final ArrayList<RightToLeft> FLAMES;
    private final int FLAME_SPAWN_INTERVAL = 20;
    private final int FLAME_SPAWN_LENGTH = 3;
    private final int SPACING = 168;
    private final int LEVEL;

    // Game variables
    private boolean hasPassed;
    private boolean hasDrawnFlames;
    private int frameCounter;
    private int flameCounter;

    /**
     * Constructor that initializes pipe type based on level at a given point
     *
     * @param level The level of the pipe (plastic/steel)
     * @param centre The randomized centre of the pipe
     */
    public PipeSet(int level, int centre) {
        // Add pipes and flames to the list
        PIPES = new ArrayList<>();
        FLAMES = new ArrayList<>();

        addPipes(level, centre);
        addFlames(centre);

        frameCounter = 0;
        flameCounter = 0;
        hasPassed = false;
        hasDrawnFlames = false;
        this.LEVEL = level;
    }

    /**
     * Add pipes to the Array List (top and bottom)
     *
     * @param level The level of the pipe (plastic/steel)
     * @param centre The centre of the pipe
     */
    public void addPipes(int level, int centre) {
        int height = Window.getHeight();
        int width = Window.getWidth();

        String directory;
        Point topPos = new Point(width, - (height / 2.0) + centre);
        Point botPos = new Point(width, (height / 2.0) + centre + SPACING);
        DrawOptions option = new DrawOptions().setRotation(Math.PI);

        // Add pipes to the list
        if (level == 0)
            directory = "res/level/plasticPipe.png";
        else
            directory = "res/level-1/steelPipe.png";

        PIPES.add(new RightToLeft(new Image(directory), topPos));
        PIPES.add(new RightToLeft(new Image(directory), botPos, option));
    }

    /**
     * Add flames to the Array List
     *
     * @param centre The centre of the pipe to place the flames
     */
    public void addFlames(int centre) {
        int width = Window.getWidth();
        String directory = "res/level-1/flame.png";
        Image flame = new Image(directory);
        DrawOptions option = new DrawOptions().setRotation(Math.PI);

        Point topPos = new Point(width, centre + (flame.getHeight() / 2.0) - 1);
        Point botPos = new Point(width, centre + SPACING - (flame.getHeight() / 2.0) + 1);

        FLAMES.add(new RightToLeft(new Image(directory), topPos));
        FLAMES.add(new RightToLeft(new Image(directory), botPos, option));
    }

    /**
     * Draw all objects created, with flames on specific routine
     */
    public void drawObjects() {
        frameCounter += 1;

        // Draw pipes
        for (RightToLeft pipe: PIPES)
            pipe.drawObject();

        // Draw Flames
        if (LEVEL == 1) {
            if (hasDrawnFlames)
                for (RightToLeft flame: FLAMES)
                    flame.drawObject();

            // Update condition based on frame counter
            if (frameCounter % FLAME_SPAWN_INTERVAL == 0)
                if(!hasDrawnFlames) {
                    hasDrawnFlames = true;
                    flameCounter = frameCounter + FLAME_SPAWN_LENGTH;
                }
            if (frameCounter == flameCounter)
                hasDrawnFlames = false;
        }
    }

    /**
     * Shift all RightToLeft objects to the left
     */
    public void leftShift() {
        for (RightToLeft pipe: PIPES)
            pipe.leftShift();
        for (RightToLeft flame: FLAMES)
            flame.leftShift();
    }

    /**
     * Check if bird (and weapon) has collided with the pipe set
     *
     * @param bird The bird in play
     * @return Returns true if collision occurs
     */
    public boolean birdWeaponCollision(Bird bird) {
        if (bird.getHasPickedWeapon())
            if (checkCollision(bird.getWeapon().getBox(), true))
                return true;
        return checkCollision(bird.getBox(), true);
    }

    /**
     * Check if collision between given object and the pipes and flames
     *
     * @param box Bounding box of object to check collision with
     * @param withFlames If checking collision with flames as well
     * @return Returns true if collision occurs
     */
    public boolean checkCollision(Rectangle box, boolean withFlames) {
        if (withFlames)
            if (hasDrawnFlames)
                for (RightToLeft flame: FLAMES)
                    if (box.intersects(flame.getBox()))
                        return true;
        for (RightToLeft pipe: PIPES)
            if(box.intersects(pipe.getBox()))
                return true;

        // No intersection
        return false;
    }

    /**
     * Check if pipes and flames are still in the window
     *
     * @return Returns true if outside window
     */
    public boolean checkWindowBounds() {
        if (hasPassed)
            return PIPES.get(0).getBox().right() < 0;
        return false;
    }

    /**
     * Check if bird has passed the object
     *
     * @param bird The bird in play
     * @return Returns true if bird has passed
     */
    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > PIPES.get(0).getBox().right())
            hasPassed = true;
        return hasPassed;
    }

    /**
     * Returns hasPassed property of the pipe set
     *
     * @return Returns hasPassed
     */
    public boolean getHasPassed() {
        return hasPassed;
    }

    /**
     * Returns the width of the pipe
     *
     * @return Returns width
     */
    public double getWidth() {
        return PIPES.get(0).getWidth();
    }

    /**
     * Returns the x-position of the pipe (centre)
     *
     * @return Returns the x-pos
     */
    public double getX() {
        return PIPES.get(0).getPosition().x;
    }

    /**
     * Returns the current level of the pipe (steel/plastic)
     *
     * @return Returns level of the pipe set
     */
    public int getLEVEL() {
        return LEVEL;
    }
}
