package bot;

import java.util.HashMap;
import robocode.*;

public class State {

    Robot owner;

	HashMap<String, OtherRobot> otherRobots = new HashMap<String, OtherRobot>();
    OtherRobot latestRobot = null;
	long time = 0;

    public State(Robot robot) {
        this.owner = robot;
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        OtherRobot robot = this.otherRobots.get(e.getName());
        if (robot == null) {
            robot = new OtherRobot(e.getName());
            this.otherRobots.put(e.getName(), robot);
        }

        OtherRobot.Tick tick = new OtherRobot.Tick(this.time);
        tick.bearing = e.getBearing();
        tick.distance = e.getDistance();
        tick.energy = e.getEnergy();
        tick.position = this.calculatePosition(tick.bearing, tick.distance);
        robot.pushHistory(tick);
        this.latestRobot = robot;

        robot.predictBulletShot(this.time);
    }

    private Vector calculatePosition(double bearing, double distance) {
        double angleR = Math.toRadians(bearing + this.owner.getHeading());
        double x = this.owner.getX() + Math.sin(angleR) * distance;
        double y = this.owner.getY() + Math.cos(angleR) * distance;
        return new Vector(x, y);
    }

}