package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import robocode.*;

public class Bot extends RateControlRobot {

    private HashMap<String, OtherRobot> otherRobots = new HashMap<String, OtherRobot>();

    public Bot() {
        
    }

    @Override
    public void run() {
        setColors(new Color(0, 40, 43),
                  new Color(0, 96, 102),
                  new Color(0, 120, 128));

        // we want to control the radar manually
        this.setAdjustRadarForRobotTurn(true);
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);

        while (true) {
            this.setTurnRadarLeft(45);
            this.scan();

            if(this.getGunHeat() == 0) {
                this.setFireBullet(Rules.MIN_BULLET_POWER);
            }

            this.execute();
        }
    }

    @Override
    public void onBattleEnded(BattleEndedEvent e) {
        System.out.println("The battle is over: " + e);
    }

    @Override
    public void onBulletHit(BulletHitEvent e) {
        System.out.println("Out bullet hit a robot: " + e);
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent e) {
        System.out.println("Our bullet hit another: " + e);
    }

    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        System.out.println("Our bullet missed: " + e);
    }

    @Override
    public void onDeath(DeathEvent e) {
        System.out.println("We've died: " + e);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        System.out.println("We've been hit: " + e);
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        System.out.println("We've crashed into a robot: " + e);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        System.out.println("We've crashed into a wall: " + e);
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);
        // perhaps we should do some debug drawing ehre
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        System.out.println("Someone else has died: " + e);
    }

    @Override
    public void onRoundEnded(RoundEndedEvent e) {
        System.out.println("The round is over: " + e);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // we don't need to use this
    }

    private void updateOtherRobots() {
        ArrayList<OtherRobot> robotsDealtWith = new ArrayList<OtherRobot>();

        for (ScannedRobotEvent ev : this.getScannedRobotEvents()) {
            OtherRobot robot = this.otherRobots.get(ev.getName());
            if (robot == null) {
                robot = new OtherRobot(ev.getName());
                this.otherRobots.put(ev.getName(), robot);
            }

            OtherRobot.Tick tick = new OtherRobot.Tick(this.getTime(), true);
            tick.bearing = ev.getBearing();
            tick.distance = ev.getDistance();
            tick.energy = ev.getEnergy();
            robot.pushHistory(tick);
            robotsDealtWith.add(robot);
        }

        // add any "not watching" events to robots not dealth with
        for (Map.Entry<String, OtherRobot> entry : this.otherRobots.entrySet()) {
            OtherRobot robot = entry.getValue();
            if (!robotsDealtWith.contains(robot)) {
                // we can guarantee that there will be at least one tick
                OtherRobot.Tick lastTick = robot.getHistory(-1);
                if (lastTick.isWatching) {
                    // we were watching it, but we've lost contact
                    robot.pushHistory(new OtherRobot.Tick(this.getTime(), false));
                }
            }
        }
    }

    @Override
    public void onStatus(StatusEvent e) {
        updateOtherRobots();
    }

    @Override
    public void onWin(WinEvent e) {
        System.out.println("We won! " + e);
    }

}
