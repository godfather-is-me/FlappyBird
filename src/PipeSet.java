import bagel.*;
import bagel.util.*;

import java.lang.Math;

public class PipeSet {
    private final Image TOP;
    private final Image BOTTOM;
    private final DrawOptions OPTION;

    private Point topPosition;
    private Point botPosition;

    // Game variables
    private boolean hasPassed;
    private double speed;
    private int timescale;

    // Constants
    private final int SPACING = 168;

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

        OPTION = new DrawOptions().setRotation(Math.PI);

        // Load positions with spacing in y-axis, pipe rendered from centre
        topPosition = new Point(Window.getWidth(), - (Window.getHeight() / 2.0) + centre);
        botPosition = new Point(Window.getWidth(), (Window.getHeight() / 2.0) + centre + SPACING);

        hasPassed = false;
        this.speed = speed;
        timescale = 1;
    }

    // Method to draw top and bottom pipes
    public void drawPipes() {
        TOP.draw(topPosition.x, topPosition.y);
        BOTTOM.draw(botPosition.x, botPosition.y, OPTION);
    }

    // Method to move pipe to the left at a constant speed
    public void leftShift() {
        topPosition = new Point(topPosition.x - speed, topPosition.y);
        botPosition = new Point(botPosition.x - speed, botPosition.y);
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
}
