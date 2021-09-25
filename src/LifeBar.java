import bagel.*;
import bagel.util.*;

public class LifeBar {
    private final Image HEALTH;
    private final Image NO_HEALTH;

    private final int X_POS = 100;
    private final int Y_POS = 15;
    private final int HEART_DISTANCE = 50;
    private final int LIVES;
    private int lives_left;

    // Constructor
    public LifeBar(int level) {
        HEALTH = new Image("res/level/fullLife.png");
        NO_HEALTH = new Image ("res/level/noLife.png");

        if (level == 0)
            LIVES = 3;
        else
            LIVES = 6;
        lives_left = LIVES;
    }

    // Method to render number of lives left on screen
    public void drawLifeBar() {
        // Draw lives
        for(int i = 0; i < lives_left; ++i)
            HEALTH.drawFromTopLeft(X_POS + (HEART_DISTANCE * i), Y_POS);
        // Draw lives lost
        for(int i = lives_left; i<LIVES; ++i)
            NO_HEALTH.drawFromTopLeft(X_POS + (HEART_DISTANCE * i), Y_POS);
    }

    // Method to update lives left
    public void lifeLost() {
        lives_left -= 1;
    }

    // Method to check if bird has lives left
    public boolean hasLives() {
        return lives_left > 0;
    }
}
