import bagel.*;

/**
 * Class that stores all messaging features
 */
public class Messages {
    private final Font FONT;

    /**
     * String: PRESS SPACE TO START
     */
    public static final String START_MESSAGE = "PRESS SPACE TO START";
    /**
     * String: SCORE:
     */
    public static final String SCORE_MESSAGE = "SCORE: ";
    /**
     * String: FINAL SCORE:
     */
    public static final String FINAL_SCORE = "FINAL SCORE: ";
    /**
     * String: GAME OVER
     */
    public static final String GAME_OVER = "GAME OVER";
    /**
     * String: CONGRATULATIONS!
     */
    public static final String WIN_MESSAGE = "CONGRATULATIONS!";
    /**
     * String: LEVEL-UP!
     */
    public static final String LEVEL_UP = "LEVEL-UP!";
    /**
     * String: PRESS 'S' TO SHOOT
     */
    public static final String SHOOT_MESSAGE = "PRESS 'S' TO SHOOT";

    private final int FONT_SIZE = 48;
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;

    /**
     * Initialize the messages class with the required font
     */
    public Messages() {
        FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);

        // Window size does not change for the duration of the game
        WINDOW_WIDTH = Window.getWidth();
        WINDOW_HEIGHT = Window.getHeight();
    }

    /**
     * Creates a centred text derived from the message
     *
     * @param message Constants from the class or user specified
     */
    public void getCentreMessage(String message) {
        FONT.drawString(message, getCentredX(message), getCentredY(0));
    }

    /**
     * Draw current score in the top-left corner of the window
     * @param score Current score of the bird
     */
    public void getCurrentScore(Integer score) {
        FONT.drawString(SCORE_MESSAGE + score.toString(), 100, 100);
    }

    /**
     * Render final score on screen when the game is over
     *
     * @param score Final score achieved by bird
     */
    public void getFinalScore(Integer score) {
        final int SHIFT_DOWN = 75;
        final String message = FINAL_SCORE + score.toString();
        FONT.drawString(message, getCentredX(message), getCentredY(SHIFT_DOWN));
    }

    /**
     * Render shoot instructions under the heading for level 1
     */
    public void getShootMessage() {
        final int SHIFT_DOWN = 68;
        FONT.drawString(Messages.SHOOT_MESSAGE, getCentredX(Messages.SHOOT_MESSAGE), getCentredY(SHIFT_DOWN));
    }

    /**
     * Return the centred x-coordinate based on the string
     *
     * @param message The String for which we are looking for the centred x-cord
     * @return Returns the centred x position
     */
    private double getCentredX(String message){
        return (WINDOW_WIDTH - FONT.getWidth(message)) / 2.0;
    }

    /**
     * Return the centred y-coordinate based on the shift
     *
     * @param shift The shift required up/down the centred text
     * @return Returns the centred y position
     */
    private double getCentredY(int shift) {
        // Font size observed to centre at half
        return ((WINDOW_HEIGHT + (FONT_SIZE / 2.0)) / 2.0) + shift;
    }
}
