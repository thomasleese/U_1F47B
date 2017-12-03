package mega.boirlerplate;

/**
 * 
 * @author Gates
 *
 */
public abstract class Radar extends Component {

    protected double rotation;

    /**
     * 
     * @param state
     */
    public Radar(State state) {
        super(state);
    }

    /**
     * 
     * @return
     */
    public double getRotation() {
        return this.rotation;
    }

}
