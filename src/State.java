package bot;

import java.util.Map;
import java.util.HashMap;
import robocode.*;
import robocode.util.*;

public class State {

    AdvancedRobot owner;

    Map<String, OtherRobot> otherRobots;
    OtherRobot latestRobot;
    OtherRobot trackingRobot;

    public State(AdvancedRobot robot) {
        this.owner = robot;
        this.otherRobots = new HashMap<String, OtherRobot>();
        this.latestRobot = null;
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        OtherRobot robot = this.otherRobots.get(e.getName());
        if (robot == null) {
            robot = new OtherRobot(e.getName());
            this.otherRobots.put(e.getName(), robot);
        }

        OtherRobot.Tick tick = new OtherRobot.Tick(this.owner.getTime());
        tick.bearing = e.getBearing();
        tick.distance = e.getDistance();
        tick.energy = e.getEnergy();
        tick.position = this.calculatePosition(tick.bearing, tick.distance);
        robot.pushHistory(tick);
        tick.velocity = State.calculateVelocity(robot.getHistory(-2).position, tick.position); // this needs to be smarter
        tick.turnRate = State.calculateTurnRate(robot.getHistory(-2).velocity, tick.velocity);
        this.latestRobot = robot;

        robot.predictBulletShot(this.owner.getTime());
    }

    public void onRobotDeath(RobotDeathEvent e) {
        System.out.println("Someone else has died: " + e);

        // Remove dead robot from otherRobots
        String name = e.getName();
        this.otherRobots.remove(name);

        // clear latest robot if it died
        if (this.latestRobot != null && this.latestRobot.getName().equals(name)) {
            this.latestRobot = null;
        }

        // clear tracking robot if it died
        // is the code above required now? is this the right place to do this?
        if (this.trackingRobot != null && this.trackingRobot.getName().equals(name)) {
            this.trackingRobot = null;
        }

    }

    private Vector calculatePosition(double bearing, double distance) {
        double angleR = Math.toRadians(bearing + this.owner.getHeading());
        double x = this.owner.getX() + Math.sin(angleR) * distance;
        double y = this.owner.getY() + Math.cos(angleR) * distance;
        return new Vector(x, y);
    }

    private static Vector calculateVelocity(Vector oldPosition, Vector newPosition) {
        return new Vector(newPosition.getX() - oldPosition.getX(),
                          newPosition.getY() - oldPosition.getY());
    }

    private static double calculateTurnRate(Vector oldVelocity, Vector newVelocity) {
        return Utils.normalRelativeAngleDegrees(newVelocity.getAngle() - oldVelocity.getAngle());
    }

}
