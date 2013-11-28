
package bot;

import java.util.ArrayList;
import java.util.List;

public class OtherRobot implements Comparable<OtherRobot> {

    public static class Tick implements Comparable<Tick> {

        public long time;
        public double bearing;
        public double distance;
        public double energy;

        public Vector position;

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
    private ArrayList<VirtualBullet> bullets = new ArrayList<VirtualBullet>();

    public OtherRobot(String name) {
        this.name = name;
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

    public List<VirtualBullet> getAllBullets() {
        return this.bullets;
    }

    public VirtualBullet getBullet(int index) {
        if (this.bullets.size() == 0) {
            return null;
        }
        return this.bullets.get(Util.modulo(index, this.bullets.size()));
    }

    public String getName() {
        return this.name;
    }

    public double getGunHeat(long time) {
        VirtualBullet lastBullet = this.getBullet(-1);
        if (lastBullet == null) {
            return 0.0;
        }
        return (1 + (lastBullet.getPower()/5)) - (0.1 * (time - lastBullet.getTime()));
    }

    public boolean predictBulletShot(long time) {

        // check if it's possible that a bullet was shot
        if (this.getGunHeat(time) > 0) {
            return false;
        }

        OtherRobot.Tick previous = this.getHistory(-1);
        OtherRobot.Tick penultimate = this.getHistory(-2);

        if (previous == null || penultimate == null) {
            // we don't know what the shot power is
            // assume that no shot has been made
            return false;
        }

        double power = Util.roundTo1(penultimate.energy - previous.energy);

        // TODO: account for being hit by us

        // TODO: account for robot ramming into wall

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

        // TODO: predict angle of bullet
        // this.bullets.add(new VirtualBullet(position, power, angle, time));

        return true;
    }

}
