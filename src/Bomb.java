import bagel.*;

public class Bomb extends Weapon {

    public Bomb(PipeSet pipeSet) {
        super(new Image("res/level-1/bomb.png"), WEAPON_TYPE.BOMB, pipeSet);
    }
}
