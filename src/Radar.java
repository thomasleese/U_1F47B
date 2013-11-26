package bot;

public abstract class Radar extends Component {

    protected double rotation;

    public Radar(State state) {
        super(state);
    }

    public double getRotation() {
        return this.rotation;
    }

}
