package bot;

public abstract class Base extends Component {

	public Base(State state) {
		super(state);
	}

    public abstract double getSpeed();
    public abstract double getRotation();

}
