import bagel.*;

public class Rock extends Weapon {

    public Rock(PipeSet pipeSet) {
        super(new Image("res/level-1/rock.png"), WEAPON_TYPE.ROCK, pipeSet);
    }
}
