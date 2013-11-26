package bot;

public abstract class Radar extends Component {

    public Radar(State state) {
        super(state);
    }

    public abstract double getRotation();

}
