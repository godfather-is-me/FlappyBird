import bagel.Window;
import bagel.util.Point;

import java.util.*;

public class Pipes {
    // Constants
    private final int PIPE_SPAWN_LENGTH = 150;
    private final int[] LEVEL0_GAPS = {100, 300, 500};

    // Store all pipes from current window in a Queue
    private final Queue<PipeSet> gamePipes;
    private final Queue<AbstractWeapon> weapons;
    private final Bird BIRD;

    // Game variables
    private final int level;
    private int score;
    private int frameCounter;
    private int timeScale;
    private double speed;
    private int frameDuration;

    public Pipes(int level, Bird bird) {
        // Load objects
        gamePipes = new LinkedList<>();
        weapons = new LinkedList<>();
        this.BIRD = bird;

        // Game variables
        score = 0;
        speed = 3.0;
        timeScale = 1;
        frameDuration = 0;
        this.level = level;
    }

    // Method to add new Pipe set into Queue
    public void addPipeSet() {
        Random rand = new Random();
        if (level == 0)
            // Choose only plastic pipes
            gamePipes.add(new PipeSet(level, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)], speed));
        else {
            // Random y between 100-500
            int randY = rand.nextInt(400) + 100;
            int randPipe = rand.nextInt(2);
            gamePipes.add(new PipeSet(randPipe, randY, speed));
        }
    }

    // Method to add new weapon set into Queue
    public void addWeapon(PipeSet pipe) {
        // Random rand = new Random();
        Bomb tempBomb = new Bomb();
        weapons.add(tempBomb);
        weaponStartPosition(pipe, tempBomb);
    }

    // Method to randomize x location of weapon
    public void weaponStartPosition(PipeSet pipe, AbstractWeapon wp) {
        Random rand = new Random();
        if (!pipe.getHasWeaponAfter()) {
            // Both top and bottom have the same right x
            if (pipe.getTopRectangle().right() < Window.getWidth()){
                // Distance to next pipe
                double distance = PIPE_SPAWN_LENGTH * speed;
                // Available x coordinates
                distance -= (pipe.getWidth() + wp.getWidth());
                int x = (int) (rand.nextInt((int) distance) + pipe.getTopRectangle().right());
                int y = rand.nextInt(400) + 100;
                wp.setPosition(new Point(x, y));
                pipe.setHasWeaponAfter(true);
            }
        }

    }

    // Method to draw every pipe from queue
    public void drawPipes() {
        frameCounter += 1;

        // Spawn every 100 frames
        if (gamePipes.isEmpty()){
            addPipeSet();
        }

        else if ((frameCounter % PIPE_SPAWN_LENGTH) == 0)
            addPipeSet();

        // Remove pipes that are out of window
        checkPipeBounds();

        // Draw all pipes seen in queue
        for(PipeSet pipe : gamePipes){
            pipe.drawPipes();
            addWeapon(pipe);

            // Draw flames
            if (pipe.getLevel() == 1) {
                if ((frameCounter % 20) == 0)
                    frameDuration = frameCounter + 3;
                if (frameCounter < frameDuration) {
                    pipe.drawFlames();
                    pipe.setHasDrawnFlames(true);
                } else
                    pipe.setHasDrawnFlames(false);
            }
        }

        // Draw all weapons
        for (AbstractWeapon wp: weapons)
            if (wp.getPosition() != null)
                wp.drawWeapon();
    }

    // Method to shift every pipe to the left
    public void leftShift() {
        for (PipeSet pipe: gamePipes)
            pipe.leftShift();

        for (AbstractWeapon wp: weapons)
            if (wp.position != null)
                wp.leftShift();
    }

    // Method to check collision with the next pipe set that has not been passed
    public boolean checkCollisionAndLives() {
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed())
                continue;

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
        if (!gamePipes.isEmpty()) {
            PipeSet head = gamePipes.peek();
            if (head.getHasPassed())
                if (head.getTopRectangle().right() < 0)
                    // Check the right side of pipe is beyond the window
                    gamePipes.remove();
        }
    }

    // Method to get score from after checking number of pipes passed
    public int getScore() {
        return score;
    }

    // Method to speed up the pipes in the game
    public void speedUp(boolean isPressed) {
        if (isPressed)
            if (timeScale < 5) {
                timeScale += 1;
                speed += 1.5;
                for (PipeSet pipe: gamePipes)
                    pipe.setSpeed(speed);
            }
    }

    // Method to slow down the pipes in the game
    public void slowDown(boolean isPressed) {
        if (isPressed)
            if (timeScale > 1) {
                timeScale -= 1;
                speed -= 1.5;
                for (PipeSet pipe: gamePipes)
                    pipe.setSpeed(speed);
            }
    }

}
