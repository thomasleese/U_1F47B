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

    private double calculateRotation(double bearing) {
        double rotation = bearing + this.state.owner.getHeading() - // absolute rotation to enemy
                          this.state.owner.getRadarHeading();   // relative rotation to gun
        return this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
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
            this.rotation = this.calculateRotation(tick.bearing);
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        this.overrideRotationValue = this.calculateRotation(e.getBearing());
        this.overrideRotation = true;
    }

}
