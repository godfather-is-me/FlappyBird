import bagel.*;

public class Bomb extends AbstractWeapon {
    public static final int BOMB_RANGE = 50;

    public Bomb(PipeSet pipeSet) {
        super(BOMB_RANGE, new Image("res/level-1/bomb.png"), WEAPON_TYPE.BOMB, pipeSet);
    }
}
