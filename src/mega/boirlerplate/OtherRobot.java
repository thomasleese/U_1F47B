package mega.boirlerplate;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import robocode.*;

/**
 * 
 * @author Gates
 *
 */
public class OtherRobot implements Comparable<OtherRobot> {
	
	/**
	 * 
	 * @author Gates
	 *
	 */
    public static class Tick implements Comparable<Tick> {

        public long time;
        public double bearing;
        public double distance;
        public double energy;

        public Vector position;
        public Vector velocity;
        public double turnRate;

        /**
         * 
         * @param time
         */
        public Tick(long time) {
            this.time = time;
        }

        /**
         * @return
         */
        @Override
        public String toString() {
            return "Tick(time=" + this.time + ", bearing=" + this.bearing
                + ", distance=" + this.distance + ", energy=" + this.energy
                + ", position=" + this.position + ", velocity=" + this.velocity
                + ", turnRate=" + this.turnRate + ")";
        }

        /**
         * 
         */
        @Override
        public int compareTo(Tick t) {
            return this.time < t.time ?  1:
                   this.time > t.time ? -1:
                /* this.time ==t.time */ 0;
        }

    }

    private String name;
    private ArrayList<Tick> history = new ArrayList<Tick>(10000);
    private ArrayList<WaveBullet> bulletWaves = new ArrayList<WaveBullet>();

    /**
     * 
     * @param name
     */
    public OtherRobot(String name) {
        this.name = name;
    }

    /**
     * 
     */
    public void advance() {
        Iterator<WaveBullet> it = this.bulletWaves.iterator();
        while (it.hasNext()) {
            if (!it.next().advance()) {
                it.remove();
            }
        }
    }

    /**
     * 
     */
    @Override
    public int compareTo(OtherRobot robot) {
        return this.getHistory(-1).compareTo(robot.getHistory(-1));
    }

    /**
     * 
     * @return
     */
    public List<Tick> getAllHistory() {
        return this.history;
    }

    /**
     * 
     * @param index
     * @return
     */
    public Tick getHistory(int index) {
        if (this.history.size() == 0) {
            return null;
        }
        return this.history.get(Util.modulo(index, this.history.size()));
    }

    /**
     * 
     * @return
     */
    public int getHistorySize() {
        return this.history.size();
    }

    /**
     * 
     * @param tick
     */
    public void pushHistory(Tick tick) {
        this.history.add(tick);
    }

    /**
     * 
     * @return
     */
    public List<WaveBullet> getAllBullets() {
        return this.bulletWaves;
    }

    /**
     * 
     * @param index
     * @return
     */
    public WaveBullet getBullet(int index) {
        if (this.bulletWaves.size() == 0) {
            return null;
        }
        return this.bulletWaves.get(Util.modulo(index, this.bulletWaves.size()));
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @param time
     * @return
     */
    public double getGunHeat(long time) {
        WaveBullet lastBullet = this.getBullet(-1);
        if (lastBullet == null) {
            return 0.0;
        }
        return (1 + (lastBullet.getPower()/5)) - (0.1 * lastBullet.getFlightTime());
    }

    /**
     * 
     * @param timeFrame
     * @param tb
     * @param sb
     * @param state
     * @return
     */
    public Vector predictLocation(int timeFrame, ProjectedBot.TurnBehaviours tb, ProjectedBot.SpeedBehaviours sb, State state)
    {
        ProjectedBot pb = new ProjectedBot(getHistory(-1));
        pb.project(timeFrame, tb, sb, state);
        return pb.getPosition();
    }

    /**
     * 
     * @author Gates
     *
     */
    public enum PresentHistoryDatas
    {
        none,
        positionOnly,
        positionVelocity,
        positionVelocityTurnRate,
    }

    /**
     * 
     * @param time
     * @return
     */
    public PresentHistoryDatas availablePresentHistoryData(long time)
    {
        if (time - 0 != getHistory(-1).time)
            return PresentHistoryDatas.none;
        else if (time - 1 != getHistory(-2).time)
            return PresentHistoryDatas.positionOnly;
        else if (time - 2 != getHistory(-3).time)
            return PresentHistoryDatas.positionVelocity;
        else
            return PresentHistoryDatas.positionVelocityTurnRate;
    }

    /**
     * 
     * @param time
     * @param state
     * @return
     */
    public boolean predictBulletShot(long time, State state) {

        // check if it's possible that a bullet was shot
        if (this.getGunHeat(time) > 0) {
            return false;
        }

        OtherRobot.Tick current = this.getHistory(-1);
        OtherRobot.Tick previous = this.getHistory(-2);

        if (current == null || previous == null || current == previous) {
            // we don't know what the shot power is
            // assume that no shot has been made
            return false;
        }

        double confidence = 1.0 / (current.time - previous.time);
        double power = Util.roundTo1(previous.energy - current.energy);

        // remove any power that would've been caused by us hitting it
        for (BulletHitEvent e : state.bulletHitEvents) {
            if (e.getName().equals(this.name)) {
                power -= Rules.getBulletDamage(e.getBullet().getPower());
            }
        }

        // remove any power that would be caused by crashing
        double battlefieldWidth = state.owner.getBattleFieldWidth();
        double battlefieldHeight = state.owner.getBattleFieldHeight();
        double marginTopBottom = Util.ROBOT_HEIGHT / 2 - 2; // -2 to make detection more lenient
        double marginLeftRight = Util.ROBOT_WIDTH / 2 - 2;

        if (Util.isOutOfBattleField(previous.position, battlefieldWidth, battlefieldHeight,
                                    marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom)) {
            confidence *= 0.9;
            double speedChange = previous.velocity.length() - current.velocity.length();
            if (Math.abs(speedChange) >= 2.0) {
                power -= Rules.getWallHitDamage(previous.velocity.length());
            }
        }

        // remove any power that would've been lost if we rammed into them
        for (HitRobotEvent e : state.hitRobotEvents) {
            if (e.getName().equals(this.name)) {
                power -= Rules.ROBOT_HIT_DAMAGE;
            }
        }

        // check if power is still less than allowed
        if (power < 0.1) {
            // assume that robot didn't shoot
            return false;
        }

        // something we didn't account for happened
        if (power > 3) {
            power = 3;
        }

        // assume the robot was aiming at us
        int angle = (int) Util.getAngle(previous.position.getX(), previous.position.getY(),
                                        state.owner.getX(), state.owner.getY());
        this.bulletWaves.add(new WaveBullet(previous.position, power, angle - 10, angle + 10, confidence, state));

        return true;
    }

    /**
     * 
     * @param g
     * @param time
     */
    public void onPaint(Graphics2D g, long time) {
        for (WaveBullet bw : this.bulletWaves) {
            bw.onPaint(g);
        }
    }

}
