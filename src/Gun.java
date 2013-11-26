package bot;

public abstract class Gun extends Component {

    protected double rotation;

    public Gun(State state) {
        super(state);
    }

    public double getRotation() {
        return this.rotation;
    }

}
