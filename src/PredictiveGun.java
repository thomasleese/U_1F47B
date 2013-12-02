package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class PredictiveGun extends Gun {

    private double coefficient;
    private boolean shouldFireNextTick = false;
    private double nextProjectileSpeed = 19.7;
    private double expectedGunDir = 0;

    private Vector predVec;
    private OtherRobot.PresentHistoryDatas phs = OtherRobot.PresentHistoryDatas.none;

    public PredictiveGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    @Override
    public void execute() {
        // does not use coefficient
        shouldFire = shouldFireNextTick;
        shouldFireNextTick = false;
        if (this.state.trackingRobot != null) {
            if (shouldFire)
            {
                // only fire if our gun is pointing in the right direction
                if (Math.abs(Utils.normalRelativeAngleDegrees(expectedGunDir - this.state.owner.getGunHeading())) <= 0.2)
                {
                    this.bulletPower = Util.speedToFirepower(nextProjectileSpeed);
                }
                else
                    shouldFire = false;
            }

            // see how much good data we have
            phs = this.state.trackingRobot.availablePresentHistoryData(this.state.owner.getTime());

            if (phs == OtherRobot.PresentHistoryDatas.positionVelocity)
                shouldFireNextTick = true;
            else if (phs == OtherRobot.PresentHistoryDatas.positionVelocityTurnRate)
                shouldFireNextTick = true;

            // predict his location
            OtherRobot.Tick tick = this.state.trackingRobot.getHistory(-1);

            // our velocity
            double mvX = Math.sin(this.state.owner.getHeadingRadians()) * this.state.owner.getVelocity();
            double mvY = Math.cos(this.state.owner.getHeadingRadians()) * this.state.owner.getVelocity();
            double locX = this.state.owner.getX() + mvX;
            double locY = this.state.owner.getY() + mvY;

            double dist = tick.distance;
            double projectileSpeed = 19.7; // need to be able to turn this into power

            double cutoff = 500.0;
            if (dist < cutoff)
            {
                projectileSpeed = (dist / cutoff) * Util.firepowerToSpeed(Rules.MIN_BULLET_POWER);
            }
            double minSpeed = Util.firepowerToSpeed(Rules.MAX_BULLET_POWER);
            if (projectileSpeed < minSpeed) // clamp to MIN_VELOCITY
            {
                projectileSpeed = minSpeed;
            }
            nextProjectileSpeed = projectileSpeed;

            double timeSteps = dist / projectileSpeed;

            // initial sub-iter
            this.predVec = this.state.trackingRobot.predictLocation((int)timeSteps + 1, ProjectedBot.TurnBehaviours.keepTurn, ProjectedBot.SpeedBehaviours.keepSpeed);

            for (int i = 0; i < 1; i++) // number of iterations to perform (you'd think more would make it better, but it's hard to tell)
            {
                double afterDist = Util.getDistance(locX - predVec.getX(), locY - predVec.getY());
                double afterTimeSteps = afterDist / projectileSpeed;

                double adqTimeSteps = ((afterTimeSteps - 1.0) * 0.5 + timeSteps * 0.5); // take weighted average (remove 1st +1 adjustment from afterTimeSteps)
                timeSteps = afterTimeSteps - 1.0;
                this.predVec = this.state.trackingRobot.predictLocation((int)adqTimeSteps + 1, ProjectedBot.TurnBehaviours.keepTurn, ProjectedBot.SpeedBehaviours.keepSpeed);
            }

            double litDir = Util.getAngle(locX - predVec.getX(), locY - predVec.getY());
            expectedGunDir = litDir;

            double rotation = litDir - this.state.owner.getGunHeading(); // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            // leave the rotation as it is allready
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        if (this.state.trackingRobot != null) {
            double x = this.predVec.getX();
            double y = this.predVec.getY();

            int a = 36;

            if (phs == OtherRobot.PresentHistoryDatas.none)
                g.setColor(new Color(255, 0, 0));
            else if (phs == OtherRobot.PresentHistoryDatas.positionOnly)
                g.setColor(new Color(255, 85, 0));
            else if (phs == OtherRobot.PresentHistoryDatas.positionVelocity)
                g.setColor(new Color(255, 170, 0));
            else if (phs == OtherRobot.PresentHistoryDatas.positionVelocityTurnRate)
                g.setColor(new Color(255, 255, 0));
            g.drawRect((int) x - a/2, (int) y - a/2, a, a);
        }
    }

}
