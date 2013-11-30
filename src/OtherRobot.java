package bot;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import robocode.*;

public class OtherRobot implements Comparable<OtherRobot> {

    public static class Tick implements Comparable<Tick> {

        public long time;
        public double bearing;
        public double distance;
        public double energy;

        public Vector position;
        public Vector velocity;
        public double turnRate;

        public Tick(long time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Tick(time=" + this.time + ", bearing=" + this.bearing
                + ", distance=" + this.distance + ", energy=" + this.energy + ")";
        }

        @Override
        public int compareTo(Tick t) {
            return this.time < t.time ?  1:
                   this.time > t.time ? -1:
                /* this.time ==t.time */ 0;
        }

    }
    
    private String name;
    private ArrayList<Tick> history = new ArrayList<Tick>(10000);
    private ArrayList<BulletWave> bulletWaves = new ArrayList<BulletWave>();

    public OtherRobot(String name) {
        this.name = name;
    }

    public void advance() {
        Iterator<BulletWave> it = this.bulletWaves.iterator();
        while (it.hasNext()) {
            if (!it.next().advance()) {
                it.remove();
            }
        }
    }

    @Override
    public int compareTo(OtherRobot robot) {
        return this.getHistory(-1).compareTo(robot.getHistory(-1));
    }

    public List<Tick> getAllHistory() {
        return this.history;
    }

    public Tick getHistory(int index) {
        if (this.history.size() == 0) {
            return null;
        }
        return this.history.get(Util.modulo(index, this.history.size()));
    }

    public int getHistorySize() {
        return this.history.size();
    }

    public void pushHistory(Tick tick) {
        this.history.add(tick);
    }

    public List<BulletWave> getAllBullets() {
        return this.bulletWaves;
    }

    public BulletWave getBullet(int index) {
        if (this.bulletWaves.size() == 0) {
            return null;
        }
        return this.bulletWaves.get(Util.modulo(index, this.bulletWaves.size()));
    }

    public String getName() {
        return this.name;
    }

    public double getGunHeat(long time) {
        BulletWave lastBullet = this.getBullet(-1);
        if (lastBullet == null) {
            return 0.0;
        }
        return (1 + (lastBullet.getPower()/5)) - (0.1 * lastBullet.getFlightTime());
    }
    
    public Vector predictLocation(int timeFrame, ProjectedBot.TurnBehaviours tb, ProjectedBot.SpeedBehaviours sb)
    {
        ProjectedBot pb = new ProjectedBot(getHistory(-1));
        pb.project(timeFrame, tb, sb);
        return pb.getPosition();
    }
    
    public enum PresentHistoryDatas
    {
        none,
        positionOnly,
        positionVelocity,
        positionVelocityTurnRate,
    }
    
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

    private double calcHitWallPowerDiff(OtherRobot.Tick tick, State state) {
        double width = state.owner.getBattleFieldWidth();
        double height = state.owner.getBattleFieldHeight();
        double marginTopBottom = Util.ROBOT_HEIGHT / 2;
        double marginLeftRight = Util.ROBOT_WIDTH / 2;

        if (Util.isOutOfBattleField(tick.position, width, height,
                                     marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom)) {
            return Rules.getWallHitDamage(tick.velocity.length());
        }

        return 0;
    }

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
        power -= this.calcHitWallPowerDiff(previous, state);

        // TODO: account for robot ramming other robot

        // check if power is still less than allowed
        if (power < 0.1) {
            // assume that robot didn't shoot
            return false;
        }

        // something we didn't account for happened
        if (power > 3) {
            power = 3;
        }

        this.bulletWaves.add(new BulletWave(previous.position, power, 360, confidence));

        return true;
    }

    public void onPaint(Graphics2D g, long time) {
        for (BulletWave bw : this.bulletWaves) {
            bw.onPaint(g);
        }
    }

}
