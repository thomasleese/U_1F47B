package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class PredictiveGun extends Gun {

    private double coefficient;
    private boolean shouldFireNextTick = false;
    private double nextProjectileSpeed = 19.7;
    
    private Vector predVec;
    private OtherRobot.PresentHistoryDatas phs = OtherRobot.PresentHistoryDatas.none;

    public PredictiveGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    @Override
    public void execute() {
        // does not use coefficient
        if (this.state.trackingRobot != null) {
            shouldFire = shouldFireNextTick;
            shouldFireNextTick = false;
            if (shouldFire)
                this.bulletPower = Util.speedToFirepower(nextProjectileSpeed);

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

            double cutoff = 200.0;
            if (dist < cutoff)
            {
                projectileSpeed = (dist / cutoff) * 19.7;
            }
            nextProjectileSpeed = projectileSpeed;

            double timeSteps = dist / projectileSpeed;

            this.predVec = this.state.trackingRobot.predictLocation((int)timeSteps + 1, ProjectedBot.TurnBehaviours.keepTurn, ProjectedBot.SpeedBehaviours.keepSpeed);

            double afterDist = Util.getDistance(locX - predVec.getX(), locY - predVec.getY());
            double afterTimeSteps = afterDist / projectileSpeed;

            double adqTimeSteps = (afterTimeSteps * 0.8 + timeSteps * 0.2); // take weighted average

            this.predVec = this.state.trackingRobot.predictLocation((int)adqTimeSteps + 1, ProjectedBot.TurnBehaviours.keepTurn, ProjectedBot.SpeedBehaviours.keepSpeed);

            double litDir = Util.getAngle(locX - predVec.getX(), locY - predVec.getY());

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
