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
        robot.pushHistory(tick);

        this.latestRobot = robot;

        robot.predictBulletShot(this.time);
    }

}