package mega;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import robocode.*;
/**
 * 
 * @author Gates
 *
 */
public class Boilerplate extends RateControlRobot {

	/**
	 * 
	 * @author Gates
	 *
	 */
    private class StrategyComponents {
        public Radar radar;
        public Gun gun;
        public Movement movement;

        /**
         * 
         * @param radar
         * @param gun
         * @param movement
         */
        public StrategyComponents(Radar radar, Gun gun, Movement movement) {
            this.radar = radar;
            this.gun = gun;
            this.movement = movement;
        }
    }

    /**
     * 
     * @author Gates
     *
     */
    public static enum Strategy {
        MELEE, SIMPLE, SPIN, RANDOM, ONEVSONE
    }

    private Map<Strategy, Boilerplate.StrategyComponents> strategyMap;

    private State state;
    // TODO: set these at some point
    private Radar radar;
    private Gun gun;
    private Movement movement;

    /**
     * 
     */
    public Boilerplate() {
        this.state = new State(this);
        this.initStrategies(this.state);
        this.updateStrategy(Strategy.MELEE);
    }

    /**
     * 
     * @param state
     */
    private void initStrategies(final State state) {
        this.strategyMap = new HashMap<Strategy, Boilerplate.StrategyComponents>() {{

            put(Strategy.MELEE, new Boilerplate.StrategyComponents(
                    new RadarPriority(state),
                    new GunPredictive(state, 1.0),
                    new MovementPredictive(state)
            ));
            
            put(Strategy.SIMPLE, new Boilerplate.StrategyComponents(
                    new RadarTracking(state, 1.0),
                    new GunSimple(state, 2.0),
                    new MovementSimple(state, 2.0)
            ));
            
            put(Strategy.RANDOM, new Boilerplate.StrategyComponents(
                    new RadarRandom(state),
                    new GunRandom(state),
                    null
            ));

            put(Strategy.ONEVSONE, new Boilerplate.StrategyComponents(
                    new RadarTracking(state, 2.0),
                    new GunPredictive(state, 1.0),
                    null
            ));

        }};
    }

    /**
     * 
     * @param strategy
     */
    public void updateStrategy(Strategy strategy) {
        Boilerplate.StrategyComponents components = this.strategyMap.get(strategy);

        if (components.radar != null)
            this.radar = components.radar;
        if (components.gun != null)
            this.gun = components.gun;
        if (components.movement != null)
            this.movement = components.movement;
    }

    /**
     * 
     */
    public void getStrategy() {
        if (getOthers() == 1) {
        	 this.updateStrategy(Strategy.ONEVSONE);
        } else if (getOthers() > 1 && getOthers() < 8) {
        	this.updateStrategy(Strategy.MELEE);
        } else if (getOthers() >= 8) {
        	this.updateStrategy(Strategy.SIMPLE);
        }
    }
    /**
     * 
     */
    @Override
    public void run() {
        

        // we want to control the radar manually
        this.setAdjustRadarForRobotTurn(true);
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);

        this.state.battleWidth  = this.getBattleFieldWidth();
        this.state.battleHeight = this.getBattleFieldHeight();
        
        int i = 0;
        
        while (true) {
        	i++;
        	if (i % 7 == 0) {
        		this.paintRobot();
        	}
        	
        	
        	this.getStrategy();
        	
            this.state.advance();

            this.radar.execute();
            this.gun.execute();
            this.movement.execute();

            this.setRadarRotationRate(this.radar.getRotation());

            this.setGunRotationRate(this.gun.getRotation());

            this.setTurnRate(this.movement.getRotation());
            this.setVelocityRate(this.movement.getSpeed());

            if (this.gun.getShouldFire() && this.getGunHeat() == 0) {
                Bullet bullet = this.setFireBullet(this.gun.getBulletPower());
                BulletTracked tb = new BulletTracked(bullet);
                this.gun.firedBullet(tb);
                this.state.ourBullets.add(tb);
                System.out.println("addbull");
            }
            this.execute();
        }
    }

    /**
     * 
     */
    @Override
    public void onBattleEnded(BattleEndedEvent e) {
        System.out.println("The battle is over: " + e);
    }

    /**
     * 
     */
    @Override
    public void onBulletHit(BulletHitEvent e) {
        System.out.println("Out bullet hit a robot: " + e);
        this.state.bulletHitEvents.add(e);
        
        //for (TrackedBullet tb: this.state.ourBullets)
        for (int i = this.state.ourBullets.size() - 1; i >= 0; i--)
        {
            BulletTracked tb = this.state.ourBullets.get(i);
            if (tb.getBullet().hashCode() == e.getBullet().hashCode())
            {
                this.gun.bulletHit(tb);
                this.state.ourBullets.remove(i);
                return;
            }
        }
    }

    /**
     * 
     */
    @Override
    public void onBulletHitBullet(BulletHitBulletEvent e) {
        System.out.println("Our bullet hit another: " + e);
        
        //for (TrackedBullet tb: this.state.ourBullets)
        for (int i = this.state.ourBullets.size() - 1; i >= 0; i--)
        {
            BulletTracked tb = this.state.ourBullets.get(i);
            if (tb.getBullet().hashCode() == e.getBullet().hashCode())
            {
                this.state.ourBullets.remove(i);
                return;
            }
        }
    }

    /**
     * 
     */
    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        System.out.println("Our bullet missed: " + e);
        
        for (int i = this.state.ourBullets.size() - 1; i >= 0; i--)
        {
            BulletTracked tb = this.state.ourBullets.get(i);
            if (tb.getBullet().hashCode() == e.getBullet().hashCode())
            {
                this.gun.bulletMissed(tb);
                this.state.ourBullets.remove(i);
                return;
            }
        }
    }

    /**
     * 
     */
    @Override
    public void onDeath(DeathEvent e) {
        System.out.println("We've died: " + e);
    }

    /**
     * 
     */
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        System.out.println("We've been hit: " + e);
    }

    /**
     * 
     */
    @Override
    public void onHitRobot(HitRobotEvent e) {
        this.radar.onHitRobot(e);
        this.gun.onHitRobot(e);
        this.movement.onHitRobot(e);

        this.state.hitRobotEvents.add(e);
    }

    /**
     * 
     */
    @Override
    public void onHitWall(HitWallEvent e) {
        System.out.println("We've crashed into a wall: " + e);
    }

    /**
     * 
     */
    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);

        this.radar.onPaint(g);
        this.gun.onPaint(g);
        this.movement.onPaint(g);

        for(OtherRobot r : this.state.otherRobots.values()) {
            r.onPaint(g, this.getTime());
        }
    }

    /**
     * 
     */
    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        this.state.onRobotDeath(e);
    }

    @Override
    public void onRoundEnded(RoundEndedEvent e) {
        System.out.println("The round is over: " + e);
    }

    /**
     * 
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        this.state.onScannedRobot(e);
    }

    /**
     * 
     */
    @Override
    public void onWin(WinEvent e) {
        System.out.println("We won! " + e);
        
        for (int i = 0; i < 50; i++) {
			turnRight(100);
			turnLeft(100);
		}
    }

    /**
     * 
     */
    @Override
    public void onSkippedTurn(SkippedTurnEvent e) {
        System.out.println("Oh no, we skipped a turn! " + e);
    }
    
    /**
     * 
     */
    public void paintRobot() {
    	float color1 = (float) Math.random();
    	float color2 = (float) Math.random();
    	float color3 = (float) Math.random();
		setBodyColor(new Color(color1, color2, color3));
        setGunColor(new Color(color1, color2, color3));
        setRadarColor(new Color(color1, color2, color3));
        setBulletColor(new Color(color1, color2, color3));
        setScanColor(new Color(color1, color2, color3));
    }
}
