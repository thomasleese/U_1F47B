package U_1F47B;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;
import robocode.util.*;

public class PredictiveGun extends Gun {

    private static final double MIN_SHOOT_SPEED = 18.7;

    private double coefficient;
    private boolean shouldFireNextTick = false;
    private double nextProjectileSpeed = 19.7;
    private double expectedGunDir = 0;

    private Vector predVec;

    private ArrayList<Vector> paintPreds = new ArrayList<Vector>();
    private OtherRobot.PresentHistoryDatas phs = OtherRobot.PresentHistoryDatas.none;
    private PredictionData pd = null;
    
    private int missCount = 0;
    private int predCycle = 0;
    private int maxPredCycle = 1;

    public PredictiveGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    @Override
    public void execute() {
        // reset drawings
        this.paintPreds.clear();

        double keepDistance = this.state.trackingRobot == null ? Double.POSITIVE_INFINITY :
                              this.state.trackingRobot.getHistory(-1).distance - 100; // we do this so we don't switch as often
        for (OtherRobot robot : this.state.otherRobots.values()) {
            OtherRobot.Tick tick = robot.getHistory(-1);
            if (tick.distance < keepDistance) {
                keepDistance = tick.distance;
                this.state.trackingRobot = robot;
            }
        }

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
            double projectileSpeed = MIN_SHOOT_SPEED; // need to be able to turn this into power

            double cutoff = 500.0;
            if (dist < cutoff)
            {
                projectileSpeed = (dist / cutoff) * MIN_SHOOT_SPEED; // Util.firepowerToSpeed(Rules.MIN_BULLET_POWER);
            }
            double minSpeed = Util.firepowerToSpeed(Rules.MAX_BULLET_POWER);
            if (projectileSpeed < minSpeed) // clamp to MIN_VELOCITY
            {
                projectileSpeed = minSpeed;
            }
            nextProjectileSpeed = projectileSpeed;

            double timeSteps = dist / projectileSpeed;

            // decide on prediction
            ProjectedBot.TurnBehaviours tb = ProjectedBot.TurnBehaviours.keepTurn; // defaults
            ProjectedBot.SpeedBehaviours sb = ProjectedBot.SpeedBehaviours.keepSpeed;

            if (predCycle == 1)
            {
                tb = ProjectedBot.TurnBehaviours.keepTurn;
                sb = ProjectedBot.SpeedBehaviours.reverse;
            }

            // initial sub-iter
            this.predVec = this.state.trackingRobot.predictLocation((int)timeSteps + 1, tb, sb);
            this.paintPreds.add(this.predVec);

            // these are adjustment interations for higher accuracy
            for (int i = 0; i < 3; i++) // number of iterations to perform (you'd think more would make it better, but it's hard to tell)
            {
                double afterDist = Util.getDistance(locX - predVec.getX(), locY - predVec.getY());
                double afterTimeSteps = afterDist / projectileSpeed;

                double adqTimeSteps = ((afterTimeSteps - 1.0) * 0.5 + timeSteps * 0.5); // take weighted average (remove 1st +1 adjustment from afterTimeSteps)
                timeSteps = afterTimeSteps - 1.0;
                this.predVec = this.state.trackingRobot.predictLocation((int)adqTimeSteps + 1, tb, sb);
                this.paintPreds.add(this.predVec);
            }
            
            pd = new PredictionData(phs, tb, sb, (int)timeSteps + 1);

            double litDir = Util.getAngle(locX - predVec.getX(), locY - predVec.getY());
            expectedGunDir = litDir;

            double rotation = litDir - this.state.owner.getGunHeading(); // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            this.rotation = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public void firedBullet(TrackedBullet tb) {
        tb.setData(pd);
    }

    @Override
    public void bulletHit(TrackedBullet tb) {
        missCount = 0;
    }

    @Override
    public void bulletMissed(TrackedBullet tb) {
        missCount++;
        if (missCount >= 5)
        {
            //predCycle++; // disabled for testing
            if (predCycle > maxPredCycle)
                predCycle = 0;
            
            missCount = 0;
            
            System.out.println("Switch to pred " + predCycle);
            this.state.owner.setBulletColor(new Color(255, 0, 0));
        }
    }

    @Override
    public void onPaint(Graphics2D g) {

        if (this.state.trackingRobot != null) {

            int a = 36;

            int opacity = 200; // 255 - 200 = 55 is the mininum opacity

            for(Vector vec : this.paintPreds) {

                double x = vec.getX();
                double y = vec.getY();

                if (phs == OtherRobot.PresentHistoryDatas.none)
                    g.setColor(new Color(255, 0, 0, 255 - opacity));
                else if (phs == OtherRobot.PresentHistoryDatas.positionOnly)
                    g.setColor(new Color(255, 85, 0, 255 - opacity));
                else if (phs == OtherRobot.PresentHistoryDatas.positionVelocity)
                    g.setColor(new Color(255, 170, 0, 255 - opacity));
                else if (phs == OtherRobot.PresentHistoryDatas.positionVelocityTurnRate)
                    g.setColor(new Color(255, 255, 0, 255 - opacity));

                g.drawRect((int) x - a/2, (int) y - a/2, a, a);
                opacity -= 200/this.paintPreds.size();
            }
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        this.state.trackingRobot = this.state.otherRobots.get(e.getName());
    }

    // probably not a good idea
    /*@Override
    public void onHitByBullet(HitByBulletEvent e) {
        this.state.trackingRobot = this.state.otherRobots.get(e.getName());
    }*/

}
