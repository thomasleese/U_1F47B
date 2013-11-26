package bot;

import robocode.util.Utils;

public class SimpleGun extends Gun {

	private double coefficient;

	public SimpleGun(State state, double coefficient) {
		super(state);
		this.coefficient = coefficient;
	}

	@Override
	public double getRotation() {
		if(this.state.latestRobot != null) {
			OtherRobot.Tick tick = this.state.latestRobot.getHistory(-1);
			double rotation = tick.bearing + this.state.owner.getHeading() - // absolute rotation to enemy
							  this.state.owner.getGunHeading();	// relative rotation to gun
			return this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}

}