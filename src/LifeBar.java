import bagel.*;

/**
 * A class to store all the lives of the bird and update accordingly
 */
public class LifeBar {
    private final Image HEALTH;
    private final Image NO_HEALTH;

    private final int X_POS = 100;
    private final int Y_POS = 15;
    private final int HEART_DISTANCE = 50;
    private final int LIVES;
    private int lives_left;

    /**
     * Creates a life bar for each level of the bird
     * @param level The current level being played
     */
    public LifeBar(int level) {
        HEALTH = new Image("res/level/fullLife.png");
        NO_HEALTH = new Image ("res/level/noLife.png");

        if (level == 0)
            LIVES = 3;
        else
            LIVES = 6;
        lives_left = LIVES;
    }

    /**
     * Render life bar on screen with given number of lives
     */
    public void drawLifeBar() {
        // Draw lives
        for(int i = 0; i < lives_left; ++i)
            HEALTH.drawFromTopLeft(X_POS + (HEART_DISTANCE * i), Y_POS);
        // Draw lives lost
        for(int i = lives_left; i < LIVES; ++i)
            NO_HEALTH.drawFromTopLeft(X_POS + (HEART_DISTANCE * i), Y_POS);
    }

    /**
     * Update lives lost with crash or out-of-bounds
     */
    public void lifeLost() {
        lives_left -= 1;
    }

    /**
     * Check if the bird has lives left
     *
     * @return Returns true if lives are left
     */
    public boolean hasLives() {
        return lives_left > 0;
    }
}
