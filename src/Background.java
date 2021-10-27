import bagel.*;

/**
 * Creates and displays the backgrounds based on current level played
 */

public class Background {
    private final Image BACKGROUND;
    private final double WINDOW_WIDTH;
    private final double WINDOW_HEIGHT;

    /**
     * Creates a background object with current level of the game
     * @param level Level of the game playing
     */
    public Background(int level){
        // Window size never changes
        WINDOW_WIDTH = Window.getWidth() / 2.0;
        WINDOW_HEIGHT = Window.getHeight() / 2.0;

        // Load appropriate background
        if (level == 0)
            BACKGROUND = new Image("res/level-0/background.png");
        else
            BACKGROUND = new Image("res/level-1/background.png");
    }

    /**
     * Display background on screen
     */
    public void displayBackground(){
        BACKGROUND.draw(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}
