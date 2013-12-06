package U_1F47B;

import java.util.ArrayList;
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
        if (this.state.trackingRobot != null) {
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);
            if (this.state.owner.getTime() - tick.time > 2) {
                this.rotation = Double.POSITIVE_INFINITY;
            } else {
                double rotation = tick.bearing + this.state.owner.getHeading() - // absolute rotation to enemy
                                  this.state.owner.getRadarHeading();   // relative rotation to gun
                this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
            }
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

}
