import java.util.*;

public class GameManager {
    // Constants
    public static final int PIPE_SPAWN_LENGTH = 150;
    private final int FLAME_SPAWN_LENGTH = 20;
    public static final int Y_LOWER_BOUND = 100;
    public static final int Y_UPPER_BOUND = 500;
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
    private int flameDuration;
    public static double speed;

    public GameManager(int level, Bird bird) {
        // Load objects
        gamePipes = new LinkedList<>();
        weapons = new LinkedList<>();
        this.BIRD = bird;

        // Game variables
        score = 0;
        speed = 3.0;
        timeScale = 1;
        flameDuration = 0;
        this.level = level;
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
        for(PipeSet pipe : gamePipes){
            pipe.drawPipes();

            // Draw flames
            if (pipe.getLevel() == 1) {
                if ((frameCounter % FLAME_SPAWN_LENGTH) == 0)
                    flameDuration = frameCounter + 3;
                if (frameCounter < flameDuration) {
                    pipe.drawFlames();
                    pipe.setHasDrawnFlames(true);
                } else
                    pipe.setHasDrawnFlames(false);
            }
        }

        // Draw all weapons in queue
        for (AbstractWeapon wp: weapons)
            wp.drawWeapon();

        // Draw bird
        BIRD.drawBird(frameCounter);
    }

    // Method to add objects to the queues
    public void addObjects() {
        // Add pipes to the queue every 100 frames
        if (gamePipes.isEmpty())
            addPipeSet();
        else if ((frameCounter % PIPE_SPAWN_LENGTH) == 0)
            addPipeSet();
    }

    // Method to add new Pipe set into Queue
    public void addPipeSet() {
        Random rand = new Random();
        // Choose only plastic pipes
        if (level == 0)
            gamePipes.add(new PipeSet(level, LEVEL0_GAPS[rand.nextInt(LEVEL0_GAPS.length)], speed));
        else {
            int randPipe = rand.nextInt(2);
            int randY = rand.nextInt(Y_UPPER_BOUND - Y_LOWER_BOUND) + Y_LOWER_BOUND;
            PipeSet tempPipe = new PipeSet(randPipe, randY, speed);
            // Add weapon and pipe set
            gamePipes.add(tempPipe);
            addWeapon(tempPipe);
        }
    }

    // Method to add new weapon set into Queue
    public void addWeapon(PipeSet pipe) {
        Random rand = new Random();
        if (rand.nextInt(2) == 0)
            weapons.add(new Rock(pipe));
        else
            weapons.add(new Bomb(pipe));
    }

    // Method to shift every pipe to the left
    public void leftShift() {
        for (PipeSet pipe: gamePipes)
            pipe.leftShift();

        for (AbstractWeapon wp: weapons)
            wp.leftShift();
    }

    // Method to check collision with the next pipe set that has not been passed
    public boolean checkCollisionAndLives() {
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed())
                continue;

            // At the next pipe which has not been passed
            if (pipe.checkBirdCollision(BIRD) || pipe.checkWeaponCollision(BIRD)) {
                BIRD.lifeLost();
                if (BIRD.hasLives())
                    gamePipes.remove(pipe);
                else
                 // Bird has collided and no lives left
                 return  true;
            }

            // Check if the weapon has collided with pipe when shot
            if (BIRD.getHasShotWeapon()) {
                if (BIRD.getWeapon().checkDestruction(pipe)) {
                    score += 1;
                    gamePipes.remove(pipe);
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
        for (PipeSet pipe: gamePipes) {
            if (pipe.getHasPassed())
                continue;
            if (pipe.checkPass(BIRD))
                score += 1;
            break;
        }

        for (AbstractWeapon weapon: weapons){
            if (weapon.getHasPassed())
                continue;
            weapon.checkPass(BIRD);
            break;
        }
    }

    // Method to pop the pipe that has left the window
    public void checkPipeBounds() {
        if (!gamePipes.isEmpty()) {
            PipeSet head = gamePipes.peek();
            if (head.getHasPassed())
                if (head.getTopRectangle().right() < 0)
                    // Check the right side of pipe is beyond the window
                    gamePipes.remove();
        }
    }

    // Method to pop the weapon that has left the window
    public void checkWeaponBounds() {
        if (!weapons.isEmpty()) {
            AbstractWeapon head = weapons.peek();
            if (head.getHasPassed())
                if (head.getBox().right() < 0)
                    weapons.remove();
        }
    }

    // Method to see if the weapon has been picked up or not
    public void pickWeapon() {
        if (!BIRD.getHasPickedWeapon()) {
            for (AbstractWeapon weapon : weapons) {
                if (weapon.getHasPassed())
                    continue;

                if (weapon.checkPickUp(BIRD)) {
                    weapon.isPicked = true;
                    BIRD.setWeapon(weapon);
                    weapons.remove(weapon);
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
