import bagel.*;
import bagel.util.*;

import java.lang.Math;

public class Pipes {
    private final Image TOP;
    private final Image BOTTOM;
    private final DrawOptions OPTION;

    private Point topPosition;
    private Point botPosition;

    // Constants
    private final int SPACING = 168 / 2;
    private final int SPEED = 5;

    // Pipes constructor
    public Pipes() {
        // Load pipes
        TOP = new Image("res/level/plasticPipe.png");
        BOTTOM = new Image("res/level/plasticPipe.png");
        OPTION = new DrawOptions().setRotation(Math.PI);

        // Load positions with spacing in y-axis, pipe rendered from centre
        topPosition = new Point(Window.getWidth(), -SPACING);
        botPosition = new Point(Window.getWidth(), Window.getHeight() + SPACING);
    }

    // Method to draw top and bottom pipes
    public void drawPipes() {
        TOP.draw(topPosition.x, topPosition.y);
        BOTTOM.draw(botPosition.x, botPosition.y, OPTION);
    }

    // Method to move pipe to the left at a constant speed
    public void leftShift() {
        topPosition = new Point(topPosition.x - SPEED, topPosition.y);
        botPosition = new Point(botPosition.x - SPEED, botPosition.y);
    }

    // Method to return rectangle of top pipe
    public Rectangle getTopRectangle() {
        return TOP.getBoundingBoxAt(topPosition);
    }

    // Method to return rectangle of bottom pipe
    public Rectangle getBotRectangle() {
        return BOTTOM.getBoundingBoxAt(botPosition);
    }
}
