import bagel.*;
import bagel.util.*;

import java.lang.Math;

public class PipeSet {
    private final Image TOP;
    private final Image BOTTOM;
    private final DrawOptions OPTION;

    private final Image TOP_FLAME;
    private final Image BOT_FLAME;

    private Point topPosition;
    private Point botPosition;
    private Point topFlamePosition;
    private Point botFlamePosition;

    // Game variables
    private boolean hasPassed;
    private boolean hasDrawnFlames;
    private double speed;

    // Constants
    private final int SPACING = 168;
    private final int LEVEL;

    // public static Queue<Pipes> gamePipes;

    // Pipes constructor
    public PipeSet(int level, int centre, double speed) {
        // Load pipes
        if (level == 0) {
            TOP = new Image("res/level/plasticPipe.png");
            BOTTOM = new Image("res/level/plasticPipe.png");
        } else {
            TOP = new Image("res/level-1/steelPipe.png");
            BOTTOM = new Image("res/level-1/steelPipe.png");
        }

        // Load flames
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
        this.speed = speed;
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

    // Method to update hasDrawnFlames if flames are seen
    public void updatedDrawnFlames(boolean hasDrawnFlames) {
        this.hasDrawnFlames = hasDrawnFlames;
    }

    // Method to move pipe to the left at a constant speed
    public void leftShift() {
        topPosition = new Point(topPosition.x - speed, topPosition.y);
        botPosition = new Point(botPosition.x - speed, botPosition.y);

        if (LEVEL == 1) {
            topFlamePosition = new Point(topFlamePosition.x - speed, topFlamePosition.y);
            botFlamePosition = new Point(botFlamePosition.x - speed, botFlamePosition.y);
        }
    }

    // Method to return rectangle of top pipe
    public Rectangle getTopRectangle() {
        return TOP.getBoundingBoxAt(topPosition);
    }

    // Method to return rectangle of bottom pipe
    public Rectangle getBotRectangle() {
        return BOTTOM.getBoundingBoxAt(botPosition);
    }

    // Method to return if pipe has been passed by bird
    public boolean getHasPassed() {
        return hasPassed;
    }

    // Method to modify speed based on the increase/decrease
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Method to check bird collision with pipe set
    public boolean checkCollision(Bird bird) {
        Rectangle birdBox = bird.getBirdBoundingBox();
        // Check if flames are drawn
        if (hasDrawnFlames) {
            if (birdBox.intersects(TOP_FLAME.getBoundingBoxAt(topFlamePosition)))
                return true;
            else if (birdBox.intersects(BOT_FLAME.getBoundingBoxAt(botFlamePosition)))
                return true;
        }
        return (birdBox.intersects(getTopRectangle()) || birdBox.intersects(getBotRectangle()));

    }

    // Method to check if bird has passed pipe set
    public boolean checkPass(Bird bird) {
        Rectangle birdBox = bird.getBirdBoundingBox();
        if (birdBox.left() > getTopRectangle().right()){
            hasPassed = true;
        }
        return hasPassed;
    }

    // Getter for pipe level
    public int getLevel() {
        return LEVEL;
    }
}
