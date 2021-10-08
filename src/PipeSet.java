import bagel.*;
import bagel.util.*;

import java.lang.Math;

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
    // private final String[] PIPE_TYPES = {"/plastic", "/steel"};

    // Pipes constructor
    public PipeSet(int level, int centre, double moveSpeed) {
        // String str_lvl = level.toString();

        if (level == 0) {
            TOP = new Image("res/level/plasticPipe.png");
            BOTTOM = new Image("res/level/plasticPipe.png");
        } else {
            TOP = new Image("res/level-1/steelPipe.png");
            BOTTOM = new Image("res/level-1/steelPipe.png");
        }

        // TOP = new Image("res/level-" + str_lvl + PIPE_TYPES[level] + "Pipe.png");
        // BOTTOM = new Image("res/level-" + str_lvl + PIPE_TYPES[level] + "Pipe.png");

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

    // Method to draw top and bottom pipes
    public void drawPipes() {
        TOP.draw(topPosition.x, topPosition.y);
        BOTTOM.draw(botPosition.x, botPosition.y, OPTION);
    }

    // Method to draw flames every 20 frames for 3 flames
    public void drawFlames() {
        TOP_FLAME.draw(topFlamePosition.x, topFlamePosition.y);
        BOT_FLAME.draw(botFlamePosition.x, botFlamePosition.y, OPTION);
    }

    // Method to move pipe to the left at a constant speed
    public void leftShift() {
        topPosition = new Point(topPosition.x - moveSpeed, topPosition.y);
        botPosition = new Point(botPosition.x - moveSpeed, botPosition.y);

        if (LEVEL == 1) {
            topFlamePosition = new Point(topFlamePosition.x - moveSpeed, topFlamePosition.y);
            botFlamePosition = new Point(botFlamePosition.x - moveSpeed, botFlamePosition.y);
        }
    }

    // Method to check bird collision with pipe set
    public boolean birdWeaponCollision(Bird bird) {
        if (bird.getHasPickedWeapon())
            return checkCollision(bird.getWeapon().getBox());
        return checkCollision(bird.getBox());
    }

    // Method to check if pipeSet is out of the window
    public boolean checkWindowBounds() {
        if (hasPassed)
            return getBox().right() < 0;
        return false;
    }

    // Method to generalize check collision for rectangles
    public boolean checkCollision(Rectangle box) {
        if (hasDrawnFlames) {
            if (box.intersects(TOP_FLAME.getBoundingBoxAt(topFlamePosition)))
                return true;
            else if (box.intersects(BOT_FLAME.getBoundingBoxAt(botFlamePosition)))
                return true;
        }
        return (box.intersects(getTopRectangle()) || box.intersects(getBotRectangle()));
    }

    // Method to check if bird has passed pipe set
    public boolean checkBirdPass(Bird bird) {
        if (bird.getPosition().x > getBox().right()){
            hasPassed = true;
        }
        return hasPassed;
    }

    // Method to return rectangle of top pipe
    public Rectangle getTopRectangle() {
        return TOP.getBoundingBoxAt(topPosition);
    }

    // Method to return rectangle of bottom pipe
    public Rectangle getBotRectangle() {
        return BOTTOM.getBoundingBoxAt(botPosition);
    }

    // Method similar to getTopRectangle
    public Rectangle getBox() {
        return getTopRectangle();
    }

    // Method to return if pipe has been passed by bird
    public boolean getHasPassed() {
        return hasPassed;
    }

    // Getter for pipe level
    public int getLevel() {
        return LEVEL;
    }

    // Getter for width of the pipe
    public double getWidth() {
        return TOP.getWidth();
    }

    // Method to modify speed based on the increase/decrease
    public void setSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    // Method to update hasDrawnFlames if flames are seen
    public void setHasDrawnFlames(boolean hasDrawnFlames) {
        this.hasDrawnFlames = hasDrawnFlames;
    }
}
