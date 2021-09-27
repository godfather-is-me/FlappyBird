import java.util.*;

public class GameManager {
    // Constants
    public static final int PIPE_SPAWN_LENGTH = 150;
    public static final int FLAME_SPAWN_LENGTH = 20;
    public static final int Y_LOWER_BOUND = 100;
    public static final int Y_UPPER_BOUND = 500;
    private final int[] LEVEL0_GAPS = {100, 300, 500};

    // Store all pipes from current window in a Queue
    private final Queue<PipeSet> GAME_PIPES;
    private final Queue<AbstractWeapon> WEAPONS;
    private final Bird BIRD;

    // Game variables
    private final int LEVEL;
    private int score;
    private int frameCounter;
    private int timeScale;
    private int flameDuration;
    public static double speed;

    public GameManager(int level, Bird bird) {
        // Load objects
        this.BIRD = bird;
        GAME_PIPES = new LinkedList<>();
        WEAPONS = new LinkedList<>();

        // Game variables
        score = 0;
        speed = 3.0;
        timeScale = 1;
        flameDuration = 0;
        this.LEVEL = level;
    }

    // Method to draw every pipe and weapon from queue
    public void drawObjects() {
        frameCounter += 1;

        // Add weapons and pipes according to specs
        addObjects();

        // Remove pipes and weapons that are out of window
        checkPipeBounds();
        checkWeaponBounds();

        // Draw all pipes in queue
        for(PipeSet pipeSet : GAME_PIPES){
            pipeSet.drawPipes();

            // Draw flames
            if (pipeSet.getLevel() == 1) {
                if ((frameCounter % FLAME_SPAWN_LENGTH) == 0)
                    flameDuration = frameCounter + 3;
                if (frameCounter < flameDuration) {
                    pipeSet.drawFlames();
                    pipeSet.setHasDrawnFlames(true);
                } else
                    pipeSet.setHasDrawnFlames(false);
            }
        }

        // Draw all weapons in queue
        for (AbstractWeapon weapon: WEAPONS)
            weapon.drawWeapon();

        // Draw bird
        BIRD.drawBird(frameCounter);
    }

    // Method to add objects to the queues
    public void addObjects() {
        // Add pipes to the queue every 100 frames
        if (GAME_PIPES.isEmpty())
            addPipeSet();
        else if ((frameCounter % PIPE_SPAWN_LENGTH) == 0)
            addPipeSet();
    }

    // Method to add new Pipe set into Queue
    public void addPipeSet() {
        Random rand = new Random();
        // Choose only plastic pipes
        if (LEVEL == 0)
            GAME_PIPES.add(new PipeSet(LEVEL, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)], speed));
        else {
            int randPipe = rand.nextInt(2);
            int randY = rand.nextInt(Y_UPPER_BOUND - Y_LOWER_BOUND) + Y_LOWER_BOUND;
            PipeSet tempPipe = new PipeSet(randPipe, randY, speed);
            // Add weapon and pipe set
            GAME_PIPES.add(tempPipe);
            addWeapon(tempPipe);
        }
    }

    // Method to add new weapon set into Queue
    public void addWeapon(PipeSet pipeSet) {
        Random rand = new Random();
        if (rand.nextInt(2) == 0)
            WEAPONS.add(new Rock(pipeSet));
        else
            WEAPONS.add(new Bomb(pipeSet));
    }

    // Method to shift every pipe to the left
    public void leftShift() {
        for (PipeSet pipeSet: GAME_PIPES)
            pipeSet.leftShift();

        for (AbstractWeapon weapon: WEAPONS)
            weapon.leftShift();
    }

    // Method to check collision with the next pipe set that has not been passed
    public boolean checkCollisionAndLives() {
        for (PipeSet pipeSet: GAME_PIPES) {
            if (pipeSet.getHasPassed())
                continue;

            // At the next pipe which has not been passed
            if (pipeSet.birdWeaponCollision(BIRD)) {
                BIRD.lifeLost();
                if (BIRD.hasLives())
                    GAME_PIPES.remove(pipeSet);
                else
                 // Bird has collided and no lives left
                 return  true;
            }

            // Check if the weapon has collided with pipe when shot
            if (BIRD.getHasShotWeapon()) {
                if (BIRD.getWeapon().checkDestruction(pipeSet)) {
                    score += 1;
                    GAME_PIPES.remove(pipeSet);
                    BIRD.removeWeapon();
                }
            }

            // The next pipe to be checked has not collided
            break;
        }
        return false;
    }

    // Method to check if bird has passed the next pipe set that has not been passed
    public void checkPass() {
        for (PipeSet pipeSet: GAME_PIPES) {
            if (pipeSet.getHasPassed())
                continue;
            if (pipeSet.checkBirdPass(BIRD))
                score += 1;
            break;
        }

        for (AbstractWeapon weapon: WEAPONS){
            if (weapon.getHasPassed())
                continue;
            weapon.checkBirdPass(BIRD);
            break;
        }
    }

    // Method to pop the pipe that has left the window
    public void checkPipeBounds() {
        if (!GAME_PIPES.isEmpty()) {
            if (GAME_PIPES.peek().checkWindowBounds())
                GAME_PIPES.remove();
        }
    }

    // Method to pop the weapon that has left the window
    public void checkWeaponBounds() {
        if (!WEAPONS.isEmpty()) {
            if (WEAPONS.peek().checkWindowBounds())
                WEAPONS.remove();
        }
    }

    // Method to see if the weapon has been picked up or not
    public void pickWeapon() {
        if (!BIRD.getHasPickedWeapon()) {
            for (AbstractWeapon weapon : WEAPONS) {
                if (weapon.getHasPassed())
                    continue;

                if (weapon.checkPickUp(BIRD)) {
                    weapon.setIsPicked(true);
                    BIRD.setWeapon(weapon);
                    WEAPONS.remove(weapon);
                    break;
                }
            }
        }
    }

    // Method to shoot weapon held by bird
    public void shootWeapon(boolean keyPress) {
        if (keyPress)
            if (BIRD.getHasPickedWeapon())
                BIRD.setHasShotWeapon();
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
                for (PipeSet pipeSet: GAME_PIPES)
                    pipeSet.setSpeed(speed);
            }
    }

    // Method to slow down the pipes in the game
    public void slowDown(boolean isPressed) {
        if (isPressed)
            if (timeScale > 1) {
                timeScale -= 1;
                speed -= 1.5;
                for (PipeSet pipeSet: GAME_PIPES)
                    pipeSet.setSpeed(speed);
            }
    }

}
