package U_1F47B;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import robocode.*;

public class U_1F47B extends RateControlRobot {

    private class StrategyComponents {
        public Radar radar;
        public Gun gun;
        public Base base;

        public StrategyComponents(Radar radar, Gun gun, Base base) {
            this.radar = radar;
            this.gun = gun;
            this.base = base;
        }
    }

    public static enum Strategy {
        MELEE, ONEVSONE
    }

    private Map<Strategy, U_1F47B.StrategyComponents> strategyMap;

    private State state;
    // TODO: set these at some point
    private Radar radar;
    private Gun gun;
    private Base base;

    public U_1F47B() {
        this.state = new State(this);
        this.initStrategies(this.state);
        this.updateStrategy(Strategy.MELEE);
    }

    private void initStrategies(final State state) {
        this.strategyMap = new HashMap<Strategy, U_1F47B.StrategyComponents>() {{

            put(Strategy.MELEE, new U_1F47B.StrategyComponents(
                    new PriorityRadar(state),
                    new PredictiveGun(state, 1.0),
                    new PredictiveBase(state)
            ));

            put(Strategy.ONEVSONE, new U_1F47B.StrategyComponents(
                    new TrackingRadar(state, 2.0),
                    new PredictiveGun(state, 1.0),
                    null
            ));

        }};
    }

    public void updateStrategy(Strategy strategy) {
        U_1F47B.StrategyComponents components = this.strategyMap.get(strategy);

        if (components.radar != null)
            this.radar = components.radar;
        if (components.gun != null)
            this.gun = components.gun;
        if (components.base != null)
            this.base = components.base;
    }

    @Override
    public void run() {
        setBulletColor(new Color(70, 77, 106));

        // we want to control the radar manually
        this.setAdjustRadarForRobotTurn(true);
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);

        int i = 0;
        while (true) {
            switch (i) {
                case 0: setColors(Color.RED, Color.GREEN, Color.BLUE); break;
                case 1: setColors(Color.BLUE, Color.RED, Color.GREEN); break;
                case 2: setColors(Color.GREEN, Color.BLUE, Color.RED); break;
            }
            i = (i + 1) % 3;

            // switch to 1vs1 Components when only one other is left
            if (this.getOthers() == 1) {
                this.updateStrategy(Strategy.ONEVSONE);
            }

            this.state.advance();

            this.radar.execute();
            this.gun.execute();
            this.base.execute();

            this.setRadarRotationRate(this.radar.getRotation());

            this.setGunRotationRate(this.gun.getRotation());

            this.setTurnRate(this.base.getRotation());
            this.setVelocityRate(this.base.getSpeed());

            if (this.gun.getShouldFire() && this.getGunHeat() == 0) {
                this.setFireBullet(this.gun.getBulletPower());
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
        this.state.bulletHitEvents.add(e);
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
        this.radar.onHitRobot(e);
        this.gun.onHitRobot(e);
        this.base.onHitRobot(e);

        this.state.hitRobotEvents.add(e);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        System.out.println("We've crashed into a wall: " + e);
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);

        this.radar.onPaint(g);
        this.gun.onPaint(g);
        this.base.onPaint(g);

        for(OtherRobot r : this.state.otherRobots.values()) {
            r.onPaint(g, this.getTime());
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        this.state.onRobotDeath(e);
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

    @Override
    public void onSkippedTurn(SkippedTurnEvent e) {
        System.out.println("Oh no, we skipped a turn! " + e);
    }

}
