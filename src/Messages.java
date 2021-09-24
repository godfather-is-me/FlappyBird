import bagel.*;

public class Messages {
    private final Font FONT;

    // Constants
    public static final String START_MESSAGE = "PRESS SPACE TO START";
    public static final String SCORE_MESSAGE = "SCORE: ";
    public static final String FINAL_SCORE = "FINAL SCORE: ";
    public static final String GAME_OVER = "GAME OVER";
    public static final String WIN_MESSAGE = "CONGRATULATIONS!";

    private final int FONT_SIZE = 48;
    private final int SHIFT_DOWN = 75;
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;

    // Messages constructor
    public Messages() {
        FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);

        // Window size does not change for the duration of the game
        WINDOW_WIDTH = Window.getWidth();
        WINDOW_HEIGHT = Window.getHeight();
    }

    // Method to output formatted start message
    public void getCentreMessage(String message) {
        FONT.drawString(message, getCentredX(message), getCentredY(false));
    }

    // Method to output current score
    public void getCurrentScore(Integer score) {
        FONT.drawString(SCORE_MESSAGE + score.toString(), 100, 100);
    }

    // Method to output final score
    public void getFinalScore(Integer score) {
        final String message = FINAL_SCORE + score.toString();
        FONT.drawString(message, getCentredX(message), getCentredY(true));
    }

    // Method to return the centred x coordinate
    public double getCentredX(String message){
        return (WINDOW_WIDTH - FONT.getWidth(message)) / 2.0;
    }

    // Method to return the centred y coordinate
    public double getCentredY(boolean shift) {
        // Font size observed to be 24 pixels along y-axis while running game
        if (shift)
            return ((WINDOW_HEIGHT + (FONT_SIZE / 2.0)) / 2.0) + SHIFT_DOWN;
        else
            return (WINDOW_HEIGHT + (FONT_SIZE / 2.0)) / 2.0;
    }
}
