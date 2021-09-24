import bagel.*;
import bagel.util.*;

import java.util.*;

public class Pipes {
    // Pre-set heights for level 0
    private final int PIPE_SPAWN_LENGTH = 150;
    private final List<Integer> LEVEL0_GAPS = new ArrayList<>(){{
            add(100);
            add(300);
            add(500);
    }};


    // Store all pipes from current window in a Queue
    private final Queue<PipeSet> gamePipes;

    // Game variables
    private final int level;
    private int score;
    private int frameCounter;

    public Pipes(int level) {
        // Load pipes queue
        gamePipes = new LinkedList<>();

        // Game variables
        this.level = level;
        score = 0;
    }

    // Method to add new Pipe set into Queue
    public void addPipeSet() {
        Random rand = new Random();

        // Check level
        if (level == 0){
            // Choose only plastic pipes
            gamePipes.add(new PipeSet(0, LEVEL0_GAPS.get(rand.nextInt(LEVEL0_GAPS.size()))));
        } else {
            // if level 1 randomize pipes between plastic and steel
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
    public boolean checkCollision(Bird bird) {
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed()) {
                continue;
            }
            // At the next pipe which has not been passed
            return pipe.checkCollision(bird);
        }
        return false;
    }

    // Method to check if bird has passed the next pipe set that has not been passed
    public boolean checkPass(Bird bird) {
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed()) {
                continue;
            }
            if (pipe.checkPass(bird)) {
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

        // Check if it has been passed by bird
        if (head.getHasPassed())
            // Check the right side of pipe is beyond the window
            if (head.getTopRectangle().right() < 0)
                gamePipes.remove();
    }

    // Method to get score from after checking number of pipes passed
    public int getScore() {
        return score;
    }

}
