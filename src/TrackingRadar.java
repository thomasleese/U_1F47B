package bot;

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
        if (this.state.trackingRobot == null && this.state.otherRobots.size() > 0) {
            // we need a pick a new robot to track and hopefully destroy
            int i = Util.RANDOM.nextInt(this.state.otherRobots.size());
            String key = new ArrayList<String>(this.state.otherRobots.keySet()).get(i); // a bit horrible
            this.state.trackingRobot = this.state.otherRobots.get(key);
        }

        if (this.state.trackingRobot != null) {
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);
            double rotation = tick.bearing + this.state.owner.getHeading() - // absolute rotation to enemy
                              this.state.owner.getRadarHeading();   // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

}
