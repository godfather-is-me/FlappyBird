import bagel.*;

public class Rock extends AbstractWeapon {
    public static final int ROCK_RANGE = 25;

    public Rock(PipeSet pipe) {
        super(ROCK_RANGE, new Image("res/level-1/rock.png"), 0, pipe);
    }
}
