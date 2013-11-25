package bot;

public abstract class Component {
	protected State state;

	public Component(State state) {
		this.state = state;
	}
}