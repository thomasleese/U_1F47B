package bot;

import robocode.util.Utils;

public class SimpleGun extends Gun {

    private double coefficient;

    public SimpleGun(State state, double coefficient) {
        super(state);
        this.coefficient = coefficient;
    }

    public double getDist(double ax, double ay, double bx, double, by)
    {
        double x = ax - bx;
        double y = ay - by;
        return Math.Sqrt(x * x + y * y);
    }

    public double getAngle(double ax, double ay, double bx, double, by)
    {
        double x = ax - bx;
        double y = ay - by;
        return Math.Atan2(y, x);
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
            double targX = tick.x;
            double targY = tick.y;

            double dist = tick.distance;
            double projectileSpeed = 19.7; // need to be able to turn this into power

            double timeSteps = dist / projectileSpeed;

            double velX = 0;
            double velY = 0;

            double afterDist = getDist(locX, locY, targX + velX * timeSteps, targY + velY * timeSteps);
            double afterTimeSteps = afterDist / projectileSpeed;

            double adqTimeSteps = (afterTimeSteps + timeSteps) * 0.5;

            double litDir = getAngle(locX, locY, u.locX + velX * adqTimeSteps, u.locY + velY * adqTimeSteps);

            double rotation = litDir + this.state.owner.getHeading() - // absolute rotation to enemy
                              this.state.owner.getGunHeading(); // relative rotation to gun
            this.rotation = this.coefficient * Utils.normalRelativeAngleDegrees(rotation); // normalise
        } else {
            // leave the rotation as it is allready
        }
    }

}
