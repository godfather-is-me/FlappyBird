import bagel.*;

public class Rock extends AbstractWeapon {
    public static final int ROCK_RANGE = 25;

    public Rock(PipeSet pipeSet) {
        super(ROCK_RANGE, new Image("res/level-1/rock.png"), WEAPON_TYPE.ROCK, pipeSet);
    }
}
