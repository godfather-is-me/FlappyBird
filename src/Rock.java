import bagel.*;

/**
 * Creates a rock class that extends functionality from Weapon
 */
public class Rock extends Weapon {

    /**
     * Constructor that feeds the rock parameters to the super (Weapon class)
     *
     * @param pipeSet The pipe set after which the weapon needs to be initialized
     */
    public Rock(PipeSet pipeSet) {
        super(
                new Image("res/level-1/rock.png"),
                WEAPON_TYPE.ROCK,
                pipeSet
        );
    }
}
