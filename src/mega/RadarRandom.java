package mega;

/**
 * 
 * @author Gates
 *
 */
public class RadarRandom extends Radar {

	/**
	 * 
	 * @param state
	 */
    public RadarRandom(State state) {
        super(state);
    }

    /**
     * 
     */
    @Override
    public void execute() {
        this.rotation = Math.random()*360 - 180;
    }

}
