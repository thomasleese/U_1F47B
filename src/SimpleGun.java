package bot;

import robocode.*;
import robocode.util.*;

public class SimpleGun extends Gun {

    private double coefficient;

    public SimpleGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    private double calculateRotation(double bearing) {
        double rotation = bearing + this.state.owner.getHeading() - // absolute rotation to enemy
                          this.state.owner.getRadarHeading();   // relative rotation to gun
        return this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
    }

    @Override
    public void execute() {
        if (this.state.trackingRobot != null) {
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);
            this.rotation = this.calculateRotation(tick.bearing);
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

}
