package bot;

public class TrackingRadar extends Radar {

	public TrackingRadar(State state) {
		super(state);
	}

	@Override
	public double getRotation() {
		return 0.0;
	}

}
