import bagel.*;

/**
 * Creates a bomb class that extends functionality from Weapon
 */
public class Bomb extends Weapon {

    /**
     * Constructor that feeds the bomb parameters to the super (Weapon class)
     *
     * @param pipeSet The pipe set after which the weapon needs to be initialized
     */
    public Bomb(PipeSet pipeSet) {
        super(
                new Image("res/level-1/bomb.png"),
                WEAPON_TYPE.BOMB,
                pipeSet
        );
    }
}
