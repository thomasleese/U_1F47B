package bot;

public abstract class Base extends Component {

	public Base(State state) {
		super(state);
	}

    public abstract double getSpeed();
    public abstract double getRotation();

    public boolean isOutOfBattleField(double x, double y) {
        return x > this.state.owner.getBattleFieldWidth() - (this.state.owner.getWidth() / 2) || // right edge
               x < this.state.owner.getWidth() / 2 || // left edge
               y > this.state.owner.getBattleFieldHeight() - (this.state.owner.getHeight() / 2) || // top edge
               y < this.state.owner.getHeight() / 2; // bottom edge
    }

}
