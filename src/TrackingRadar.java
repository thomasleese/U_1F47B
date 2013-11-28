package bot;

import robocode.*;
import robocode.util.*;

public class TrackingRadar extends Radar {

    private double coefficient;

    public TrackingRadar(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    @Override
    public void execute() {
        if(this.state.latestRobot != null) {
            OtherRobot.Tick tick = this.state.latestRobot.getHistory(-1);
            double rotation = tick.bearing + this.state.owner.getHeading() - // absolute rotation to enemy
                              this.state.owner.getRadarHeading();   // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

}
