package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.*;

public class Bot extends RateControlRobot {

    public Bot() {
        
    }

    @Override
    public void run() {
        setColors(new Color(0, 40, 43), 
                  new Color(0, 96, 102),
                  new Color(0, 120, 128));
        
        while (true) {
            // temporary robot logic for testing
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
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
        System.out.println("Seen a robot: " + e);
    }

    @Override
    public void onStatus(StatusEvent e) {
        System.out.println("We've got some status information: " + e);
    }

    @Override
    public void onWin(WinEvent e) {
        System.out.println("We won! " + e);
    }

}
