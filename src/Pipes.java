import bagel.*;
import bagel.util.*;

import java.util.*;

public class Pipes {
    // Constants
    private final int PIPE_SPAWN_LENGTH = 150;
    private final int[] LEVEL0_GAPS = {100, 300, 500};

    // Store all pipes from current window in a Queue
    private final Queue<PipeSet> gamePipes;
    private final Bird BIRD;

    // Game variables
    private final int level;
    private int score;
    private int frameCounter;

    public Pipes(int level, Bird bird) {
        // Load objects
        gamePipes = new LinkedList<>();
        this.BIRD = bird;

        // Game variables
        score = 0;
        this.level = level;
    }

    // Method to add new Pipe set into Queue
    public void addPipeSet() {
        Random rand = new Random();
        if (level == 0)
            // Choose only plastic pipes
            gamePipes.add(new PipeSet(0, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)]));
        else {
            // if level 1 randomize pipes between plastic and steel
            gamePipes.add(new PipeSet(1, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)]));
        }
    }

    // Method to draw every pipe from queue
    public void drawPipes() {
        frameCounter += 1;

        // Spawn every 100 frames
        if (gamePipes.isEmpty())
            addPipeSet();
        else if ((frameCounter % PIPE_SPAWN_LENGTH) == 0)
            addPipeSet();

        // Remove pipes that are out of window
        checkPipeBounds();

        // Draw all pipes seen in queue
        for(PipeSet pipe : gamePipes){
            pipe.drawPipes();
        }
    }

    // Method to shift every pipe to the left
    public void leftShift() {
        for (PipeSet pipe: gamePipes){
            pipe.leftShift();
        }
    }

    // Method to check collision with the next pipe set that has not been passed
    public boolean checkCollisionAndLives() {
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed()) {
                continue;
            }
            // At the next pipe which has not been passed
            if (pipe.checkCollision(BIRD)) {
                BIRD.lifeLost();

                if (BIRD.hasLives())
                    gamePipes.remove(pipe);
                else
                 // Bird has collided and no lives left
                 return  true;
            }
            // The next pipe to be checked has not collided
            break;
        }
        return false;
    }

    // Method to check if bird has passed the next pipe set that has not been passed
    public boolean checkPass() {
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed())
                continue;

            if (pipe.checkPass(BIRD)) {
                score += 1;
                return true;
            } else
                return false;
        }
        return false;
    }

    // Method to pop pipe that has left the window
    public void checkPipeBounds() {
        PipeSet head = gamePipes.peek();
        if (head.getHasPassed())
            if (head.getTopRectangle().right() < 0)
                // Check the right side of pipe is beyond the window
                gamePipes.remove();
    }

    // Method to get score from after checking number of pipes passed
    public int getScore() {
        return score;
    }

}
