package bot;

public abstract class Gun extends Component {

	public Gun(State state) {
		super(state);
	}

    public abstract double getRotation();

}
