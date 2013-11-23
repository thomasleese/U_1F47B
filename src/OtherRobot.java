package bot;

import java.util.ArrayList;
import java.util.List;

public class OtherRobot {

    public static class Tick {

        public long time;
        public boolean isWatching;
        public double bearing;
        public double distance;
        public double energy;

        public Tick(long time, boolean isWatching) {
            this.time = time;
            this.isWatching = isWatching;
        }

        @Override
        public String toString() {
            return "Tick(time=" + this.time + ", isWatching=" + this.isWatching
                + ", bearing=" + this.bearing + ", distance=" + this.distance
                + ", energy=" + this.energy + ")";
        }

    }

    private String name;
    private ArrayList<Tick> history = new ArrayList<Tick>(10000);
    private ArrayList<EnemyBullet> bullets = new ArrayList<EnemyBullet>();

    public OtherRobot(String name) {
        this.name = name;
    }

    public List<Tick> getAllHistory() {
        return this.history;
    }

    public Tick getHistory(int index) {
        return this.history.get(index % this.bullets.size());
    }

    public void pushHistory(Tick tick) {
        this.history.add(tick);
    }

    public List<EnemyBullet> getAllBullets() {
        return this.bullets;
    }

    public EnemyBullet getBullet(int index) {
        return this.bullets.get(index % this.bullets.size());
    }

    public double getGunHeat(long time) {
        EnemyBullet lastBullet = this.getBullet(-1);
        return (1 + (lastBullet.getPower()/5)) - (0.1 * (time - lastBullet.getTime()));
    }

    public boolean predictBulletShot(long time) {

        // check if it's possible that a bullet was shot
        if(this.getGunHeat(time) > 0) {
            return false;
        }

        double power = getHistory(-1).energy - getHistory(-2).energy;

        // TODO: account for being hit by us

        // TODO: account for robot ramming into wall

        // TODO: account for robot ramming other robot

        // check if power is still less than allowed
        if(power < 0.1)
        {
            // assume that robot didn't shoot
            return false;
        }

        // something we didn't account for happened
        if (power > 3) {
            power = 3;
        }

        // TODO: predict angle of bullet
        // this.bullets.add(new EnemyBullet(position, power, angle, time));

        return true;
    }

}
