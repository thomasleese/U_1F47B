package bot;

import robocode.*;
import robocode.util.*;

public class SimpleGun extends Gun {

    private double coefficient;

    private boolean overrideRotation = false;
    private double overrideRotationValue;

    public SimpleGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    @Override
    public void execute() {
        if (this.overrideRotation) {
            this.rotation = this.overrideRotationValue;
            this.overrideRotation = false;
            return;
        }

        if (this.state.latestRobot != null) {
            OtherRobot.Tick tick = this.state.latestRobot.getHistory(-1);
            double rotation = tick.bearing + this.state.owner.getHeading() - // absolute rotation to enemy
                              this.state.owner.getGunHeading(); // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        double rotation = e.getBearing() + this.state.owner.getHeading() - // absolute rotation to enemy
                          this.state.owner.getRadarHeading();   // relative rotation to gun
        this.overrideRotationValue = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        this.overrideRotation = true;
    }

}
