package bot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import robocode.*;

public class Bot extends RateControlRobot {

    private State state;
    // TODO: set these at some point
    private Radar radar;
    private Gun gun;
    private Base base;

    public Bot() {
        this.state = new State(this);
        this.radar = new TrackingRadar(this.state, 1.6);
        this.gun   = new SimpleGun(this.state, 1.0);
        this.base  = new SimpleBase(this.state, 2.0);
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

            this.setRadarRotationRate(this.radar.getRotation());

            this.setGunRotationRate(this.gun.getRotation());

            this.setTurnRate(this.base.getRotation());
            this.setVelocityRate(this.base.getSpeed());

            if (this.getGunHeat() == 0) {
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
        this.state.onScannedRobot(e);
    }

    @Override
    public void onWin(WinEvent e) {
        System.out.println("We won! " + e);
    }
}
