package bot;

import robocode.util.Utils;

public class SimplePredictiveGun extends Gun {

    private double coefficient;

    public SimplePredictiveGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    public double getDist(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return Math.sqrt(x * x + y * y);
    }

    public double getAngle(double ax, double ay, double bx, double by)
    {
        double x = ax - bx;
        double y = ay - by;
        return -90 - Math.toDegrees(Math.atan2(y, x));
    }

    @Override
    public void execute() {
        // does not use coefficient
        if(this.state.latestRobot != null) {
            OtherRobot.Tick tick = this.state.latestRobot.getHistory(-1);
            
            // need a way to approximate velocity using history
            // if no history, wait a bit so we have some?
            double locX = this.state.owner.getX();
            double locY = this.state.owner.getY();
            double targX = tick.position.getX();
            double targY = tick.position.getY();

            double dist = tick.distance;
            double projectileSpeed = 19.7; // need to be able to turn this into power

            double timeSteps = dist / projectileSpeed;

            double velX = 0;
            double velY = 0;

            double afterDist = getDist(locX, locY, targX + velX * timeSteps, targY + velY * timeSteps);
            double afterTimeSteps = afterDist / projectileSpeed;

            double adqTimeSteps = (afterTimeSteps + timeSteps) * 0.5;

            double litDir = getAngle(locX, locY, targX + velX * adqTimeSteps, targY + velY * adqTimeSteps);

            double rotation = litDir - this.state.owner.getGunHeading(); // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            // leave the rotation as it is allready
        }
    }

}
