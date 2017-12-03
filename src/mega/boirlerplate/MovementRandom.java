package mega.boirlerplate;

/**
 * 
 * @author Gates
 *
 */
public class MovementRandom extends Movement {

	/**
	 * 
	 * @param state
	 */
    public MovementRandom(State state) {
        super(state);
    }

    /**
     * 
     */
    @Override
    public void execute() {
        this.speed = Math.random()*16 - 8;
        this.rotation = Math.random()*360 - 180;
    }

}
