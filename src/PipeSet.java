import bagel.*;
import bagel.util.*;

import java.lang.Math;
import java.util.ArrayList;

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

    // Method to draw objects
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

    // Method to left shift
    public void leftShift() {
        for (RightToLeft pipe: PIPES)
            pipe.leftShift();
        for (RightToLeft flame: FLAMES)
            flame.leftShift();
    }

    // check if bird and weapon has collided with pipe set
    public boolean birdWeaponCollision(Bird bird) {
        if (bird.getHasPickedWeapon())
            if (checkCollision(bird.getWeapon().getBox(), true))
                return true;
        return checkCollision(bird.getBox(), true);
    }

    // Check collision between objects
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

    // Check if still in window
    public boolean checkWindowBounds() {
        if (hasPassed)
            return PIPES.get(0).getBox().right() < 0;
        return false;
    }

    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > PIPES.get(0).getBox().right())
            hasPassed = true;
        return hasPassed;
    }

    public boolean getHasPassed() {
        return hasPassed;
    }

    public double getWidth() {
        return PIPES.get(0).getWidth();
    }

    public double getX() {
        return PIPES.get(0).getPosition().x;
    }

    public int getLEVEL() {
        return LEVEL;
    }
}
