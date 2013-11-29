package bot;

import robocode.*;
import robocode.util.*;

public class LinearPredictiveGun extends Gun {

    private double coefficient;

    public LinearPredictiveGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    @Override
    public void execute() {
        // does not use coefficient
        if (this.state.trackingRobot != null) {
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);

            // need a way to approximate velocity using history
            // if no history, wait a bit so we have some?
            double velX = tick.velocity.getX(); // target velocity
            double velY = tick.velocity.getY();

            // out velocity
            double mvX = Math.sin(this.state.owner.getHeadingRadians()) * this.state.owner.getVelocity();
            double mvY = Math.cos(this.state.owner.getHeadingRadians()) * this.state.owner.getVelocity();
            double locX = this.state.owner.getX() + mvX;
            double locY = this.state.owner.getY() + mvY;
            double targX = tick.position.getX() + velX;
            double targY = tick.position.getY() + velY;

            double dist = tick.distance;
            this.bulletPower = 2.0;

            this.bulletPower += 80/(dist - 35); // adjust this

            double projectileSpeed = Util.firepowerToSpeed(this.bulletPower);

            double timeSteps = dist / projectileSpeed;

            double afterDist = Util.getDistance(locX - (targX + velX * timeSteps), locY - (targY + velY * timeSteps));
            double afterTimeSteps = afterDist / projectileSpeed;

            double adqTimeSteps = (afterTimeSteps + timeSteps) * 0.5;

            double litDir = Util.getAngle(locX - (targX + velX * adqTimeSteps), locY - (targY + velY * adqTimeSteps));

            double rotation = litDir - this.state.owner.getGunHeading(); // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            // leave the rotation as it is allready
        }
    }

}
